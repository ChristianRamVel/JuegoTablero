package com.example.juegotablero.api

import java.lang.Exception

interface CrearPreguntasCallback {
    fun onPreguntaCreada()
    fun onError(exception: Exception?)
}