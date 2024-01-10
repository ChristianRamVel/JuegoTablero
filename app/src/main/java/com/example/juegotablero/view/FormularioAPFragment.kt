package com.example.juegotablero.view

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
import com.example.juegotablero.api.CrearPreguntasCallback
import com.example.juegotablero.viewModel.FormularioAPViewModel
import com.example.juegotablero.viewModel.FormularioEORViewMode
import java.lang.Exception

class FormularioAPFragment : Fragment() {

    private lateinit var viewModel: FormularioAPViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[FormularioAPViewModel::class.java]

        return inflater.inflate(R.layout.fragment_formulario_ap, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tvTipoPregunta = view.findViewById<TextView>(R.id.tvTipoPregunta)

        tvTipoPregunta.text = getString(R.string.nuevaPregunta, "adivina palabra")

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
            enviarPregunta(crearPreguntasCallback)
        }
    }

    fun enviarPregunta(crearPreguntasCallback: CrearPreguntasCallback){

        val definicion = view?.findViewById<TextView>(R.id.etDefinicion)?.text.toString()
        val palabra = view?.findViewById<TextView>(R.id.etPalabra)?.text.toString()


        viewModel.enviarPregunta(definicion, palabra, crearPreguntasCallback)
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