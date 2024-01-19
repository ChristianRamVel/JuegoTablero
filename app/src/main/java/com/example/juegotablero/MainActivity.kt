package com.example.juegotablero

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
import com.example.juegotablero.view.AdivinarPalabraFragment
import com.example.juegotablero.view.EstadisticasFragment
import com.example.juegotablero.view.MenuNuevaPreguntaFragment
import com.example.juegotablero.view.ParejasFragment
import com.example.juegotablero.view.RepasoFragment
import com.example.juegotablero.view.TableroFragment
import com.example.juegotablero.view.TestFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import android.media.MediaPlayer
import androidx.core.content.ContentProviderCompat.requireContext

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
        mediaPlayer?.start()

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
            R.id.about_us -> {

            }
            R.id.settings -> {

            }
            R.id.musica -> {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()

                } else {
                    mediaPlayer?.start()
                }
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

}


