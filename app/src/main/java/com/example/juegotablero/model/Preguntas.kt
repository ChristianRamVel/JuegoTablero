package com.example.juegotablero.model

data class PreguntasContainer(
    val preguntas: List<Pregunta>
)
//sea class sirve para definir una clase que puede tener varios tipos de datos dentro de ella
//y podremosevaluar con un when que tipo de dato es y hacer algo con el.
sealed class Pregunta {

    data class Repaso(
        val enunciado: String,
        val opciones: List<String>,
        val respuestaCorrecta: String
    ) : Pregunta()

    data class AdivinaPalabra(
        val definicion: String,
        val palabra: String
    ) : Pregunta()

    data class Test(
        val enunciado: String,
        val opciones: List<String>,
        val respuestaCorrecta: String
    ) : Pregunta()

    data class JuegoParejas(
        val parejas: List<Pareja>
    ) : Pregunta()

    data class PruebaFinal(
        val enunciado: String,
        val opciones: List<String>,
        val respuestaCorrecta: String
    ) : Pregunta()

    fun evaluarPregunta(pregunta: Pregunta) {
        when (pregunta) {
            is Pregunta.Repaso -> {

            }
            is Pregunta.AdivinaPalabra -> {

            }
            is Pregunta.Test -> {

            }
            is Pregunta.JuegoParejas -> {

            }
            is Pregunta.PruebaFinal -> {

            }
        }
    }
}

data class Pareja(
    val opcion1: String,
    val opcion2: String
)
