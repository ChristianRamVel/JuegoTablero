package com.example.juegotablero.añadirPreguntas.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.juegotablero.R

class NuevaPreguntaFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_formulario_eor, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val tipoPregunta = bundle?.getString("tipoPregunta")




        generarFormulario(tipoPregunta)

    }


    fun generarFormulario(tipoPregunta : String?){
        when(tipoPregunta){
            "adivinaPalabra" -> {
                // Generar formulario para adivinar palabra
            }
            "test" -> {
                // Generar formulario para test
            }
            "parejas" -> {
                // Generar formulario para parejas
            }
            "repaso" -> {
                // Generar formulario para repaso
            }
            "pruebaFinal" -> {
                // Generar formulario para prueba final
            }
        }
    }


}