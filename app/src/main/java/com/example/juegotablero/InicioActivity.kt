package com.example.juegotablero

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.juegotablero.model.DatabaseHelper
import com.example.juegotablero.model.Estadisticas
import com.example.juegotablero.model.Partida
import com.example.juegotablero.model.Pregunta
import com.example.juegotablero.view.PartidasAdapter
import com.example.juegotablero.viewModel.EstadisticasViewModel
import com.google.android.material.snackbar.Snackbar

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

        val botonAjustes = findViewById<Button>(R.id.ajustes)
        botonAjustes.setOnClickListener {
            mostrarAjustes()
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
            Snackbar.make(findViewById(R.id.inicio), "Partida eliminada", Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
            mostrarPartidasGuardadas()
        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        dialog.show()
    }


    private fun mostrarAjustes() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_ajustes_inicio)

        val vibrationIcon = dialog.findViewById<ImageView>(R.id.vibrationIcon)
        // comprobar si la vibracion esta activada o no en shared preferences
        val sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)

        var vibration = sharedPreferences.getBoolean("vibration", true)
        if (vibration) {
            vibrationIcon.setImageResource(R.drawable.vibration)
        } else {
            vibrationIcon.setImageResource(R.drawable.vibration_disabled)
        }

        vibrationIcon.setOnClickListener {
            vibration = sharedPreferences.getBoolean("vibration", true)
            val editor = sharedPreferences.edit()
            if (vibration) {
                vibrationIcon.setImageResource(R.drawable.vibration_disabled)
                editor.putBoolean("vibration", false)
                Snackbar.make(findViewById(R.id.inicio), "Vibración desactivada", Snackbar.LENGTH_SHORT).show()
            } else {
                vibrationIcon.setImageResource(R.drawable.vibration)
                editor.putBoolean("vibration", true)
                Snackbar.make(findViewById(R.id.inicio), "Vibración activada", Snackbar.LENGTH_SHORT).show()
            }
            editor.apply()
        }


        val btnBorrarPartidas = dialog.findViewById<Button>(R.id.btnBorrarPartidas)
        btnBorrarPartidas.setOnClickListener {
            dialog.dismiss()
            showAlertBorrarPartidas()
        }

        val btnBorrarEstadisticas = dialog.findViewById<Button>(R.id.btnResetearEstadisticas)
        btnBorrarEstadisticas.setOnClickListener {
            dialog.dismiss()
            showAlertBorrarEstadisticas()
        }

        dialog.show()
    }

    private fun showAlertBorrarPartidas() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)

        builder.setTitle("¿Quieres borrar todas las partidas?")
        builder.setMessage("Estas a punto de borrar todas las partidas guardadas")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val dbHelper = DatabaseHelper(this)
            dbHelper.eliminarPartidas()
            Snackbar.make(findViewById(R.id.inicio), "Partidas borradas", Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()

        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        dialog.show()
    }

    private fun showAlertBorrarEstadisticas() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)

        builder.setTitle("¿Quieres resetear todas las estadisticas?")
        builder.setMessage("Estas a punto de resetear todas las estadisticas guardadas")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val estadisticasViewModel = EstadisticasViewModel()
            estadisticasViewModel.resetearEstadisticas(getSharedPreferences("Estadisticas", Context.MODE_PRIVATE))
            Snackbar.make(findViewById(R.id.inicio), "Estadisticas reseteadas", Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        dialog.show()
    }









}