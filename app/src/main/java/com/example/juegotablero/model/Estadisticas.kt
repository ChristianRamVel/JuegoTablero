package com.example.juegotablero.model

class Estadisticas {
    companion object {
        const val PARTIDAS_GANADASJ1 = "partidas_ganadas_j1"
        const val MINIJUEGOS_JUGADOSJ1 = "minijuegos_jugados_j1"
        const val MINIJUEGOS_GANADOSJ1 = "minijuegos_ganados_j1"
        const val MINIJUEGOS_PERDIDOSJ1 = "minijuegos_perdidos_j1"

        const val PARTIDAS_GANADASJ2 = "partidas_ganadas_j2"
        const val MINIJUEGOS_JUGADOSJ2 = "minijuegos_jugados_j2"
        const val MINIJUEGOS_GANADOSJ2 = "minijuegos_ganados_j2"
        const val MINIJUEGOS_PERDIDOSJ2 = "minijuegos_perdidos_j2"

        val estadisticas = mapOf(
            PARTIDAS_GANADASJ1 to 0,
            MINIJUEGOS_JUGADOSJ1 to 0,
            MINIJUEGOS_GANADOSJ1 to 0,
            MINIJUEGOS_PERDIDOSJ1 to 0,
            PARTIDAS_GANADASJ2 to 0,
            MINIJUEGOS_JUGADOSJ2 to 0,
            MINIJUEGOS_GANADOSJ2 to 0,
            MINIJUEGOS_PERDIDOSJ2 to 0
        )


    }





}