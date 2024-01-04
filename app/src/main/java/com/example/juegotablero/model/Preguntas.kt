package com.example.juegotablero.model

import android.os.Parcel
import android.os.Parcelable

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
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(opcion1)
            parcel.writeString(opcion2)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Pareja> {
            override fun createFromParcel(parcel: Parcel): Pareja {
                return Pareja(parcel)
            }

            override fun newArray(size: Int): Array<Pareja?> {
                return arrayOfNulls(size)
            }
        }
    }
}