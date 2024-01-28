package com.example.juegotablero.tablero.view

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.example.juegotablero.R
import com.example.juegotablero.common.interfaces.ObtenerPreguntasCallback
import com.example.juegotablero.common.interfaces.OnGameEventListener
import com.example.juegotablero.estadisticas.model.Estadisticas
import com.example.juegotablero.tablero.model.Jugador
import com.example.juegotablero.api.model.Pregunta
import com.example.juegotablero.minijuegos.adivinaPalabra.view.AdivinarPalabraFragment
import com.example.juegotablero.minijuegos.parejas.view.ParejasFragment
import com.example.juegotablero.minijuegos.pruebaFinal.view.PruebaFinalFragment
import com.example.juegotablero.minijuegos.repaso.view.RepasoFragment
import com.example.juegotablero.minijuegos.test.view.TestFragment
import com.example.juegotablero.tablero.viewModel.TableroViewModel
import com.example.juegotablero.tablero.viewModel.TableroViewModelFactory
import com.google.firebase.database.DatabaseError

class TableroFragment : Fragment(), OnGameEventListener {

    private var vista : View? = null
    private lateinit var viewModel: TableroViewModel
    private lateinit var jugadorActual: Jugador
    private lateinit var gridLayout: GridLayout
    private lateinit var tvInfoPartida: TextView

    companion object {
        const val TAG = "TableroFragment"
        // Resto del código
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View?



        if (vista == null) {
            vista = inflater.inflate(R.layout.fragment_tablero, container, false)
            view = vista
            gridLayout = view?.findViewById(R.id.gridLayoutTablero)!!
            tvInfoPartida = view.findViewById(R.id.tvInfoPartida)!!

            val btnTirarDado = view.findViewById<AppCompatImageButton>(R.id.btnTirarDado)!! // Cambié de Button a AppCompatImageButton
            btnTirarDado.isEnabled = true

            val factory = TableroViewModelFactory(requireContext()) // O applicationContext según el contexto que tengas
            viewModel = ViewModelProvider(this, factory)[TableroViewModel::class.java]


            val idPartida = arguments?.getInt("idPartida")

            if (idPartida != null) {
                viewModel.cargarPartida(idPartida)
                viewModel.cargarJugadores()

                setupTablero()
                actualizarTurno()
                actualizarTablero()
                actualizarPuntuacion()

            } else {
                mostrarCrearJugadores()
            }



        }
        view = vista

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se inicializan los componentes de la vista
        val btnTirarDado = view.findViewById<ImageButton>(R.id.btnTirarDado)!!
        btnTirarDado.isEnabled = true

        val idPartida = arguments?.getInt("idPartida")

        if (idPartida != null) {
            actualizarNombres(viewModel.jugador1.nombre, viewModel.jugador2.nombre)
            actualizarPuntuacion()
        }


        btnTirarDado.setOnClickListener {

            if (!isInternetAvailable()) {
                showToast(getString(R.string.noInternet))
                return@setOnClickListener
            }


            btnTirarDado.isEnabled = false

            val sharedPreferences = requireContext().getSharedPreferences("config", Context.MODE_PRIVATE)
            val vibration = sharedPreferences.getBoolean("vibration", true)
            if (vibration){
                vibrate()
            }


            // Se obtiene el jugador de el turno actual
            jugadorActual = if (viewModel.turno == 0) viewModel.jugador1 else viewModel.jugador2
            val ultimaTirada = viewModel.tirarDado()

            //se camia la imagen del dado
            when(ultimaTirada){
                1 -> btnTirarDado.setImageResource(R.drawable.dado1)
                2 -> btnTirarDado.setImageResource(R.drawable.dado2)
                3 -> btnTirarDado.setImageResource(R.drawable.dado3)
                4 -> btnTirarDado.setImageResource(R.drawable.dado4)
                5 -> btnTirarDado.setImageResource(R.drawable.dado5)
                6 -> btnTirarDado.setImageResource(R.drawable.dado6)
            }

            // Se obtiene una pregunta aleatoria de la base de datos dependendiendo del tipo de casilla
            val preguntaCallback = object : ObtenerPreguntasCallback {
                override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                    if (preguntas.isNotEmpty()) {
                        actualizarTurno()
                        viewModel.guardarPartida()
                        viewModel.guardarJugadores()
                        if (viewModel.paseAPreguntaFinal(jugadorActual)) {
                            showAlertMinijuego(getString(R.string.mensajePruebaFinal), preguntas, ultimaTirada)

                        }else{
                            showAlertMinijuego(getString(R.string.casillaCaida, viewModel.obtenerTipoCasilla(jugadorActual)) , preguntas, ultimaTirada)

                        }
                    } else {
                        showToast("No se obtuvieron preguntas")
                    }
                }

                override fun onError(error: DatabaseError) {
                    // Manejar el error obtenido desde la base de datos
                    showToast("Error al obtener las preguntas, inténtelo mas tarde {${error.message}}")
                }
            }

            avanzar(jugadorActual.posicion, ultimaTirada)

            // Se obtiene una pregunta aleatoria de la base de datos
            viewModel.obtenerPreguntaAleatoria(jugadorActual, preguntaCallback)
        }
    }

    private fun setupTablero() {

        val colorRojo = ContextCompat.getColor(requireContext(), R.color.casillaAdivinaPalabra)
        val colorAzul = ContextCompat.getColor(requireContext(), R.color.casillaJuegoparejas)
        val colorAmarillo = ContextCompat.getColor(requireContext(), R.color.casillaRepaso)
        val colorVerde = ContextCompat.getColor(requireContext(), R.color.casillaTest)

        val colors = listOf(
            colorRojo,
            colorAzul,
            colorAmarillo,
            colorVerde
        )

        val numFilas = 6
        val numColumnas = 4

        for (fila in 0 until numFilas) {
            for (columna in 0 until numColumnas) {
                val button = Button(requireContext())
                val layoutParams = GridLayout.LayoutParams()

                layoutParams.rowSpec = GridLayout.spec(fila)
                layoutParams.columnSpec = GridLayout.spec(columna)
                button.layoutParams = layoutParams
                button.setBackgroundColor(colors[(fila + columna) % colors.size])
                button.text = ""
                gridLayout.addView(button)
            }
        }
    }

    private fun actualizarTurno() {

        tvInfoPartida.text = if (viewModel.turno == 0) {
            getString(R.string.turno_jugador1, viewModel.jugador1.nombre)
        } else {
            getString(R.string.turno_jugador2, viewModel.jugador2.nombre)
        }
    }

    private fun actualizarPuntuacion() {
        val tvPuntuacionJugador1 = view?.findViewById<TextView>(R.id.tvInfoJugador1)
        val tvPuntuacionJugador2 = view?.findViewById<TextView>(R.id.tvInfoJugador2)

        // Función para actualizar el TextView sin duplicar la cadena
        fun actualizarTextView(tv: TextView?, nuevoTexto: String) {
            val textoActual = tv?.text.toString()
            if (!textoActual.contains(nuevoTexto)) {
                tv?.text = "$textoActual\n$nuevoTexto"
            }
        }

        if (viewModel.jugador1.PAdivinaPalabra) {
            actualizarTextView(tvPuntuacionJugador1, getString(R.string.PAdivinaPalabra))
        }
        if (viewModel.jugador1.PParejas) {
            actualizarTextView(tvPuntuacionJugador1, getString(R.string.PParejas))
        }
        if (viewModel.jugador1.PRepaso) {
            actualizarTextView(tvPuntuacionJugador1, getString(R.string.PRepaso))
        }
        if (viewModel.jugador1.PTest) {
            actualizarTextView(tvPuntuacionJugador1, getString(R.string.PTest))
        }
        if (viewModel.jugador1.PFinal) {
            actualizarTextView(tvPuntuacionJugador1, getString(R.string.PFinal))
        }
        if (viewModel.jugador2.PAdivinaPalabra) {
            actualizarTextView(tvPuntuacionJugador2, getString(R.string.PAdivinaPalabra))
        }
        if (viewModel.jugador2.PParejas) {
            actualizarTextView(tvPuntuacionJugador2, getString(R.string.PParejas))
        }
        if (viewModel.jugador2.PRepaso) {
            actualizarTextView(tvPuntuacionJugador2, getString(R.string.PRepaso))
        }
        if (viewModel.jugador2.PTest) {
            actualizarTextView(tvPuntuacionJugador2, getString(R.string.PTest))
        }
        if (viewModel.jugador2.PFinal) {
            actualizarTextView(tvPuntuacionJugador2, getString(R.string.PFinal))
        }
    }

    private fun avanzar(posicionJugador: Int, tirada: Int) {
        // Calcula la nueva posición del jugador
        var posicionFinal = posicionJugador + tirada


        // Si la posición supera 31, vuelve al inicio y sigue avanzando
        if (posicionFinal > 23) {
            // Regresa al inicio

            if (viewModel.turno == 0) {
                viewModel.jugador1.posicion = 0
            } else {
                viewModel.jugador2.posicion = 0
            }
            // Continúa avanzando con la cantidad restante
            posicionFinal = tirada - (24 - posicionJugador)
        } else {
            // Actualiza la posición del jugador
              if (viewModel.turno == 0) {
                    viewModel.jugador1.posicion = posicionFinal
                } else {
                    viewModel.jugador2.posicion = posicionFinal
                }
        }

        // Actualiza la posición del jugador en el objeto Jugador
        if (viewModel.turno == 0) {
            viewModel.jugador1.posicion = posicionFinal
        } else {
            viewModel.jugador2.posicion = posicionFinal
        }

        // Actualiza el texto de los botones en el tablero con los nombres de los jugadores
        actualizarTablero()
    }


    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showAlertMinijuego(message: String, preguntas: List<Pregunta>, ultimaTirada: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        if (viewModel.paseAPreguntaFinal(viewModel.jugador1) || viewModel.paseAPreguntaFinal(viewModel.jugador2)) builder.setTitle("Estas listo")
        else
            builder.setTitle(getString(R.string.tirada, ultimaTirada))
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.aceptar)) { _, _ ->
            // Al pulsar en aceptar se abre el fragment correspondiente a la pregunta
            mostrarPregunta(preguntas)
        }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        // Evita que se cierre el dialogo al pulsar fuera de el
        dialog.setCanceledOnTouchOutside(false)

        val buttonColor = if (isDarkModeEnabled()) {
            // Color para modo oscuro
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            // Color para modo claro
            ContextCompat.getColor(requireContext(), R.color.black)
        }

        dialog.setOnShowListener {
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(buttonColor)
        }

        dialog.show()
    }

    fun showAlertFinalDePartida(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.finDePartida))
        builder.setMessage(message)

        builder.setPositiveButton(getString(R.string.salir)) { _, _ ->
              // Al pulsar en aceptar se abre el fragment correspondiente a la pregunta
                requireActivity().finish()
        }

        val dialog: androidx.appcompat.app.AlertDialog = builder.create()

        // Evita que se cierre el dialogo al pulsar fuera de el
        dialog.setCanceledOnTouchOutside(false)

        val buttonColor = if (isDarkModeEnabled()) {
            // Color para modo oscuro
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            // Color para modo claro
            ContextCompat.getColor(requireContext(), R.color.black)
        }

        dialog.setOnShowListener {
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(buttonColor)
        }

        dialog.show()
    }

    private fun mostrarPregunta(preguntas : List<Pregunta>){
        when(val pregunta = preguntas.randomOrNull()){
            is Pregunta.AdivinaPalabra -> {
                val adivinarPalabraFragment = AdivinarPalabraFragment()
                val bundle = Bundle()

                adivinarPalabraFragment.setGameListener(this)

                bundle.putString("definicion", pregunta.definicion)
                bundle.putString("palabra", pregunta.palabra)
                adivinarPalabraFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, adivinarPalabraFragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }
            is Pregunta.JuegoParejas -> { //llamar al fragmento de parejas y pasarle una pregunta recogida del json de firebase

                val parejasFragment = ParejasFragment()

                parejasFragment.setGameListener(this)


                val bundle = Bundle()
                    bundle.putParcelableArrayList("parejas", ArrayList(pregunta.parejas))
                    parejasFragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, parejasFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()



            }
            is Pregunta.Repaso -> {
                val repasoFragment = RepasoFragment()
                val bundle = Bundle()

                repasoFragment.setGameListener(this)

                añadirTrancision(repasoFragment)

                repasoFragment.exitTransition = null

                bundle.putString("enunciado", pregunta.enunciado)
                bundle.putStringArray("opciones", pregunta.opciones.toTypedArray())
                bundle.putString("respuesta", pregunta.respuesta_correcta)
                repasoFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, repasoFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            is Pregunta.Test -> {
                val testFragment = TestFragment(preguntas)
                val bundle = Bundle()

                testFragment.setGameListener(this)

                añadirTrancision(testFragment)

                testFragment.exitTransition = null

                bundle.putString("definicion", pregunta.enunciado)
                bundle.putStringArray("opciones", pregunta.opciones.toTypedArray())
                bundle.putString("respuesta", pregunta.respuesta_correcta)
                testFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, testFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            is Pregunta.PruebaFinal -> {
                val pruebaFinalFragment = PruebaFinalFragment()
                val bundle = Bundle()

                pruebaFinalFragment.setGameListener(this)

                añadirTrancision(pruebaFinalFragment)

                pruebaFinalFragment.exitTransition = null

                bundle.putString("enunciado", pregunta.enunciado)
                bundle.putStringArray("opciones", pregunta.opciones?.toTypedArray())
                bundle.putString("respuesta", pregunta.respuesta_correcta)
                pruebaFinalFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, pruebaFinalFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            else -> {
                Toast.makeText(requireContext(), "Error al mostrar la pregunta", Toast.LENGTH_SHORT).show()}
        }

    }

    private fun isDarkModeEnabled(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    fun añadirTrancision(fragment: Fragment){
        val transicionEntrada = Slide(Gravity.END)
        transicionEntrada.duration = 200
        fragment.enterTransition =  transicionEntrada
    }




    private fun actualizarTablero() {
        // Actualiza el texto de los botones en el tablero con los nombres de los jugadores
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button
                // Verifica si cualquiera de los jugadores está en esta posición y actualiza el texto del botón
                button?.text = when {
                    i == viewModel.jugador1.posicion && i == viewModel.jugador2.posicion -> "J1/J2"
                    i == viewModel.jugador1.posicion -> "J1"
                    i == viewModel.jugador2.posicion -> "J2"
                    else -> ""
                }
        }
    }



    override fun onGameResult(isWinner: Boolean) {
        val refs = requireActivity().getSharedPreferences("Estadisticas", Context.MODE_PRIVATE)

        if (isWinner) {
            if (viewModel.paseAPreguntaFinal(viewModel.jugador1) || viewModel.paseAPreguntaFinal(viewModel.jugador2)) {
                    viewModel.sumarPuntoFinal(jugadorActual)

            }
            // Se incrementa la puntuación del jugador
            else if (viewModel.turno == 0) {
                viewModel.sumarPunto(viewModel.jugador1)
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_GANADOSJ1)
            } else {
                viewModel.sumarPunto(viewModel.jugador2)
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_GANADOSJ2)
            }

            // Se comprueba si el jugador ha ganado
            if (viewModel.haGanado(jugadorActual)) {
                showAlertFinalDePartida("${if (viewModel.turno == 0) viewModel.jugador1.nombre else viewModel.jugador2.nombre } ha ganado")
                if (viewModel.turno == 0) viewModel.guardarEstadistica(refs, Estadisticas.PARTIDAS_GANADASJ1)
                else viewModel.guardarEstadistica(refs, Estadisticas.PARTIDAS_GANADASJ2)
            }

            if (viewModel.turno == 0) viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_JUGADOSJ1)
            else viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_JUGADOSJ2)

        }else{
            if (viewModel.turno == 0){
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_PERDIDOSJ1)
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_JUGADOSJ1)
            }
            else{
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_PERDIDOSJ2)
                viewModel.guardarEstadistica(refs, Estadisticas.MINIJUEGOS_JUGADOSJ2)
            }
            viewModel.cambiarTurno()
            actualizarTurno()
        }

        viewModel.guardarJugadores()
        viewModel.guardarPartida()

    }

    fun vibrate() {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(requireContext(), ConnectivityManager::class.java) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    private fun mostrarCrearJugadores(){
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_crear_jugadores)
        dialog.setCanceledOnTouchOutside(false)

        val btnCrearJugadores = dialog.findViewById<Button>(R.id.btnCrearJugadores)

        btnCrearJugadores.setOnClickListener {
            val nombreJugador1 = dialog.findViewById<TextView>(R.id.etJugador1).text.toString()
            val nombreJugador2 = dialog.findViewById<TextView>(R.id.etJugador2).text.toString()

            if (nombreJugador1.isNotEmpty() && nombreJugador2.isNotEmpty()) {
                viewModel.crearJugadores(nombreJugador1, nombreJugador2)
                viewModel.crearPartida()


                actualizarNombres(nombreJugador1, nombreJugador2)

                setupTablero()
                actualizarTurno()
                actualizarTablero()

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Introduce un nombre para cada jugador", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun actualizarNombres(nombreJugador1 : String, nombreJugador2 : String){
        val tvPuntuacionJugador1 = view?.findViewById<TextView>(R.id.tvInfoJugador1)
        val tvPuntuacionJugador2 = view?.findViewById<TextView>(R.id.tvInfoJugador2)

        tvPuntuacionJugador1?.text = nombreJugador1 + ":"
        tvPuntuacionJugador2?.text = nombreJugador2 + ":"


    }

}
