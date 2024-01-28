package com.example.juegotablero

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.juegotablero.common.interfaces.OnGameEventListener
import com.example.juegotablero.minijuegos.adivinaPalabra.view.AdivinarPalabraFragment
import com.example.juegotablero.estadisticas.view.EstadisticasFragment
import com.example.juegotablero.añadirPreguntas.view.MenuNuevaPreguntaFragment
import com.example.juegotablero.minijuegos.parejas.view.ParejasFragment
import com.example.juegotablero.minijuegos.repaso.view.RepasoFragment
import com.example.juegotablero.tablero.view.TableroFragment
import com.example.juegotablero.minijuegos.test.view.TestFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import android.media.MediaPlayer
import android.widget.Button
import android.widget.ImageView
import com.example.juegotablero.api.db.DatabaseHelper
import com.example.juegotablero.estadisticas.viewModel.EstadisticasViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnGameEventListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.musica)

        // Reproduce la música en un bucle (si se desea)
        mediaPlayer?.isLooping = true

        startMusic()

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
        toolbar.layoutParams = params

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        if (intent.hasExtra("idPartida")) {
            val bundle = Bundle()
            bundle.putInt("idPartida", intent.getIntExtra("idPartida", 0))

            val tableroFragment = TableroFragment()
            tableroFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, tableroFragment, TableroFragment.TAG)
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TableroFragment(), TableroFragment.TAG)
                .commit()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.new_game -> {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TableroFragment(), TableroFragment.TAG)
                    .commit()
            }
            R.id.new_questions -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, MenuNuevaPreguntaFragment())
                transaction.addToBackStack(null) // Agregar la transacción a la pila de retroceso
                transaction.commit()
            }
            R.id.stats -> {

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, EstadisticasFragment())
                transaction.addToBackStack(null) // Agregar la transacción a la pila de retroceso
                transaction.commit()


            }
            R.id.settings -> {
                mostrarAjustes()
            }
            R.id.exit -> {
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        // Si el fragmento actual es una pregunta no permite volver atrás
        if (currentFragment is RepasoFragment || currentFragment is ParejasFragment || currentFragment is AdivinarPalabraFragment || currentFragment is TestFragment || currentFragment is TableroFragment) {
            // No hace nada
        }else{
            super.onBackPressed()
        }
    }

    override fun onGameResult(isWinner: Boolean) {
        val tableroFragment = supportFragmentManager.findFragmentByTag(TableroFragment.TAG) as? TableroFragment
        tableroFragment?.onGameResult(isWinner)
    }


    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()

    }

    override fun onResume() {
        super.onResume()
        startMusic()
    }
    fun startMusic(){
        val sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)

        val music = sharedPreferences.getBoolean("music", true)
        if (music) {
            mediaPlayer?.start()
        }
    }

    private fun mostrarAjustes() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_ajustes_inicio)

        val vibrationIcon = dialog.findViewById<ImageView>(R.id.vibrationIcon)
        val musicIcon = dialog.findViewById<ImageView>(R.id.musicIcon)

        // comprobar si la vibracion esta activada o no en shared preferences
        val sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)


        var vibration = sharedPreferences.getBoolean("vibration", true)
        if (vibration) {
            vibrationIcon.setImageResource(R.drawable.vibration)
        } else {
            vibrationIcon.setImageResource(R.drawable.vibration_disabled)
        }

        var music = sharedPreferences.getBoolean("music", true)
        if (music) {
            musicIcon.setImageResource(R.drawable.music)
        } else {
            musicIcon.setImageResource(R.drawable.music_off)
        }

        vibrationIcon.setOnClickListener {
            vibration = sharedPreferences.getBoolean("vibration", true)
            val editor = sharedPreferences.edit()
            if (vibration) {
                vibrationIcon.setImageResource(R.drawable.vibration_disabled)
                editor.putBoolean("vibration", false)
                Snackbar.make(findViewById(R.id.drawer_layout), "Vibración desactivada", Snackbar.LENGTH_SHORT).show()
            } else {
                vibrationIcon.setImageResource(R.drawable.vibration)
                editor.putBoolean("vibration", true)
                Snackbar.make(findViewById(R.id.drawer_layout), "Vibración activada", Snackbar.LENGTH_SHORT).show()
            }
            editor.apply()
        }

        musicIcon.setOnClickListener {
            music = sharedPreferences.getBoolean("music", true)
            val editor = sharedPreferences.edit()
            if (music) {
                musicIcon.setImageResource(R.drawable.music_off)
                editor.putBoolean("music", false)
                mediaPlayer?.pause()
                Snackbar.make(findViewById(R.id.drawer_layout), "Música desactivada", Snackbar.LENGTH_SHORT).show()
            } else {
                musicIcon.setImageResource(R.drawable.music)
                editor.putBoolean("music", true)
                mediaPlayer?.start()
                Snackbar.make(findViewById(R.id.drawer_layout), "Música activada", Snackbar.LENGTH_SHORT).show()
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
            Snackbar.make(findViewById(R.id.drawer_layout), "Partidas borradas", Snackbar.LENGTH_SHORT).show()
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
            Snackbar.make(findViewById(R.id.drawer_layout), "Estadisticas reseteadas", Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        dialog.show()
    }
}


