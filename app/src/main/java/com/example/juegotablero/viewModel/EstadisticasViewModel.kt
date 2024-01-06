package com.example.juegotablero.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.juegotablero.model.Estadisticas

class EstadisticasViewModel : ViewModel() {


    fun leerEstadisticas(prefs: SharedPreferences): Map<String, Int> {
        return Estadisticas.estadisticas.mapValues { (key, defaultValue) ->
            prefs.getInt(key, defaultValue)
        }
    }






}