package com.example.juegotablero.viewModel

import androidx.lifecycle.ViewModel
import com.example.juegotablero.model.Casilla
import com.example.juegotablero.model.Pareja
import com.example.juegotablero.model.Pregunta

class TableroViewModel : ViewModel() {

    //el tablero es un array de 20 casillas
    val tablero = arrayOfNulls<Casilla>(20)

    //inicializa el tablero
    init {
        inicializarTablero()
    }

    //inicializa el tablero con las casillas

    private fun inicializarTablero() {
        tablero[0] = Casilla(
            Pregunta.AdivinaPalabra(
                "animal de compañia que no es un perro",
                "gato"
            )
        )
        tablero[1] = Casilla(
            Pregunta.Repaso(
                "¿El mejor SO es _____?",
                listOf(
                    "Linux",
                    "Windows",
                ),
                "Linux"
            )
        )
        tablero[2] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[3] = Casilla(
            Pregunta.JuegoParejas(
                listOf(
                    //parejas de opciones que hay que unir en el juego de parejas
                    Pareja("Linux", "Pinguino"),
                    Pareja("Android", "Robot"),
                    Pareja("MacOS", "Manzana"),
                    Pareja("Windows", "Ventana"),

                ),
            )
        )

        tablero[4] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[5] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[6] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[7] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[8] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[9] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[10] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[11] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas",
                    "Es un programa que ordeña vacas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[12] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[13] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador",
                    "Es un programa que hace cosas"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[14] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[15] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[16] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[17] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[18] = Casilla(
            Pregunta.Repaso(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )

        tablero[19] = Casilla(
            Pregunta.Test(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )
        tablero[20] = Casilla(
            Pregunta.PruebaFinal(
                "¿Qué es un sistema operativo?",
                listOf(
                    "Es un programa que gestiona los recursos de un ordenador"
                ),
                "Es un programa que gestiona los recursos de un ordenador"
            )
        )


    }
}


