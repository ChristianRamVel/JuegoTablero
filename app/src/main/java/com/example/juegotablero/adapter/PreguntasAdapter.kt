import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

class PreguntasAdapter(private val context: Context) {
    fun cargarPreguntasDesdeJSON(fileName: String): List<Pregunta> {
        val gson = Gson()

        // Abre el archivo JSON desde assets
        context.assets.open(fileName).use { inputStream ->
            // Utiliza un InputStreamReader para leer el contenido del archivo JSON
            InputStreamReader(inputStream).use { reader ->
                // Deserializa el JSON a una lista de objetos Pregunta
                return gson.fromJson(reader, List::class.java)
            }
        }
    }
}

class Pregunta {

}
