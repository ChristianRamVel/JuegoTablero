package com.example.juegotablero.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.juegotablero.R
import com.example.juegotablero.common.interfaces.OnGameEventListener
import com.example.juegotablero.model.Pregunta

import com.example.juegotablero.viewModel.TestViewModel

class TestFragment(var preguntas : List<Pregunta>) : Fragment() {

    private lateinit var viewModel: TestViewModel
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private var indicePreguntaActual: Int = 0

    private var gameListener: OnGameEventListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        for (pregunta in preguntas){
            if (pregunta is Pregunta.Test){
                Log.d("TestFragment", "Pregunta: ${pregunta.enunciado}")
            }
        }

        actualizarPreguntaActual()
    }

    fun actualizarPreguntaActual() {
        if (indicePreguntaActual < preguntas.size) {
            val preguntaActual = preguntas[indicePreguntaActual]
            if (preguntaActual is Pregunta.Test) {
                // Logica para actualizar la vista con la pregunta actual
                // Puedes reutilizar tu lógica existente de actualización aquí

                val enunciado = preguntaActual.enunciado
                val opciones = preguntaActual.opciones
                val respuesta = preguntaActual.respuesta_correcta


                actualizarEnunciado(enunciado)
                actualizarOpciones(opciones, respuesta)

                // Incrementa el índice para la siguiente pregunta
                indicePreguntaActual++
            }
        } else {
            // Se han respondido todas las preguntas, puedes manejar el fin del juego aquí
            terminarPartida(true) // O false si prefieres
        }
    }

    fun actualizarEnunciado(enunciado: String){
        val textView = view?.findViewById<TextView>(R.id.tvTestEnunciado)
        textView?.text = enunciado
    }

    fun actualizarOpciones(opciones: List<String>?, respuestaCorrecta: String?){
        val button1 = view?.findViewById<Button>(R.id.btnRespuesta1)
        val button2 = view?.findViewById<Button>(R.id.btnRespuesta2)
        val button3 = view?.findViewById<Button>(R.id.btnRespuesta3)

        if (button1 != null) {
            button1.text = opciones?.get(0)
            button1.setOnClickListener { verificarRespuesta(button1.text.toString(), respuestaCorrecta) }

        }
        if (button2 != null) {
            button2.text = opciones?.get(1)
            button2.setOnClickListener { verificarRespuesta(button2.text.toString(), respuestaCorrecta) }

        }
        if (button3 != null) {
            button3.text = opciones?.get(2)
            button3.setOnClickListener { verificarRespuesta(button3.text.toString(), respuestaCorrecta) }
        }



    }

    fun verificarRespuesta(opcion: String, respuestaCorrecta: String?) {
        if (opcion == respuestaCorrecta) {
            viewModel.incrementarAciertos()
            if (viewModel.getAciertos() == 5){
                terminarPartida(true)
            }else {
                actualizarPreguntaActual()
            }
        } else {
            terminarPartida(false)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGameEventListener) {
            gameListener = context
        }
    }

    fun setGameListener(listener: OnGameEventListener) {
        gameListener = listener
    }

    fun terminarPartida(ganador: Boolean) {
            gameListener?.onGameResult(ganador)

            val fragmentManager = requireActivity().supportFragmentManager
            // Cierra el fragmento actual y vuelve al anterior en la pila
            fragmentManager.popBackStack()
    }

}