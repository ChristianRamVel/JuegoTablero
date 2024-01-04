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
import com.example.juegotablero.view.AdivinarPalabraFragment
import com.example.juegotablero.view.ParejasFragment
import com.example.juegotablero.view.RepasoFragment
import com.example.juegotablero.view.TableroFragment
import com.example.juegotablero.view.TestFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)




        // Resto del código para inicializar el fragmento y realizar otras acciones...
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ParejasFragment())
                .commit()
        }


        if (intent.hasExtra("continuar") && intent.getBooleanExtra("continuar", false)) {
            cargarPartida()
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_game -> {
                val tableroFragment = TableroFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ParejasFragment())
                    .commit()
            }
            R.id.new_questions -> {

            }
            R.id.stats -> {

            }
            R.id.about_us -> {

            }
            R.id.settings -> {

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

    fun cargarPartida(){
        val tableroFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? TableroFragment
        tableroFragment?.cargarPartida()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        // Si el fragmento actual es una pregunta no permite volver atrás
        if (currentFragment is RepasoFragment || currentFragment is ParejasFragment || currentFragment is AdivinarPalabraFragment || currentFragment is TestFragment) {
            // No hace nada
        }else{
            super.onBackPressed()
        }
    }

}


