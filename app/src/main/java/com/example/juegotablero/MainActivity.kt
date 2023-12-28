package com.example.juegotablero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.example.juegotablero.view.ParejasFragment
import com.example.juegotablero.view.RepasoFragment
import com.example.juegotablero.view.TestFragment
import com.example.juegotablero.view.TableroFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar
        val toolbar =  findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        // Inicializa y muestra el fragmento del tablero al iniciar la actividad.
        val tableroFragment = TableroFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ParejasFragment())
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


}