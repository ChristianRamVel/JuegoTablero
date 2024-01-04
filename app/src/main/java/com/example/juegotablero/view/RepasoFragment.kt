package com.example.juegotablero.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.juegotablero.R
import com.example.juegotablero.model.Pregunta
import com.example.juegotablero.viewModel.RepasoViewModel

class RepasoFragment : Fragment() {

    private lateinit var viewModel: RepasoViewModel

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
            if (enunciado != null) {
                // se actualiza el enunciado de la pregunta en la vista
                actualizarEnunciado(enunciado)
            }
        }

    }


    fun actualizarEnunciado(enunciado: String){
        val textView = view?.findViewById<TextView>(R.id.tvRepasoPregunta)
        textView?.text = enunciado
    }

}