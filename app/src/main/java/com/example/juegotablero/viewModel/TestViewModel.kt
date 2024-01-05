package com.example.juegotablero.viewModel

import androidx.lifecycle.ViewModel
import com.example.juegotablero.model.Pregunta

class TestViewModel : ViewModel() {

    private val preguntas = mutableListOf<Pregunta.Test>()
    private var preguntaActual: Pregunta.Test? = null
    private var aciertos = 0

    //funcion para añadir preguntas al array de preguntas
    fun addPregunta(pregunta: Pregunta.Test){
        preguntas.add(pregunta)
    }

    //funcion para incrementar el numero de aciertos

    fun incrementarAciertos(){
        aciertos++
    }

    //funcion para obtener el numero de aciertos

    fun getAciertos(): Int{
        return aciertos
    }








}