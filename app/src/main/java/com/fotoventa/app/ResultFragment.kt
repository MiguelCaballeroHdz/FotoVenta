package com.fotoventa.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fotoventa.app.databinding.FragmentResultBinding

/** Anuncio final (SPEC-05): foto grande arriba, texto abajo. */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity() as MainActivity
        val vm = activity.viewModel
        val isPremium = activity.premiumManager.isPremium()
        val adText = vm.buildAdText(isPremium)

        binding.imageCar.setImageURI(vm.carPhotoUri)
        binding.textAd.text = adText

        binding.buttonShareWhatsapp.setOnClickListener {
            ShareHelper.shareToWhatsApp(requireContext(), vm.carPhotoUri, adText)
        }
        binding.buttonCopy.setOnClickListener {
            ShareHelper.copyText(requireContext(), adText)
        }
        binding.buttonSaveImage.setOnClickListener {
            val name = "fotoventa_${System.currentTimeMillis()}.png"
            ShareHelper.saveAdImage(requireContext(), binding.layoutResult, name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
