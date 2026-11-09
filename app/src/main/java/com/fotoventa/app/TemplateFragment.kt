package com.fotoventa.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fotoventa.app.databinding.FragmentTemplateBinding

/** Plantilla genérica con campos vacíos (SPEC-01). */
class TemplateFragment : Fragment() {

    private var _binding: FragmentTemplateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity() as MainActivity
        val vm = activity.viewModel
        binding.editMarca.setText(vm.marcaModelo)
        binding.editPrecio.setText(vm.precio)
        binding.editKm.setText(vm.km)
        binding.editAnio.setText(vm.anio)
        binding.editUbicacion.setText(vm.ubicacion)
        binding.editWhatsapp.setText(vm.whatsapp)
        binding.editDescripcion.setText(vm.descripcion)

        binding.buttonGenerate.setOnClickListener {
            vm.marcaModelo = binding.editMarca.text.toString().trim()
            vm.precio = binding.editPrecio.text.toString().trim()
            vm.km = binding.editKm.text.toString().trim()
            vm.anio = binding.editAnio.text.toString().trim()
            vm.ubicacion = binding.editUbicacion.text.toString().trim()
            vm.whatsapp = binding.editWhatsapp.text.toString().trim()
            vm.descripcion = binding.editDescripcion.text.toString().trim()
            activity.attemptGenerate()
        }

        binding.buttonPremium.setOnClickListener {
            if (activity.premiumManager.isPremium()) {
                Toast.makeText(requireContext(), "Ya tienes Premium", Toast.LENGTH_SHORT).show()
            } else {
                activity.buyPremium()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
