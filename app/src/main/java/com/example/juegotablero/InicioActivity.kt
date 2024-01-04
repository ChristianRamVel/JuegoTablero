package com.example.juegotablero

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InicioActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_activity)

        val botonNuevaPartida = findViewById<Button>(R.id.nueva_partida)
        botonNuevaPartida.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val botonContinuarPartida = findViewById<Button>(R.id.continuar_partida)

        botonContinuarPartida.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("continuar", true)
            startActivity(intent)
        }


    }
}