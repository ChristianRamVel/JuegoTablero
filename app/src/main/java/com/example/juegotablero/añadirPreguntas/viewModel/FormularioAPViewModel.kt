package com.example.juegotablero.añadirPreguntas.viewModel

import PreguntasApi
import androidx.lifecycle.ViewModel
import com.example.juegotablero.common.interfaces.CrearPreguntasCallback
import com.example.juegotablero.api.model.Pregunta

class FormularioAPViewModel : ViewModel() {


    fun enviarPregunta(definicion : String, palabra : String, crearPreguntasCallback: CrearPreguntasCallback){
        val preguntasApi = PreguntasApi()
        val categoria = "adivina_palabra"
        val pregunta = Pregunta.AdivinaPalabra("adivinaPalabra", definicion, palabra)

        preguntasApi.agregarPregunta(categoria, pregunta, crearPreguntasCallback)

    }

}