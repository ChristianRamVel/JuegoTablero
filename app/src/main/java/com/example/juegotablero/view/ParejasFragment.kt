package com.example.juegotablero.view

import ParejasViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.juegotablero.R
import com.example.juegotablero.model.Pregunta

class ParejasFragment : Fragment() {

    private lateinit var viewModel: ParejasViewModel
    private lateinit var gridLayout1: GridLayout
    private lateinit var gridLayout2: GridLayout
    private var primerParSeleccionado: Button? = null
    private var segundoParSeleccionado: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_parejas, container, false)
        gridLayout1 = view.findViewById(R.id.grid1)
        gridLayout2 = view.findViewById<GridLayout>(R.id.grid2)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[ParejasViewModel::class.java]

        val bundle = arguments

        if (bundle != null) {
            val parejasArray = bundle.getParcelableArrayList<Pregunta.Pareja>("parejas")
            if (parejasArray != null) {
                for (pareja in parejasArray) {
                    viewModel.addWordPair(pareja.opcion1, pareja.opcion2)

                }
            }
        }

        // Crear y agregar botones dinámicamente al GridLayout
        createButtons()

        return view
    }

    private fun createButtons() {
        // Agregar parejas de prueba

        // Obtener la lista de animales y tipos
        val primerosPares = viewModel.getPair1().shuffled()
        val segundosPares = viewModel.getPair2().shuffled()



        // Crear y agregar botones dinámicamente al GridLayout
        for (primerPar in primerosPares) {
            val button = Button(requireContext())
            button.text = primerPar
            button.maxWidth = 500
            button.setOnClickListener {
                onButtonClicked(button)
            }
            gridLayout1.addView(button)
        }

        for (segundoPar in segundosPares) {
            val button = Button(requireContext())
            button.text = segundoPar
            button.maxWidth = 500
            button.setOnClickListener {
                onButtonClicked(button)
            }
            gridLayout2.addView(button)
        }
    }

    private fun onButtonClicked(button: Button) {
        if (primerParSeleccionado == null) {
            // Si no hay un animal seleccionado, guarda el botón del animal seleccionado
            primerParSeleccionado = button
            // Marca visualmente el botón seleccionado (puedes ajustar según tus necesidades)
            primerParSeleccionado?.isSelected = true
        } else {
            // Si ya hay un animal seleccionado, guarda el botón del tipo seleccionado
            segundoParSeleccionado = button
            // Marca visualmente el botón seleccionado (puedes ajustar según tus necesidades)
            segundoParSeleccionado?.isSelected = true

            // Verifica si la combinación es correcta
            val animal = primerParSeleccionado?.text.toString()
            val tipo = segundoParSeleccionado?.text.toString()

            if (viewModel.isMatch(animal, tipo)) {
                // La combinación es correcta, puedes manejarlo según tus necesidades
                Toast.makeText(requireContext(), "¡Correcto!", Toast.LENGTH_SHORT).show()

                // Oculta o deshabilita los botones correspondientes
                ocultarBotones(aciertos = setOf(animal to tipo))

                if (todasLasParejasAcertadas()) {
                    Toast.makeText(requireContext(), "¡Has ganado!", Toast.LENGTH_SHORT).show()
                }
                resetSelections()
            } else {
                // La combinación es incorrecta, puedes manejarlo según tus necesidades
                Toast.makeText(requireContext(), "Incorrecto, has perdido", Toast.LENGTH_SHORT).show()
                // Desmarca visualmente los botones seleccionados
                resetSelections()
            }
        }
    }

    private fun ocultarBotones(aciertos: Set<Pair<String, String>>) {
        // Recorre todos los botones del GridLayout1
        for (i in 0 until gridLayout1.childCount) {
            val button = gridLayout1.getChildAt(i) as Button
            val par1 = button.text.toString()
            val par2 = viewModel.getMatchForWord1(par1)
            val pareja = par1 to par2
            val isMatched = aciertos.contains(pareja)
            if (isMatched) {
                // Oculta el botón
                button.visibility = View.INVISIBLE
            }
        }

        // Recorre todos los botones del GridLayout2
        for (i in 0 until gridLayout2.childCount) {
            val button = gridLayout2.getChildAt(i) as Button
            val par2 = button.text.toString()
            val par1 = viewModel.getMatchForWord2(par2)
            val pareja = par1 to par2
            val isMatched = aciertos.contains(pareja)
            if (isMatched) {
                // Oculta el botón
                button.visibility = View.INVISIBLE
            }
        }
    }


    private fun resetSelections() {
        primerParSeleccionado?.isSelected = false
        segundoParSeleccionado?.isSelected = false
        primerParSeleccionado = null
        segundoParSeleccionado = null
    }

    private fun resetSelectionsVisual() {
        // Desmarca visualmente los botones seleccionados
        primerParSeleccionado?.isSelected = false
        segundoParSeleccionado?.isSelected = false
        // Reinicia las selecciones
        resetSelections()
    }


    //funcion para saber si ya se han acertado todas las parejas
    fun todasLasParejasAcertadas(): Boolean {
        return viewModel.getAciertos() == viewModel.getWordPairs().size
    }


}