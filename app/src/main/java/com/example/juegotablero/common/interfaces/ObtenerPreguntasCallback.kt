package com.example.juegotablero.common.interfaces

import com.example.juegotablero.api.model.Pregunta
import com.google.firebase.database.DatabaseError

interface ObtenerPreguntasCallback {
    fun onPreguntasObtenidas(preguntas: List<Pregunta>)
    fun onError(error: DatabaseError)
}