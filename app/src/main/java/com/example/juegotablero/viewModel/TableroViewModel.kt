package com.example.juegotablero.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.juegotablero.model.Casilla
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.model.Pregunta.Pareja
import com.example.juegotablero.model.Pregunta

class TableroViewModel : ViewModel() {

    //el tablero es un array de 20 casillas
    val tablero = arrayOfNulls<Casilla>(20)

    //inicializa el tablero
    init {
        inicializarTablero()
    }

    //inicializa el tablero con las casillas, para saber de que tipo es cada casilla

    private fun inicializarTablero() {
        tablero[0] = Casilla(
            Pregunta.JuegoParejas("JuegoParejas",emptyList()))
        tablero[1] = Casilla(Pregunta.Test("Test","", emptyList(),""))
        tablero[2] = Casilla(Pregunta.AdivinaPalabra("AdivinaPalabra","",""))
        tablero[3] = Casilla(Pregunta.Repaso("Repaso", emptyList(),""))

        tablero[4] = Casilla(
            Pregunta.JuegoParejas("JuegoParejas",emptyList()))
        tablero[5] = Casilla(Pregunta.Test("Test","", emptyList(),""))
        tablero[6] = Casilla(Pregunta.AdivinaPalabra("AdivinaPalabra","",""))
        tablero[7] = Casilla(Pregunta.Repaso("Repaso", emptyList(),""))

        tablero[8] = Casilla(
            Pregunta.JuegoParejas("JuegoParejas",emptyList()))
        tablero[9] = Casilla(Pregunta.Test("Test","", emptyList(),""))
        tablero[10] = Casilla(Pregunta.AdivinaPalabra("AdivinaPalabra","",""))
        tablero[11] = Casilla(Pregunta.Repaso("Repaso", emptyList(),""))

        tablero[12] = Casilla(
            Pregunta.JuegoParejas("JuegoParejas",emptyList()))
        tablero[13] = Casilla(Pregunta.Test("Test","", emptyList(),""))
        tablero[14] = Casilla(Pregunta.AdivinaPalabra("AdivinaPalabra","",""))
        tablero[15] = Casilla(Pregunta.Repaso("Repaso", emptyList(),""))

        tablero[16] = Casilla(
            Pregunta.JuegoParejas("JuegoParejas",emptyList()))
        tablero[17] = Casilla(Pregunta.Test("Test","", emptyList(),""))
        tablero[18] = Casilla(Pregunta.AdivinaPalabra("AdivinaPalabra","",""))
        tablero[19] = Casilla(Pregunta.Repaso("Repaso", emptyList(),""))


    }

    //funcion para tirar el dado de 6 caras
    fun tirarDado(): Int {
        return (1..6).random()
    }

    //funcion para avanzar en el tablero
    fun avanzar(posicion: Int, dado: Int): Int {
        var posicionFinal = posicion + dado
        if (posicionFinal > 19) {
            posicionFinal = posicionFinal - 20
        }
        return posicionFinal
    }

    //Comprobar que el jugador ha completado las 4 preguntas basicas para pasar a la pregunta final

    fun paseAPreguntaFinal(jugador : Jugador): Boolean {
        val puntuacionJugador = jugador.puntuacion
        return puntuacionJugador == 4
    }

    //funcion para comprobar si el jugador ha ganado

    fun haGanado(jugador : Jugador): Boolean {
        val puntuacionJugador = jugador.puntuacion
        return puntuacionJugador == 5
    }

    //funcion para actualizar la puntuacion del jugador

    fun sumarPunto(jugador : Jugador) {
        jugador.puntuacion ++
    }

    //funcion para comprobar si la respuesta del jugador es correcta

    fun comprobarRespuestaDeStrings(respuestaJugador: String, respuestaCorrecta: String): Boolean {
        return respuestaJugador == respuestaCorrecta
    }


}


