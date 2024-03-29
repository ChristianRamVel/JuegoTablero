package com.example.juegotablero.tablero.viewModel

import PreguntasApi
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.juegotablero.common.interfaces.ObtenerPreguntasCallback
import com.example.juegotablero.tablero.model.Casilla
import com.example.juegotablero.api.db.DatabaseHelper
import com.example.juegotablero.tablero.model.Jugador
import com.example.juegotablero.tablero.model.Partida
import com.example.juegotablero.api.model.Pregunta
import com.google.firebase.database.DatabaseError

class TableroViewModel(context: Context) : ViewModel() {

    //el tablero es un array de 24 casillas
    private val tablero = arrayOfNulls<Casilla>(24)

    var turno = 0

    lateinit var jugador1 : Jugador
    lateinit var jugador2 : Jugador
    lateinit var partida : Partida

    private val preguntasApi = PreguntasApi()

    private val dbHelper : DatabaseHelper = DatabaseHelper(context)

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

    fun sumarPuntoFinal(jugador : Jugador) {
        jugador.PFinal = true
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


    fun obtenerPreguntaAleatoria(jugador: Jugador, callback: ObtenerPreguntasCallback) {
        val posicion = jugador.posicion
        val casilla = tablero[posicion % tablero.size]

        if (paseAPreguntaFinal(jugador)){
            obtenerPreguntaAleatoriaPruebaFinal(callback)
        }else {

            when (casilla?.tipo) {
                "Parejas" -> obtenerPreguntaAleatoriaJuegoParejas(callback)
                "Test" -> obtenerPreguntaAleatoriaTest(callback)
                "AdivinaPalabra" -> obtenerPreguntaAleatoriaAdivinaPalabra(callback)
                "Repaso" -> obtenerPreguntaAleatoriaRepaso(callback)
            }
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

    fun obtenerPreguntaAleatoriaPruebaFinal(callback: ObtenerPreguntasCallback) {
        preguntasApi.obtenerPreguntasPruebaFinal(object : ObtenerPreguntasCallback {
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
        val casilla = tablero[(jugador.posicion) % tablero.size]
        return casilla?.tipo
    }

    fun crearJugadores(nombre1 : String, nombre2 : String){
        val id = dbHelper.obtenerIDUltimoJugador()
        jugador1 = Jugador(id + 1, nombre1, false, false, false, false, false, -1)
        jugador2 = Jugador(id + 2, nombre2, false, false, false, false, false, -1)

        dbHelper.crearJugadores(jugador1, jugador2)
    }

    fun crearPartida(){
        val id =  dbHelper.crearPartida(jugador1.id, jugador2.id, turno, obtenerFechaYHoraActual())
        partida = Partida(id.toInt(), jugador1.id, jugador2.id, turno, obtenerFechaYHoraActual())
    }

    fun cargarPartida(id : Int){
        partida = dbHelper.cargarPartida(id)
        turno = partida.turno
    }

    fun cargarJugadores(){
        jugador1 = dbHelper.cargarJugador(partida.jugador1ID)
        jugador2  = dbHelper.cargarJugador(partida.jugador2ID)
    }

    fun guardarPartida(){
        partida.turno = turno
        partida.fecha = obtenerFechaYHoraActual()
        dbHelper.guardarPartida(partida)
    }

    fun guardarJugadores(){
        dbHelper.guardarJugadores(jugador1, jugador2)
    }




    // obtener la fecha actual del sistema
    fun obtenerFechaYHoraActual(): String {
        val c = java.util.Calendar.getInstance()
        val dia = c.get(java.util.Calendar.DAY_OF_MONTH)
        val mes = c.get(java.util.Calendar.MONTH) + 1
        val anio = c.get(java.util.Calendar.YEAR)
        val hora = c.get(java.util.Calendar.HOUR_OF_DAY)
        val minuto = c.get(java.util.Calendar.MINUTE)
        val segundo = c.get(java.util.Calendar.SECOND)

        val diaStr = if (dia < 10) "0$dia" else dia.toString()
        val mesStr = if (mes < 10) "0$mes" else mes.toString()
        val horaStr = if (hora < 10) "0$hora" else hora.toString()
        val minutoStr = if (minuto < 10) "0$minuto" else minuto.toString()
        val segundoStr = if (segundo < 10) "0$segundo" else segundo.toString()

        val fechaHoraFormateada = String.format("%s/%s/%s %s:%s:%s", diaStr, mesStr, anio, horaStr, minutoStr, segundoStr)

        Log.d("fecha", fechaHoraFormateada)
        return fechaHoraFormateada

    }














}

