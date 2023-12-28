package com.example.juegotablero.model

data class PreguntasContainer(
    val preguntas: List<Pregunta>
)

sealed class Pregunta {
    data class Repaso(
        val enunciado: String = "",
        val opciones: List<String> = emptyList(),
        val respuesta_correcta: String = ""
    ) : Pregunta()

    data class AdivinaPalabra(
        val tipo: String = "",
        val definicion: String = "",
        val palabra: String = ""
    ) : Pregunta()

    data class Test(
        val tipo: String = "",
        val enunciado: String = "",
        val opciones: List<String> = emptyList(),
        val respuestaCorrecta: String = ""
    ) : Pregunta()

    data class JuegoParejas(
        val tipo: String = "",
        val parejas: List<Pareja> = emptyList()
    ) : Pregunta()

    data class PruebaFinal(
        val tipo: String = "",
        val enunciado: String = "",
        val opciones: List<String>? = emptyList(),
        val respuestaCorrecta: String? = "",
        val respuestaCorrectaPalabrasClave: String? = ""
    ) : Pregunta()

    data class Pareja(
        val opcion1: String = "",
        val opcion2: String = ""
    )
}