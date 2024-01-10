package com.example.juegotablero.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.juegotablero.R

class MenuNuevaPreguntaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_menu_nueva_pregunta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



          elegirPregunta()
    }

    fun elegirPregunta(){
        val buttonAdivinaPalabra = view?.findViewById<View>(R.id.btnAdivinaPalabra)
        val buttonTest = view?.findViewById<View>(R.id.btnTest)
        val buttonParejas = view?.findViewById<View>(R.id.btnParejas)
        val buttonRepaso = view?.findViewById<View>(R.id.btnRepaso)
        val buttonPruebaFinal = view?.findViewById<View>(R.id.btnPruebaFinal)

        buttonAdivinaPalabra?.setOnClickListener {
            val formularioAPFragment = FormularioAPFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, formularioAPFragment)
                .addToBackStack(null)
                .commit()
        }

        buttonTest?.setOnClickListener {
            val formularioEORFragment = FormularioEORFragment()
            val bundle = Bundle()
            bundle.putString("tipoPregunta", "test")
            formularioEORFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, formularioEORFragment)
                .addToBackStack(null)
                .commit()
        }

        buttonParejas?.setOnClickListener {
            val nuevaPreguntaFragment = NuevaPreguntaFragment()
            val bundle = Bundle()
            bundle.putString("tipoPregunta", "parejas")
            nuevaPreguntaFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nuevaPreguntaFragment)
                .addToBackStack(null)
                .commit()
        }

        buttonRepaso?.setOnClickListener {
            val formularioEORFragment = FormularioEORFragment()
            val bundle = Bundle()
            bundle.putString("tipoPregunta", "repaso")
            formularioEORFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, formularioEORFragment)
                .addToBackStack(null)
                .commit()
        }

        buttonPruebaFinal?.setOnClickListener {
            val formularioEORFragment = FormularioEORFragment()
            val bundle = Bundle()
            bundle.putString("tipoPregunta", "prueba final")
            formularioEORFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, formularioEORFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}