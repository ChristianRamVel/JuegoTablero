package com.example.juegotablero.model

data class PreguntasContainer(
    val preguntas: List<Pregunta>
)

sealed class Pregunta {

    data class Repaso(
        @Transient val tipo: String = "Repaso",
        val enunciado: String = "",
        val opciones: List<String> = emptyList(),
        val respuesta_correcta: String = ""
    ) : Pregunta()

    data class AdivinaPalabra(
        @Transient val tipo: String = "AdivinaPalabra",
        val definicion: String = "",
        val palabra: String = ""
    ) : Pregunta()

    data class Test(
        @Transient val tipo: String = "Test",
        val enunciado: String = "",
        val opciones: List<String> = emptyList(),
        val respuestaCorrecta: String = ""
    ) : Pregunta()

    data class JuegoParejas(
        @Transient val tipo: String = "JuegoParejas",
        val parejas: List<Pareja> = emptyList()
    ) : Pregunta()

    data class PruebaFinal(
        @Transient val tipo: String = "Final",
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