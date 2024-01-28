import com.example.juegotablero.common.interfaces.CrearPreguntasCallback
import com.example.juegotablero.common.interfaces.ObtenerPreguntasCallback
import com.example.juegotablero.api.model.Pregunta
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener

class PreguntasApi {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun obtenerPreguntasRepaso(callback: ObtenerPreguntasCallback) {
        val repasoReference = databaseReference.child("repaso")
        repasoReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasRepaso = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.Repaso::class.java) }
                callback.onPreguntasObtenidas(preguntasRepaso)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }
    fun obtenerPreguntasAdivinaPalabra(callback: ObtenerPreguntasCallback) {
        val adivinaPalabraReference = databaseReference.child("adivina_palabra")
        adivinaPalabraReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasAdivinaPalabra = dataSnapshot.children.mapNotNull { it.getValue(
                    Pregunta.AdivinaPalabra::class.java) }
                callback.onPreguntasObtenidas(preguntasAdivinaPalabra)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

    fun obtenerPreguntasTest(callback: ObtenerPreguntasCallback) {
        val testReference = databaseReference.child("test")
        testReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasTest = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.Test::class.java) }
                callback.onPreguntasObtenidas(preguntasTest)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

    fun obtenerPreguntasJuegoParejas(callback: ObtenerPreguntasCallback) {
        val juegoParejasReference = databaseReference.child("juego_parejas")
        juegoParejasReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasJuegoParejas = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.JuegoParejas::class.java) }
                callback.onPreguntasObtenidas(preguntasJuegoParejas)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

    fun obtenerPreguntasPruebaFinal(callback: ObtenerPreguntasCallback) {
        val pruebaFinalReference = databaseReference.child("prueba_final")
        pruebaFinalReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasPruebaFinal = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.PruebaFinal::class.java) }
                callback.onPreguntasObtenidas(preguntasPruebaFinal)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

    fun agregarPregunta(categoria: String, pregunta: Pregunta, callback: CrearPreguntasCallback) {
        val categoriaReference = databaseReference.child(categoria)

        // Generar una nueva clave única para la pregunta
        val nuevaClave = categoriaReference.push().key

        if (nuevaClave != null) {
            // Establecer la nueva pregunta en la base de datos bajo la clave generada
            categoriaReference.child(nuevaClave).setValue(pregunta)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback.onPreguntaCreada()
                    } else {
                        callback.onError(task.exception)
                    }
                }
        }
    }
}