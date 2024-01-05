import androidx.lifecycle.ViewModel

class ParejasViewModel : ViewModel() {

    private val wordPairs = mutableListOf<Pair<String, String>>()
    private val aciertos = mutableSetOf<Pair<String, String>>()


    // Agrega una nueva palabra y su clase a la lista de parejas
    fun addWordPair(animal: String, claseAnimal: String) {
        val newWordPair = Pair(animal, claseAnimal)
        wordPairs.add(newWordPair)
    }


    // Verifica si la palabra seleccionada coincide con su pareja
    fun isMatch(pareja1: String, pareja2: String): Boolean {
        // Verificar si la pareja ya ha sido acertada
        val pareja = pareja1 to pareja2
        val alreadyMatched = aciertos.contains(pareja)
        if (alreadyMatched) {
            return false  // Ya ha sido acertada, no permitir otra vez
        }

        val correctType = wordPairs.find { it.first == pareja1 }?.second
        val match = correctType == pareja2

        if (match) {
            // Incrementar el contador de aciertos
            aciertos.add(pareja)
        }

        return match
    }

    fun getMatchForWord1(word: String): String? {
        return wordPairs.find { it.first == word }?.second
    }

    fun getMatchForWord2(word: String): String? {
        return wordPairs.find { it.second == word }?.first
    }

    fun getPair1(): List<String> {
        return wordPairs.map { it.first }
    }

    fun getPair2(): List<String> {
        return wordPairs.map { it.second }
    }

    fun getAciertos(): Int {
        return aciertos.size
    }

    fun getWordPairs(): List<Pair<String, String>> {
        return wordPairs
    }
}
