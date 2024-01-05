package com.example.juegotablero.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.example.juegotablero.R
import com.example.juegotablero.viewModel.AdivinarPalabraViewModel


class AdivinarPalabraFragment : Fragment() {

    private lateinit var viewModel: AdivinarPalabraViewModel
    private lateinit var palabraTextView: TextView
    private lateinit var intentosTextView: TextView
    private var palabraOculta = ""
    private var palabraAdivinarVista = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_adivinar_palabra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdivinarPalabraViewModel::class.java]
        palabraTextView = view.findViewById(R.id.tvPalabraAdivinar)
        intentosTextView = view.findViewById(R.id.tvIntentosAdivinarPalabra)



        val bundle = arguments

        if (bundle != null) {
            val definicion = bundle.getString("definicion")
            val palabra = bundle.getString("palabra")
            palabraOculta = palabra!!
            Log.d("AdivinarPalabraFragment", "Bundle JSON: $bundle")

            if (definicion != null) {
                actualizarEnunciado(definicion)
                viewModel.setPalabraAdivinar(palabra!!)
                actualizarPalabraAdivinar()
            }
        }

        palabraAdivinarVista = "_ ".repeat(palabraOculta.length)

        abrirTeclado()

        // Configurar un OnKeyListener para la vista de texto (palabraTextView)
        palabraTextView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.isPrintingKey) {
                val letraPulsada = event.displayLabel.toString()
                comprobarLetra(letraPulsada)
                return@setOnKeyListener true
            }
            false
        }

        // Hacer que la vista de texto sea enfocable y solicitar el enfoque para que funcione el OnKeyListener
        palabraTextView.isFocusableInTouchMode = true
        palabraTextView.requestFocus()

    }




    private fun actualizarEnunciado(enunciado: String) {
        val textView = view?.findViewById<TextView>(R.id.tvEnunciadoAdivinarPalabra)
        textView?.text = enunciado
    }

    private fun actualizarPalabraAdivinar() {
        val palabra = viewModel.getPalabraAdivinar()
        var palabraAdivinar = ""

        for (i in palabra.indices) {
            palabraAdivinar += "_ "
        }

        palabraTextView.text = palabraAdivinar
    }

    //metodo deprecated pero no he encontrado otra alternativa, tendra que coregirse en un futuro
    private fun abrirTeclado() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun cerrarTeclado() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun comprobarLetra(letra: String) {
        val palabra = viewModel.getPalabraAdivinar()

        // Convertimos la palabraAdivinarVista a un StringBuilder para poder modificarla
        val palabraModificada = StringBuilder(palabraAdivinarVista)

        var letraAcierto = false

        for (i in palabra.indices) {
            val letraActual = palabra[i].toString().toLowerCase()
            val letraPulsada = letra.toLowerCase()

            if (letraActual == letraPulsada) {
                // Sustituimos el guion bajo en la posición correspondiente con la letra correcta
                palabraModificada[i * 2] = letra[0]
                letraAcierto = true
            }
        }

        if (letraAcierto) {
            // Actualiza palabraAdivinarVista solo si la letra es correcta
            palabraAdivinarVista = palabraModificada.toString()
            palabraTextView.text = palabraAdivinarVista

            // Verifica si el jugador ha adivinado toda la palabra
            if (palabraAdivinarVista.indexOf('_') == -1) {
                Toast.makeText(requireContext(), "Has ganado", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si la letra no está en la palabra, decrementa los intentos
            viewModel.decrementarIntentos()
            // Llama a la función para comprobar si se agotaron los intentos
            comprobarIntentos()
        }
    }


    fun comprobarIntentos() {
        val intentos = viewModel.getIntentos()

        // Mostramos los intentos restantes en el tvIntento
        intentosTextView.text = intentos.toString()

        if (intentos == 0) {
            Toast.makeText(requireContext(), "Has agotado los intentos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cerrarTeclado()
    }
}
