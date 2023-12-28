import android.util.Log
import com.example.juegotablero.model.Pregunta
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener

class PreguntasApi {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var preguntasRepaso: List<Pregunta.Repaso> = emptyList()
    private var preguntasAdivinaPalabra: List<Pregunta.AdivinaPalabra> = emptyList()
    private var preguntasTest: List<Pregunta.Test> = emptyList()
    private var preguntasJuegoParejas: List<Pregunta.JuegoParejas> = emptyList()
    private var preguntasPruebaFinal: List<Pregunta.PruebaFinal> = emptyList()

    init {
        obtenerPreguntasRepaso()
        obtenerPreguntasAdivinaPalabra()

    }

    private fun obtenerPreguntasRepaso() {
        val repasoReference = databaseReference.child("repaso")
        repasoReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preguntasRepaso = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.Repaso::class.java) }
                mostrarPreguntasRepaso()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores de lectura de la base de datos

            }
        })
    }
    private fun obtenerPreguntasAdivinaPalabra() {
        val adivinaPalabraReference = databaseReference.child("adivina_palabra")
        adivinaPalabraReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preguntasAdivinaPalabra = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.AdivinaPalabra::class.java) }
                mostrarPreguntasAdivinaPalabra()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores de lectura de la base de datos
            }
        })
    }
    private fun obtenerPreguntasTest() {
        val testReference = databaseReference.child("test")
        testReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preguntasTest = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.Test::class.java) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores de lectura de la base de datos
            }
        })
    }

    private fun obtenerPreguntasJuegoParejas() {
        val juegoParejasReference = databaseReference.child("juego_parejas")
        juegoParejasReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preguntasJuegoParejas = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.JuegoParejas::class.java) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores de lectura de la base de datos
            }
        })
    }

    private fun obtenerPreguntasPruebaFinal() {
        val pruebaFinalReference = databaseReference.child("prueba_final")
        pruebaFinalReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preguntasPruebaFinal = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.PruebaFinal::class.java) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores de lectura de la base de datos
            }
        })
    }

    private fun mostrarPreguntasRepaso() {
        for (pregunta in preguntasRepaso) {
            Log.d("Pregunta", "Repaso: ${pregunta.enunciado}")
        }
    }

    private fun mostrarPreguntasAdivinaPalabra() {
        for (pregunta in preguntasAdivinaPalabra) {
            Log.d("Pregunta", "Adivina palabra: ${pregunta.definicion}")
        }
    }


}