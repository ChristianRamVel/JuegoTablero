package com.example.juegotablero.api

import com.example.juegotablero.model.Pregunta
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseError

interface ObtenerPreguntasCallback {
    fun onPreguntasObtenidas(preguntas: List<Pregunta>)
    fun onError(error: DatabaseError)
}