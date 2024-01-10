package com.example.juegotablero.viewModel

import PreguntasApi
import androidx.lifecycle.ViewModel
import com.example.juegotablero.api.CrearPreguntasCallback
import com.example.juegotablero.model.Pregunta

class FormularioAPViewModel : ViewModel() {


    fun enviarPregunta(definicion : String, palabra : String, crearPreguntasCallback: CrearPreguntasCallback){
        val preguntasApi = PreguntasApi()
        val categoria = "adivina_palabra"
        val pregunta = Pregunta.AdivinaPalabra("adivinaPalabra", definicion, palabra)

        preguntasApi.agregarPregunta(categoria, pregunta, crearPreguntasCallback)

    }

}