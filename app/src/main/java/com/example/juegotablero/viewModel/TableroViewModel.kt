package com.example.juegotablero.viewModel

import PreguntasApi
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.juegotablero.api.ObtenerPreguntasCallback
import com.example.juegotablero.model.Casilla
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.model.Pregunta
import com.google.firebase.database.DatabaseError

class TableroViewModel : ViewModel() {

    //el tablero es un array de 24 casillas
    private val tablero = arrayOfNulls<Casilla>(24)
    var turno = 0
    private val preguntasApi = PreguntasApi()

    //inicializa el tablero
    init {
        inicializarTablero()

    }

    //inicializa el tablero con las casillas, para saber de que tipo es cada casilla


    private fun inicializarTablero() {
        val tiposPreguntas = listOf("Parejas", "Test", "AdivinaPalabra", "Repaso")

        for (fila in 0 until 6) {
            for (columna in 0 until 4) {
                val indice = fila * 4 + columna
                val tipoPregunta = tiposPreguntas[(columna + fila) % 4]
                tablero[indice] = Casilla(tipoPregunta)
            }
        }
    }
    //funcion para tirar el dado de 6 caras
    fun tirarDado(): Int {
        return (1..6).random()
    }


    //Comprobar que el jugador ha completado las 4 preguntas basicas para pasar a la pregunta final
    fun paseAPreguntaFinal(jugador : Jugador): Boolean {

        return jugador.PAdivinaPalabra && jugador.PParejas && jugador.PRepaso && jugador.PTest
    }

    //funcion para comprobar si el jugador ha ganado

    fun haGanado(jugador : Jugador): Boolean {
        return jugador.PAdivinaPalabra && jugador.PParejas && jugador.PRepaso && jugador.PTest && jugador.PFinal
    }

    //funcion para actualizar la puntuacion del jugador

    fun sumarPunto(jugador : Jugador) {
        obtenerTipoCasilla(jugador)?.let {
            when (it) {
                "Parejas" -> jugador.PParejas = true
                "Test" -> jugador.PTest = true
                "AdivinaPalabra" -> jugador.PAdivinaPalabra = true
                "Repaso" -> jugador.PRepaso = true
            }
        }
    }

    fun guardarEstadistica( prefs : SharedPreferences, tipo : String){
        val editor = prefs.edit()
        editor.putInt(tipo, prefs.getInt(tipo, 0) + 1)
        editor.apply()
    }

    fun cambiarTurno(){
        if (turno == 0){
            turno = 1
        }else{
            turno = 0
        }
    }


    fun obtenerPreguntaAleatoria(jugador: Jugador,  callback: ObtenerPreguntasCallback) {
        val posicion = jugador.posicion-1
        val casilla = tablero[posicion % tablero.size]

        when (casilla?.tipo) {
            "Parejas" -> obtenerPreguntaAleatoriaJuegoParejas(callback)
            "Test" -> obtenerPreguntaAleatoriaTest(callback)
            "AdivinaPalabra" -> obtenerPreguntaAleatoriaAdivinaPalabra(callback)
            "Repaso" -> obtenerPreguntaAleatoriaRepaso(callback)
        }
    }
    fun obtenerPreguntaAleatoriaJuegoParejas(callback: ObtenerPreguntasCallback) {
        preguntasApi.obtenerPreguntasJuegoParejas(object : ObtenerPreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaTest(callback: ObtenerPreguntasCallback) {
        preguntasApi.obtenerPreguntasTest(object : ObtenerPreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {

                if (preguntas.size >= 5) {
                    // Obtener 5 preguntas aleatorias
                    val preguntasAleatorias = preguntas.shuffled().take(5)
                    callback.onPreguntasObtenidas(preguntasAleatorias)
                } else {
                    // Manejar el caso donde no hay suficientes preguntas
                    callback.onPreguntasObtenidas(emptyList())
                }
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaAdivinaPalabra(callback: ObtenerPreguntasCallback) {
        preguntasApi.obtenerPreguntasAdivinaPalabra(object : ObtenerPreguntasCallback {
            override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                val pregunta = preguntas.randomOrNull()
                callback.onPreguntasObtenidas(listOfNotNull(pregunta))
            }

            override fun onError(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun obtenerPreguntaAleatoriaRepaso(callback: ObtenerPreguntasCallback) {
        preguntasApi.obtenerPreguntasRepaso(object : ObtenerPreguntasCallback {
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

