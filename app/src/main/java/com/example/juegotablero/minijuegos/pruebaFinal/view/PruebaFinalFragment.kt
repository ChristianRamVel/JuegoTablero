package com.example.juegotablero.minijuegos.pruebaFinal.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.juegotablero.R
import com.example.juegotablero.common.interfaces.OnGameEventListener
import com.example.juegotablero.minijuegos.pruebaFinal.viewModel.PruebaFinalViewModel

class PruebaFinalFragment : Fragment() {

    private lateinit var viewModel: PruebaFinalViewModel
    private var gameListener: OnGameEventListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prueba_final, container, false)
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

            if (enunciado != null) {
                // se actualiza el enunciado de la pregunta en la vista
                actualizarEnunciado(enunciado)

                // se verifica que el array de opciones no sea nulo
                actualizarOpciones(opciones?.toList())
            }
        }
    }

    fun actualizarEnunciado(enunciado: String){
        val textView = view?.findViewById<TextView>(R.id.tvPreguntaFinal)
        textView?.text = enunciado
    }

    fun actualizarOpciones(opciones: List<String>?){
        val button1 = view?.findViewById<Button>(R.id.btnRespuesta1)
        val button2 = view?.findViewById<Button>(R.id.btnRespuesta2)
        val button3 = view?.findViewById<Button>(R.id.btnRespuesta3)

        if (button1 != null) {
            button1.text = opciones?.get(0)
            button1.setOnClickListener { onButtonClicked(button1) }

        }
        if (button2 != null) {
            button2.text = opciones?.get(1)
            button2.setOnClickListener { onButtonClicked(button2) }

        }
        if (button3 != null) {
            button3.text = opciones?.get(2)
            button3.setOnClickListener { onButtonClicked(button3) }
        }
    }

    fun onButtonClicked(button: Button){
        //si la respuesta es correcta se llama al metodo terminarPartida
        val bundle = arguments
        val respuestaCorrecta = bundle?.getString("respuesta")

        if (button.text == respuestaCorrecta){
            marcarOpcionConColor(button, true)
            button.postDelayed({
                terminarPartida(true)
            }, 1000)
        }else{
            marcarOpcionConColor(button, false)
            button.postDelayed({
                terminarPartida(false)
            }, 1000)
        }
    }

    fun marcarOpcionConColor(button: Button, correcto: Boolean){
        desactivarBotones()
        if (correcto){
            button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.correcto);
        }else{
            button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.incorrecto);
        }

    }

    fun desactivarBotones(){
        val button1 = view?.findViewById<Button>(R.id.btnRespuesta1)
        val button2 = view?.findViewById<Button>(R.id.btnRespuesta2)
        val button3 = view?.findViewById<Button>(R.id.btnRespuesta3)

        button1?.isClickable = false
        button2?.isClickable = false
        button3?.isClickable = false
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