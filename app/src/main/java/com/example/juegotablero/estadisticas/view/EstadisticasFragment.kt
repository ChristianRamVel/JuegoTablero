package com.example.juegotablero.estadisticas.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.juegotablero.R
import com.example.juegotablero.estadisticas.model.Estadisticas
import com.example.juegotablero.estadisticas.viewModel.EstadisticasViewModel

class EstadisticasFragment : Fragment() {

    private lateinit var viewModel: EstadisticasViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[EstadisticasViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_estadisticas, container, false)

        // Lee las estadísticas desde SharedPreferences
        leerEstadisticas(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botonResetear = view.findViewById<View>(R.id.botonResetear)

        botonResetear.setOnClickListener {
            viewModel.resetearEstadisticas(requireActivity().getSharedPreferences("Estadisticas", Context.MODE_PRIVATE))

            // Actualizar las estadísticas mostradas
            leerEstadisticas(view)
        }
    }

    fun leerEstadisticas(view : View){
        val estadisticas = viewModel.leerEstadisticas(requireActivity().getSharedPreferences("Estadisticas", Context.MODE_PRIVATE))

        view.findViewById<TextView>(R.id.partidasGanadasJ1).text =
            getString(R.string.partidas_ganadas, estadisticas[Estadisticas.PARTIDAS_GANADASJ1].toString())

        view.findViewById<TextView>(R.id.minijuegosJugadosJ1).text =
            getString(R.string.minijuegos_jugados, estadisticas[Estadisticas.MINIJUEGOS_JUGADOSJ1].toString())

        view.findViewById<TextView>(R.id.minijuegosGanadosJ1).text =
            getString(R.string.minijuegos_ganados, estadisticas[Estadisticas.MINIJUEGOS_GANADOSJ1].toString())

        view.findViewById<TextView>(R.id.minijuegosPerdidosJ1).text =
            getString(R.string.minijuegos_perdidos, estadisticas[Estadisticas.MINIJUEGOS_PERDIDOSJ1].toString())

        view.findViewById<TextView>(R.id.partidasGanadasJ2).text =
            getString(R.string.partidas_ganadas, estadisticas[Estadisticas.PARTIDAS_GANADASJ2].toString())

        view.findViewById<TextView>(R.id.minijuegosJugadosJ2).text =
            getString(R.string.minijuegos_jugados, estadisticas[Estadisticas.MINIJUEGOS_JUGADOSJ2].toString())

        view.findViewById<TextView>(R.id.minijuegosGanadosJ2).text =
            getString(R.string.minijuegos_ganados, estadisticas[Estadisticas.MINIJUEGOS_GANADOSJ2].toString())

        view.findViewById<TextView>(R.id.minijuegosPerdidosJ2).text =
            getString(R.string.minijuegos_perdidos, estadisticas[Estadisticas.MINIJUEGOS_PERDIDOSJ2].toString())
    }
}