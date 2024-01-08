package com.example.juegotablero.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.juegotablero.R
import com.example.juegotablero.common.interfaces.OnGameEventListener
import com.example.juegotablero.model.Pregunta
import com.example.juegotablero.viewModel.RepasoViewModel

class RepasoFragment : Fragment() {

    private lateinit var viewModel: RepasoViewModel
    private var gameListener: OnGameEventListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repaso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // se obtienen los datos que se han enviado desde el fragmento anterior
        val bundle = arguments

        // se comprueba que el bundle no sea nulo
        if (bundle != null) {
            // se obtiene el enunciado de la pregunta
            val enunciado = bundle.getString("enunciado")
            val opciones = bundle.getStringArray("opciones")
            val respuesta = bundle.getString("respuesta")
            Log.d("RepasoFragment", "Bundle JSON: $bundle")
            if (enunciado != null) {
                // se actualiza el enunciado de la pregunta en la vista
                actualizarEnunciado(enunciado)

                // se verifica que el array de opciones no sea nulo
                if (opciones != null) {
                    generarBotones(opciones)
                }
            }
        }
    }


    fun actualizarEnunciado(enunciado: String){
        val textView = view?.findViewById<TextView>(R.id.tvRepasoPregunta)
        textView?.text = enunciado
    }

    fun generarBotones(opciones: Array<String>){
        val gridLayout = view?.findViewById<GridLayout>(R.id.gridRespuestasRepaso)
        for (opcion in opciones){
            val button = Button(context)
            button.text = opcion
            button.setOnClickListener {
                onButtonClicked(button)
            }
            gridLayout?.addView(button)
        }
    }

    fun onButtonClicked(button: Button){
    //si la respuesta es correcta se llama al metodo terminarPartida
        val bundle = arguments
        val respuestaCorrecta = bundle?.getString("respuesta")

        if (button.text == respuestaCorrecta){
            terminarPartida(true)
        }else{
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
        view?.post {
            gameListener?.onGameResult(ganador)

            val fragmentManager = requireActivity().supportFragmentManager
            // Cierra el fragmento actual y vuelve al anterior en la pila
            fragmentManager.popBackStack()
        }
    }

}