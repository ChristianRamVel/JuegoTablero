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

import com.example.juegotablero.viewModel.TestViewModel

class TestFragment : Fragment() {

    private lateinit var viewModel: TestViewModel
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
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

        // se obtienen los datos que se han enviado desde el fragmento anterior
        val bundle = arguments

        // se comprueba que el bundle no sea nulo
        if (bundle != null) {
            // se obtiene el enunciado de la pregunta
            val enunciado = bundle.getString("definicion")
            val opciones = bundle.getStringArray("opciones")
            val respuesta = bundle.getString("respuesta")

            Log.d("TestFragment", "Bundle JSON: $bundle")

            Log.d("TestFragment", "Enunciado: $enunciado, Opciones: ${opciones?.contentToString()}, Respuesta Correcta: $respuesta")

                // se actualiza el enunciado de la pregunta en la vista
            if (enunciado != null) {
                //rellenar el textview con el enunciado de la pregunta y las opciones de respuesta en los botones
                actualizarEnunciado(enunciado)
                actualizarOpciones(opciones)


                button1 = view.findViewById(R.id.btnRespuesta1)
                button2 = view.findViewById(R.id.btnRespuesta2)
                button3 = view.findViewById(R.id.btnRespuesta3)



                Log.d("puntos", "puntos: ${viewModel.getAciertos()}")
            }

        }

    }

    fun actualizarEnunciado(enunciado: String){
        val textView = view?.findViewById<TextView>(R.id.tvTestEnunciado)
        textView?.text = enunciado
    }

    fun actualizarOpciones(opciones: Array<String>?){
        val button1 = view?.findViewById<Button>(R.id.btnRespuesta1)
        val button2 = view?.findViewById<Button>(R.id.btnRespuesta2)
        val button3 = view?.findViewById<Button>(R.id.btnRespuesta3)

        if (button1 != null) {
            button1.text = opciones?.get(0)
        }
        if (button2 != null) {
            button2.text = opciones?.get(1)
        }
        if (button3 != null) {
            button3.text = opciones?.get(2)
        }
    }

    fun verificarRespuesta(opcion: String, respuestaCorrecta: String?): Boolean{

        if (opcion == respuestaCorrecta){
            viewModel.incrementarAciertos()
            return true
        }
        return false
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
        view?.post {
            gameListener?.onGameResult(ganador)

            val fragmentManager = requireActivity().supportFragmentManager
            // Cierra el fragmento actual y vuelve al anterior en la pila
            fragmentManager.popBackStack()
        }
    }

}