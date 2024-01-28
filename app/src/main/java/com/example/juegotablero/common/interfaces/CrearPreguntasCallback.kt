package com.example.juegotablero.common.interfaces

import java.lang.Exception

interface CrearPreguntasCallback {
    fun onPreguntaCreada()
    fun onError(exception: Exception?)
}