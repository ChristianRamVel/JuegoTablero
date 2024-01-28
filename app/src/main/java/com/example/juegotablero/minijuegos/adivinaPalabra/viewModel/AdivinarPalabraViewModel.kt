package com.example.juegotablero.minijuegos.adivinaPalabra.viewModel

import androidx.lifecycle.ViewModel

class AdivinarPalabraViewModel : ViewModel() {

    private var palabraAdivinar = ""
    private var intentos = 5


    fun setPalabraAdivinar(palabraAdivinar: String){
        this.palabraAdivinar = palabraAdivinar
    }

    fun getPalabraAdivinar(): String{
        return palabraAdivinar
    }

    fun getIntentos(): Int{
        return intentos
    }

    fun decrementarIntentos(){
        intentos--
    }
}