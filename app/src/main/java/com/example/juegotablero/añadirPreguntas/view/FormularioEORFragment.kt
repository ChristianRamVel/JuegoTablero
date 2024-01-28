package com.example.juegotablero.añadirPreguntas.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.juegotablero.R
import com.example.juegotablero.common.interfaces.CrearPreguntasCallback
import com.example.juegotablero.añadirPreguntas.viewModel.FormularioEORViewMode
import java.lang.Exception

class FormularioEORFragment : Fragment() {

    private lateinit var viewModel: FormularioEORViewMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[FormularioEORViewMode::class.java]

        return inflater.inflate(R.layout.fragment_formulario_eor, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val tipoPregunta = bundle?.getString("tipoPregunta")

        val tvTipoPregunta = view.findViewById<TextView>(R.id.tvTipoPregunta)

        tvTipoPregunta.text = getString(R.string.nuevaPregunta, tipoPregunta)

        val botonCrear = view.findViewById<View>(R.id.btnCrear)

        val crearPreguntasCallback = object : CrearPreguntasCallback {
            override fun onPreguntaCreada() {
               showAlertDialog("Pregunta creada correctamente", "success")
            }

            override fun onError(exception: Exception?) {
                showAlertDialog("Ha habido un error", "error")
            }

        }
        botonCrear.setOnClickListener {
            enviarPregunta(tipoPregunta, crearPreguntasCallback)
        }
    }


    fun enviarPregunta(tipoPregunta: String?, obtenerPreguntasCallback: CrearPreguntasCallback){

        val enunciado = view?.findViewById<TextView>(R.id.etDefinicion)?.text.toString()
        val opcion1 = view?.findViewById<TextView>(R.id.etPalabra)?.text.toString()
        val opcion2 = view?.findViewById<TextView>(R.id.etOpcion2)?.text.toString()
        val opcion3 = view?.findViewById<TextView>(R.id.etOpcion3)?.text.toString()
        val opciones = listOf(opcion1, opcion2, opcion3)
        val respuestaCorrecta : String

        val radioGroupOptions = view?.findViewById<RadioGroup>(R.id.radioGroupOptions)
        val selectedId = radioGroupOptions?.checkedRadioButtonId
        val radioButton = selectedId?.let { view?.findViewById<RadioButton>(it) }

        val texto = radioButton?.text.toString()

        respuestaCorrecta = when(texto){
            "Opción 1" -> {
                opcion1
            }

            "Opción 2" -> {
                opcion2
            }

            "Opción 3" -> {
                opcion3
            }else -> {
                ""
            }
        }

        viewModel.enviarPregunta(enunciado, opciones, respuestaCorrecta, tipoPregunta!!, obtenerPreguntasCallback)
    }

    fun showAlertDialog(message: String, type: String){
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(message)
        builder.setTitle(if (type == "success") "Éxito" else "Error")

        builder.setPositiveButton("OK") { _, _ ->
            if (type == "success"){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MenuNuevaPreguntaFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        val dialog = builder.create()
        dialog.show()

    }

}