package com.example.juegotablero.model

data class Jugador(
    val id: Int,
    val nombre: String,
    var PAdivinaPalabra: Boolean,
    var PParejas: Boolean,
    var PRepaso: Boolean,
    var PTest: Boolean,
    var PFinal: Boolean,
    var posicion: Int)