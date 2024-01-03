package com.example.juegotablero.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.juegotablero.R
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.viewModel.TableroViewModel

class TableroFragment : Fragment() {

    private lateinit var jugador1: Jugador
    private lateinit var jugador2: Jugador
    private lateinit var viewModel: TableroViewModel
    private lateinit var gridLayout: GridLayout
    private lateinit var tvInfoPartida: TextView
    private lateinit var btnTirarDado: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tablero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jugador1 = Jugador("Jugador 1", 0, 0)
        jugador2 = Jugador("Jugador 2", 0, 0)

        viewModel = ViewModelProvider(this)[TableroViewModel::class.java]

        gridLayout = view.findViewById(R.id.gridLayoutTablero)
        tvInfoPartida = view.findViewById(R.id.tvInfoPartida)
        btnTirarDado = view.findViewById(R.id.btnTirarDado)

        setupTablero()

        btnTirarDado.setOnClickListener {
            if (viewModel.turno == 0) {
                avanzar(jugador1.posicion, viewModel.tirarDado(), jugador1)
            } else {
                avanzar(jugador2.posicion, viewModel.tirarDado(), jugador2)
            }
            actualizarTurno()
        }
    }

    private fun setupTablero() {

        val colorRojo = ContextCompat.getColor(requireContext(), R.color.casillaAdivinaPalabra)
        val colorAzul = ContextCompat.getColor(requireContext(), R.color.casillaJuegoparejas)
        val colorAmarillo = ContextCompat.getColor(requireContext(), R.color.casillaRepaso)
        val colorVerde = ContextCompat.getColor(requireContext(), R.color.casillaTest)

        val colors = listOf(
            colorRojo,
            colorAzul,
            colorAmarillo,
            colorVerde
        )

        val numFilas = 6
        val numColumnas = 4

        for (fila in 0 until numFilas) {
            for (columna in 0 until numColumnas) {
                val button = Button(requireContext())
                val layoutParams = GridLayout.LayoutParams()

                layoutParams.rowSpec = GridLayout.spec(fila)
                layoutParams.columnSpec = GridLayout.spec(columna)
                button.layoutParams = layoutParams
                button.setBackgroundColor(colors[(fila + columna) % colors.size])
                button.text = ""
                gridLayout.addView(button)
            }
        }

        // Otras configuraciones y actualizaciones según tus necesidades
    }

    private fun actualizarTurno() {
        viewModel.cambiarTurno()
        tvInfoPartida.text = if (viewModel.turno == 0) {
            getString(R.string.turno_jugador1)
        } else {
            getString(R.string.turno_jugador2)
        }
    }

    private fun actualizarPuntuacion() {
        val tvPuntuacionJugador1 = view?.findViewById<TextView>(R.id.tvInfoJugador1)
        val tvPuntuacionJugador2 = view?.findViewById<TextView>(R.id.tvInfoJugador2)
        tvPuntuacionJugador1?.text = getString(R.string.infoJugador1, jugador1.puntuacion.toString())
        tvPuntuacionJugador2?.text = getString(R.string.infoJugador2, jugador2.puntuacion.toString())
    }

    private fun avanzar(posicionJugador: Int, tirada: Int, jugador: Jugador) {
        // Calcula la nueva posición del jugador
        var posicionFinal = posicionJugador + tirada

        // Si la posición supera 31, vuelve al inicio y sigue avanzando
        if (posicionFinal > 23) {
            // Regresa al inicio
            jugador.posicion = 0
            // Continúa avanzando con la cantidad restante
            posicionFinal = tirada - (24 - posicionJugador)
        } else {
            // Actualiza la posición del jugador
            jugador.posicion = posicionFinal
        }

        // Actualiza la posición del jugador en el objeto Jugador
        jugador.posicion = posicionFinal

        showToast("Número en el dado: $tirada")
        // Actualiza el texto de los botones en el tablero con los nombres de los jugadores
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button
            if (button != null) {
                // Verifica si cualquiera de los jugadores está en esta posición y actualiza el texto del botón
                button.text = when {
                    i == jugador1.posicion && i == jugador2.posicion -> "J1/J2"
                    i == jugador1.posicion -> "J1"
                    i == jugador2.posicion -> "J2"
                    else -> ""
                }
            }
        }
        //no funciona el cambio de turno si quitas alguno de los dos metodos, ya que uno actualiza la vista y otro el int del turno en el viewmodel
        viewModel.cambiarTurno()
        actualizarTurno()
    }


    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }


}
