import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class WordHashMap(private val context: Context) {
    private val wordMap = HashMap<String, Int>()

    fun loadWordMap(fileName: String) {
        context.assets.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).useLines { lines ->
                lines.forEach { line ->
                    val (word, value) = line.split(" ")
                    wordMap[word] = value.toInt()
                }
            }
        }
    }

    fun getWordMap(): HashMap<String, Int> {
        return wordMap
    }

    fun isWordPresent(word: String): Boolean {
        return wordMap.containsKey(word)
    }
}
