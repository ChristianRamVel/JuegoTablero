package com.example.juegotablero.viewModel

import PreguntasApi
import androidx.lifecycle.ViewModel
import com.example.juegotablero.api.CrearPreguntasCallback
import com.example.juegotablero.model.Pregunta

class FormularioEORViewMode : ViewModel() {


    fun enviarPregunta(enunciado : String, opciones : List<String>, respuestaCorrecta : String, tipoPregunta : String?, crearPreguntasCallback: CrearPreguntasCallback){
        val preguntasApi = PreguntasApi()
        val categoria : String
        val pregunta : Pregunta

        when(tipoPregunta){
            "test" -> {
                categoria = "test"
                pregunta = Pregunta.Test("Test", enunciado, opciones, respuestaCorrecta)
            }
            "repaso" -> {
                categoria = "repaso"
                pregunta = Pregunta.Repaso("Repaso", enunciado, opciones, respuestaCorrecta)
            }
            "pruebaFinal" -> {
                categoria = "prueba_final"
                pregunta = Pregunta.PruebaFinal("PruebaFinal", enunciado, opciones, respuestaCorrecta)
            }
            else -> {
                categoria = ""
                pregunta = Pregunta.Repaso("ninguno", enunciado, opciones, respuestaCorrecta)
            }
        }
        if (categoria != ""){
            preguntasApi.agregarPregunta(categoria, pregunta, crearPreguntasCallback)
        }




    }



}