package com.example.juegotablero.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.juegotablero.R
import com.example.juegotablero.api.PreguntasCallback
import com.example.juegotablero.model.Jugador
import com.example.juegotablero.model.Pregunta
import com.example.juegotablero.viewModel.TableroViewModel
import com.google.firebase.database.DatabaseError

class TableroFragment : Fragment() {

    private lateinit var jugador1: Jugador
    private lateinit var jugador2: Jugador
    private lateinit var viewModel: TableroViewModel
    private lateinit var gridLayout: GridLayout
    private lateinit var tvInfoPartida: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tablero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se inicializan los jugadores
        jugador1 = Jugador("Jugador 1", 0, 0)
        jugador2 = Jugador("Jugador 2", 0, 0)

        // Se inicializa el ViewModel
        viewModel = ViewModelProvider(this)[TableroViewModel::class.java]

        // Se inicializan los componentes de la vista
        gridLayout = view.findViewById(R.id.gridLayoutTablero)
        tvInfoPartida = view.findViewById(R.id.tvInfoPartida)
        val btnTirarDado = view.findViewById<Button>(R.id.btnTirarDado)

        // Se crea el tablero
        setupTablero()

        // Se actualiza la puntación de los jugadores
        actualizarPuntuacion()
        actualizarTablero()

        val bundle = arguments

        // Si el usuario ha pulsado en continuar partida en el menu principal
        // se carga la ultima partidad guardada
        if (bundle != null) {
            val valor = bundle.getBoolean("cargarPartida")
            if (valor) {
                cargarPartida()
            }
        }

        btnTirarDado.setOnClickListener {
            // Se obtiene el jugador de el turno actual
            val jugador = if (viewModel.turno == 0) jugador1 else jugador2

            // Se obtiene una pregunta aleatoria de la base de datos dependendiendo del tipo de casilla
            val preguntaCallback = object : PreguntasCallback {
                override fun onPreguntasObtenidas(preguntas: List<Pregunta>) {
                    if (preguntas.isNotEmpty()) {
                        val pregunta = preguntas.randomOrNull()
                        actualizarTurno()
                        if (pregunta != null) {
                            showAlert("Has caído en una casilla de ${viewModel.obtenerTipoCasilla(jugador)}", pregunta)
                        }
                    } else {
                        showToast("No se obtuvieron preguntas")
                    }
                }

                override fun onError(error: DatabaseError) {
                    // Manejar el error obtenido desde la base de datos
                    showToast("Error al obtener las preguntas, inténtelo mas tarde")
                }
            }

            // Se tira el dado y se avanza de casilla
            avanzar(jugador.posicion, viewModel.tirarDado(), jugador)

            // Se obtiene una pregunta aleatoria de la base de datos
            viewModel.obtenerPreguntaAleatoria(jugador, preguntaCallback)
            guardarPartida()
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
            getString(R.string.turno_jugador1)
        } else {
            getString(R.string.turno_jugador2)
        }
    }

    private fun actualizarPuntuacion() {
        val tvPuntuacionJugador1 = view?.findViewById<TextView>(R.id.tvInfoJugador1)
        val tvPuntuacionJugador2 = view?.findViewById<TextView>(R.id.tvInfoJugador2)
        tvPuntuacionJugador1?.text = getString(R.string.infoJugador1, jugador1.puntuacion.toString())
        tvPuntuacionJugador2?.text = getString(R.string.infoJugador2, jugador2.puntuacion.toString())
    }

    private fun avanzar(posicionJugador: Int, tirada: Int, jugador: Jugador) {
        // Calcula la nueva posición del jugador
        var posicionFinal = posicionJugador + tirada

        // Si la posición supera 31, vuelve al inicio y sigue avanzando
        if (posicionFinal > 23) {
            // Regresa al inicio
            jugador.posicion = 0
            // Continúa avanzando con la cantidad restante
            posicionFinal = tirada - (24 - posicionJugador)
        } else {
            // Actualiza la posición del jugador
            jugador.posicion = posicionFinal
        }

        // Actualiza la posición del jugador en el objeto Jugador
        jugador.posicion = posicionFinal

        showToast("Número en el dado: $tirada")
        // Actualiza el texto de los botones en el tablero con los nombres de los jugadores
        actualizarTablero()
        //no funciona el cambio de turno si quitas alguno de los dos metodos, ya que uno actualiza la vista y otro el int del turno en el viewmodel
        viewModel.cambiarTurno()
        actualizarTurno()
    }


    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showAlert(message: String, pregunta: Pregunta) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Alerta")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { _, _ ->
            // Al pulsar en aceptar se abre el fragment correspondiente a la pregunta
            mostrarPregunta(pregunta)
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

    private fun mostrarPregunta(pregunta : Pregunta){
        when(pregunta){
            is Pregunta.AdivinaPalabra -> {
                val adivinarPalabraFragment = AdivinarPalabraFragment()
                val bundle = Bundle()
                bundle.putString("definicion", pregunta.definicion)
                bundle.putString("palabra", pregunta.palabra)
                adivinarPalabraFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, adivinarPalabraFragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }
            is Pregunta.JuegoParejas -> {
                val parejasFragment = parentFragmentManager.findFragmentByTag("parejasFragment")
                //llamar al fragmento de parejas y pasarle una pregunta recogida del json de firebase
                if (parejasFragment == null) {
                    val parejasFragment = ParejasFragment()
                    val bundle = Bundle()
                    bundle.putStringArray("parejas", pregunta.parejas.map { it.opcion1 }.toTypedArray())
                    parejasFragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, parejasFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }


            }
            is Pregunta.Repaso -> {
                val repasoFragment = RepasoFragment()
                val bundle = Bundle()
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
                val testFragment = TestFragment()
                val bundle = Bundle()
                bundle.putString("definicion", pregunta.enunciado)
                bundle.putStringArray("opciones", pregunta.opciones.toTypedArray())
                bundle.putString("respuesta", pregunta.respuestaCorrecta)
                testFragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, testFragment)
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

    private fun guardarPartida() {
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // Guardar datos de la partida
        editor.putInt("posicionJugador1", jugador1.posicion)
        editor.putInt("posicionJugador2", jugador2.posicion)
        editor.putInt("puntuacionJugador1", jugador1.puntuacion)
        editor.putInt("puntuacionJugador2", jugador2.puntuacion)
        editor.putInt("turno", viewModel.turno)

        // Aplicar los cambios
        editor.apply()
    }


    fun cargarPartida() {
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)

        // Cargar datos de la partida
        jugador1.posicion = prefs.getInt("posicionJugador1", 0)
        jugador2.posicion = prefs.getInt("posicionJugador2", 0)
        jugador1.puntuacion = prefs.getInt("puntuacionJugador1", 0)
        jugador2.puntuacion = prefs.getInt("puntuacionJugador2", 0)
        viewModel.turno = prefs.getInt("turno", 0)

        // Actualizar la vista
        actualizarTurno()
        actualizarPuntuacion()
        actualizarTablero()
    }

    private fun actualizarTablero() {
        // Actualiza el texto de los botones en el tablero con los nombres de los jugadores
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button
            if (button != null) {
                // Verifica si cualquiera de los jugadores está en esta posición y actualiza el texto del botón
                button.text = when {
                    i == jugador1.posicion && i == jugador2.posicion -> "J1/J2"
                    i == jugador1.posicion -> "J1"
                    i == jugador2.posicion -> "J2"
                    else -> ""
                }
            }
        }
    }
}
