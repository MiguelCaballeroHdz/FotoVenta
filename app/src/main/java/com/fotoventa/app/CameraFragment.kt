package com.fotoventa.app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.fotoventa.app.databinding.FragmentCameraBinding
import java.io.File

/**
 * Cámara reutilizable (SPEC-01). Paso CAR obligatorio; SHEET y SPECS opcionales.
 */
class CameraFragment : Fragment() {

    companion object {
        const val STEP_CAR = "CAR"
        const val STEP_SHEET = "SHEET"
        const val STEP_SPECS = "SPECS"
        private const val ARG_STEP = "step"

        fun newInstance(step: String) = CameraFragment().apply {
            arguments = Bundle().apply { putString(ARG_STEP, step) }
        }
    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private var photoFile: File? = null
    private var step: String = STEP_CAR

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCamera() else
            Toast.makeText(requireContext(), "Se necesita la cámara", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        step = arguments?.getString(ARG_STEP) ?: STEP_CAR
        binding.textStep.text = when (step) {
            STEP_SHEET -> getString(R.string.step_sheet)
            STEP_SPECS -> getString(R.string.step_specs)
            else -> getString(R.string.step_car)
        }
        // Saltar solo existe en las fotos opcionales.
        binding.buttonSkip.visibility = if (step == STEP_CAR) View.GONE else View.VISIBLE

        binding.buttonSkip.setOnClickListener { onSkip() }
        binding.buttonCapture.setOnClickListener { takePhoto() }
        binding.buttonRetake.setOnClickListener { showPreview(false) }
        binding.buttonUsePhoto.setOnClickListener { onPhotoConfirmed() }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val providerFuture = ProcessCameraProvider.getInstance(requireContext())
        providerFuture.addListener({
            val provider = providerFuture.get()
            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(binding.previewView.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()
            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    viewLifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
                )
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val capture = imageCapture ?: return
        val dir = File(requireContext().cacheDir, "fotos").apply { mkdirs() }
        val file = File(dir, "foto_${step}_${System.currentTimeMillis()}.jpg")
        val output = ImageCapture.OutputFileOptions.Builder(file).build()
        capture.takePicture(
            output,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    photoFile = file
                    binding.imagePreview.setImageURI(Uri.fromFile(file))
                    showPreview(true)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showPreview(show: Boolean) {
        binding.previewView.visibility = if (show) View.GONE else View.VISIBLE
        binding.imagePreview.visibility = if (show) View.VISIBLE else View.GONE
        binding.buttonCapture.visibility = if (show) View.GONE else View.VISIBLE
        binding.buttonSkip.visibility = if (show) View.GONE else
            if (step == STEP_CAR) View.GONE else View.VISIBLE
        binding.buttonUsePhoto.visibility = if (show) View.VISIBLE else View.GONE
        binding.buttonRetake.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun photoUri(): Uri? = photoFile?.let {
        FileProvider.getUriForFile(requireContext(), "com.fotoventa.app.fileprovider", it)
    }

    private fun onPhotoConfirmed() {
        val activity = requireActivity() as MainActivity
        val uri = photoUri() ?: return
        when (step) {
            STEP_CAR -> {
                activity.viewModel.carPhotoUri = uri
                activity.goToSheetPhoto()
            }
            STEP_SHEET -> {
                activity.viewModel.sheetPhotoUri = uri
                activity.goToOcr(uri, OCRFragment.TARGET_SHEET)
            }
            STEP_SPECS -> {
                activity.viewModel.specsPhotoUri = uri
                activity.goToOcr(uri, OCRFragment.TARGET_SPECS)
            }
        }
    }

    private fun onSkip() {
        val activity = requireActivity() as MainActivity
        when (step) {
            STEP_SHEET -> activity.goToSpecsPhoto()
            STEP_SPECS -> activity.goToTemplate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
