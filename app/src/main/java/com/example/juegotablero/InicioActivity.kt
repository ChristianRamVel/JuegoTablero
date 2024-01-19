package com.example.juegotablero

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.juegotablero.model.DatabaseHelper
import com.example.juegotablero.model.Partida
import com.example.juegotablero.model.Pregunta
import com.example.juegotablero.view.PartidasAdapter

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
            mostrarPartidasGuardadas()
        }


    }

    fun mostrarPartidasGuardadas() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_partidas_guardadas)

        val dbHelper = DatabaseHelper(this)
        val partidas = dbHelper.obtenerPartidas().reversed()

        val adapter = PartidasAdapter(this, R.layout.list_item_partida_guardada , partidas)

        val lvPartidasGuardadas = dialog.findViewById<ListView>(R.id.lvPartidas)
        lvPartidasGuardadas.adapter = adapter
        lvPartidasGuardadas.setOnItemClickListener { _, _, position, _ ->
            dialog.dismiss()
            val partida = partidas[position]
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("idPartida", partida.id)
            startActivity(intent)
        }

        lvPartidasGuardadas.setOnItemLongClickListener { _, _, position, _ ->
            dialog.dismiss()
            val partida = partidas[position]
            showAlertBorrarPartida(partida)
            true
        }


        val btnCerrarModal = dialog.findViewById<Button>(R.id.btnCerrar)
        btnCerrarModal.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showAlertBorrarPartida(partida: Partida) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)

        builder.setTitle("¿Quieres borrar la partida?")
        builder.setMessage("Estas a punto de borrar la partida ${partida.id}")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val dbHelper = DatabaseHelper(this)
            dbHelper.eliminarPartidaYJugadores(partida.id, partida.jugador1ID, partida.jugador2ID)
            mostrarPartidasGuardadas()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
            mostrarPartidasGuardadas()
        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        dialog.show()
    }









}