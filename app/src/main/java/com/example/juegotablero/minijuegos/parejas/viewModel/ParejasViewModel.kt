package com.example.juegotablero.minijuegos.parejas.viewModel

import androidx.lifecycle.ViewModel

class ParejasViewModel : ViewModel() {

    private val parejasDePalabras = mutableListOf<Pair<String, String>>()
    private val aciertos = mutableSetOf<Pair<String, String>>()


    // Agrega una nueva palabra y su clase a la lista de parejas
    fun anadirParejas(animal: String, claseAnimal: String) {
        val newWordPair = Pair(animal, claseAnimal)
        parejasDePalabras.add(newWordPair)
    }


    // Verifica si la palabra seleccionada coincide con su pareja
    fun isMatch(pareja1: String, pareja2: String): Boolean {
        // Verificar si la pareja ya ha sido acertada
        val pareja = pareja1 to pareja2
        val alreadyMatched = aciertos.contains(pareja)
        if (alreadyMatched) {
            return false  // Ya ha sido acertada, no permitir otra vez
        }

        val correctType = parejasDePalabras.find { it.first == pareja1 }?.second
        val match = correctType == pareja2

        if (match) {
            // Incrementar el contador de aciertos
            aciertos.add(pareja)
        }

        return match
    }

    fun getParejaPalabra1(word: String): String? {
        return parejasDePalabras.find { it.first == word }?.second
    }

    fun getparejaPalabra2(word: String): String? {
        return parejasDePalabras.find { it.second == word }?.first
    }

    fun getPar1(): List<String> {
        return parejasDePalabras.map { it.first }
    }

    fun getPar2(): List<String> {
        return parejasDePalabras.map { it.second }
    }

    fun getAciertos(): Int {
        return aciertos.size
    }

    fun getParejasDePalabras(): List<Pair<String, String>> {
        return parejasDePalabras
    }

}
