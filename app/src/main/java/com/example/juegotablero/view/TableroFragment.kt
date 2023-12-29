package com.example.juegotablero.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.juegotablero.R
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.viewModel.TableroViewModel

class TableroFragment : Fragment() {


    companion object {
        fun newInstance() = TableroFragment()
    }

    lateinit var jugador1: Jugador
    lateinit var jugador2: Jugador
    lateinit var viewModel: TableroViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tablero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarPartida()
        val tirarDadoButton = view.findViewById<View>(R.id.btnTirarDado)

        // Al pulsar el botón, se avanza la posición del jugador correspondiente y se cambia de turno
        tirarDadoButton.setOnClickListener {
            if (viewModel.turno == 0){
                jugador1.posicion = viewModel.avanzar(jugador1.posicion, viewModel.tirarDado())
            }else{
                jugador2.posicion = viewModel.avanzar(jugador2.posicion, viewModel.tirarDado())
            }
            actualizarTurno()
        }
    }

    fun inicializarPartida() {
        jugador1 = Jugador("Jugador 1", 0 , 0)
        jugador2 = Jugador("Jugador 2", 0, 0)
        viewModel = ViewModelProvider(this)[TableroViewModel::class.java]
        actualizarPuntuacion()
    }

    fun actualizarTurno() {
        var tvTurno = view?.findViewById<TextView>(R.id.tvInfoPartida)
        viewModel.cambiarTurno()
        if (viewModel.turno == 0){
            tvTurno?.text = getString(R.string.turno_jugador1)
        }else{
            tvTurno?.text = getString(R.string.turno_jugador2)
        }
    }

    fun actualizarPuntuacion() {
        val tvPuntuacionJugador1 = view?.findViewById<TextView>(R.id.tvInfoJugador1)
        val tvPuntuacionJugador2 = view?.findViewById<TextView>(R.id.tvInfoJugador2)
        tvPuntuacionJugador1?.text = getString(R.string.infoJugador1, jugador1.puntuacion.toString())
        tvPuntuacionJugador2?.text = getString(R.string.infoJugador2, jugador2.puntuacion.toString())
    }


}