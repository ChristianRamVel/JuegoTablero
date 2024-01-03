import android.util.Log
import com.example.juegotablero.api.PreguntasCallback
import com.example.juegotablero.model.Pregunta
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener

class PreguntasApi {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun obtenerPreguntasRepaso(callback: PreguntasCallback) {
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
    fun obtenerPreguntasAdivinaPalabra(callback: PreguntasCallback) {
        val adivinaPalabraReference = databaseReference.child("adivina_palabra")
        adivinaPalabraReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val preguntasAdivinaPalabra = dataSnapshot.children.mapNotNull { it.getValue(Pregunta.AdivinaPalabra::class.java) }
                callback.onPreguntasObtenidas(preguntasAdivinaPalabra)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

    fun obtenerPreguntasTest(callback: PreguntasCallback) {
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

    fun obtenerPreguntasJuegoParejas(callback: PreguntasCallback) {
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

    fun obtenerPreguntasPruebaFinal(callback: PreguntasCallback) {
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
}