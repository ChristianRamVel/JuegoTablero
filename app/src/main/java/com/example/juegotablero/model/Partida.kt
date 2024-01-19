package com.example.juegotablero.model
data class Partida(
    var id: Int,
    var jugador1ID: Int,
    var jugador2ID: Int,
    var turno: Int,
    var fecha: String = "",
)