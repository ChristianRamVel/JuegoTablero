package com.example.juegotablero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juegotablero.R
import com.example.juegotablero.view.TableroFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa y muestra el fragmento del tablero al iniciar la actividad.
        val tableroFragment = TableroFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, tableroFragment)
            .commit()
    }
}