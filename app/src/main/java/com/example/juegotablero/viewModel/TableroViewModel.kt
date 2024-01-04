package com.example.juegotablero.viewModel

import PreguntasApi
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.juegotablero.R
import com.example.juegotablero.api.PreguntasCallback
import com.example.juegotablero.model.Casilla
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.model.Pregunta.Pareja
import com.example.juegotablero.model.Pregunta
import com.google.firebase.database.DatabaseError

class TableroViewModel : ViewModel() {

    //el tablero es un array de 20 casillas
    private val tablero = arrayOfNulls<Casilla>(32)
    var turno = 0
    private val preguntasApi = PreguntasApi()

    //inicializa el tablero
    init {
        inicializarTablero()

    }

    //inicializa el tablero con las casillas, para saber de que tipo es cada casilla

    private fun inicializarTablero() {
        // Llena el tablero con las casillas y preguntas correspondientes
        for (i in tablero.indices) {
            tablero[i] = when (i % 4) {
                0 -> Casilla("Parejas")
                1 -> Casilla("Test")
                2 -> Casilla("AdivinaPalabra")
                3 -> Casilla("Repaso")
                else -> null // Esto no debería ocurrir, pero es por si acaso
            }
        }
    }
    //funcion para tirar el dado de 6 caras
    fun tirarDado(): Int {
        return (1..6).random()
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


    fun cambiarTurno(){
        if (turno == 0){
            turno = 1
        }else{
            turno = 0
        }
    }

    fun obtenerPreguntaAleatoria(jugador: Jugador,  callback: PreguntasCallback) {
        val posicion = jugador.posicion-1
        val casilla = tablero[posicion % tablero.size]

        when (casilla?.tipo) {
            "Parejas" -> obtenerPreguntaAleatoriaJuegoParejas(callback)
            "Test" -> obtenerPreguntaAleatoriaTest(callback)
            "AdivinaPalabra" -> obtenerPreguntaAleatoriaAdivinaPalabra(callback)
            "Repaso" -> obtenerPreguntaAleatoriaRepaso(callback)
        }
    }
    fun obtenerPreguntaAleatoriaJuegoParejas(callback: PreguntasCallback) {
        preguntasApi.obtenerPreguntasJuegoParejas(object : PreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaTest(callback: PreguntasCallback) {
        preguntasApi.obtenerPreguntasTest(object : PreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaAdivinaPalabra(callback: PreguntasCallback) {
        preguntasApi.obtenerPreguntasAdivinaPalabra(object : PreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaRepaso(callback: PreguntasCallback) {
        preguntasApi.obtenerPreguntasRepaso(object : PreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerTipoCasilla(jugador: Jugador): String? {
        val casilla = tablero[(jugador.posicion-1) % tablero.size]
        return casilla?.tipo
    }
}

