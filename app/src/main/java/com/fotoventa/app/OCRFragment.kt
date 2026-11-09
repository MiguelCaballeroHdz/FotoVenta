package com.fotoventa.app

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fotoventa.app.databinding.FragmentOcrBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * OCR con ML Kit on-device (SPEC-02). Si falla, edición manual; nunca bloquea.
 */
class OCRFragment : Fragment() {

    companion object {
        const val TARGET_SHEET = "SHEET"
        const val TARGET_SPECS = "SPECS"
        private const val ARG_URI = "uri"
        private const val ARG_TARGET = "target"

        fun newInstance(uri: Uri, target: String) = OCRFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_URI, uri.toString())
                putString(ARG_TARGET, target)
            }
        }
    }

    private var _binding: FragmentOcrBinding? = null
    private val binding get() = _binding!!
    private var target: String = TARGET_SHEET

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOcrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        target = arguments?.getString(ARG_TARGET) ?: TARGET_SHEET
        val uriString = arguments?.getString(ARG_URI) ?: return
        val uri = Uri.parse(uriString)
        binding.imageSheet.setImageURI(uri)
        runOcr(uri)

        binding.buttonOcrOk.setOnClickListener {
            applyTextAndContinue()
        }
    }

    private fun runOcr(uri: Uri) {
        val image = try {
            InputImage.fromFilePath(requireContext(), uri)
        } catch (_: Exception) {
            onOcrFailed()
            return
        }
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(image)
            .addOnSuccessListener { result ->
                val text = result.text.trim()
                if (text.isEmpty()) {
                    onOcrFailed()
                } else {
                    binding.textOcrStatus.text = "Revisa el texto antes de generar"
                    binding.editOcrText.setText(text)
                    extractFields(text)
                }
            }
            .addOnFailureListener { onOcrFailed() }
    }

    private fun onOcrFailed() {
        binding.textOcrStatus.text = getString(R.string.ocr_failed)
        binding.editOcrText.setText("")
        Toast.makeText(requireContext(), getString(R.string.ocr_failed), Toast.LENGTH_SHORT).show()
    }

    /** Extrae precio/km/año/WhatsApp si aparecen; el resto va a descripción (SPEC-02). */
    private fun extractFields(text: String) {
        val activity = requireActivity() as MainActivity
        val vm = activity.viewModel
        Regex("""\$?\s?([\d.,]{4,})""").find(text)?.groupValues?.getOrNull(1)?.let {
            if (vm.precio.isBlank()) vm.precio = it
        }
        Regex("""(\d[\d.,]*)\s?km""", RegexOption.IGNORE_CASE).find(text)?.groupValues?.getOrNull(1)?.let {
            if (vm.km.isBlank()) vm.km = it
        }
        Regex("""\b(19|20)\d{2}\b""").find(text)?.value?.let {
            if (vm.anio.isBlank()) vm.anio = it
        }
        Regex("""\b\d{10}\b""").find(text)?.value?.let {
            if (vm.whatsapp.isBlank()) vm.whatsapp = it
        }
        if (vm.descripcion.isBlank()) vm.descripcion = text
    }

    private fun applyTextAndContinue() {
        val activity = requireActivity() as MainActivity
        // El texto corregido por el usuario es el que llega al anuncio.
        activity.viewModel.descripcion = binding.editOcrText.text.toString().trim()
        if (target == TARGET_SHEET) activity.goToSpecsPhoto() else activity.goToTemplate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
