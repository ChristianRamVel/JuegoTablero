package com.example.juegotablero.api

import com.example.juegotablero.model.Pregunta
import com.google.firebase.database.DatabaseError

interface PreguntasCallback {
    fun onPreguntasObtenidas(preguntas: List<Pregunta>)
    fun onError(error: DatabaseError)
}