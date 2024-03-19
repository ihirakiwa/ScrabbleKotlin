import android.content.Context
import com.example.scrabble.R
import java.io.IOException

fun loadWordsAsArray(context: Context): Array<String>? {
    val wordsList = mutableListOf<String>()
    try {
        val inputStream = context.resources.openRawResource(R.raw.wordlist)
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                wordsList.add(line.trim())
            }
        }
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return wordsList.toTypedArray()
}