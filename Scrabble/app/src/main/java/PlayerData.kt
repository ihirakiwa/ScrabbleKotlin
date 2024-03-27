import com.example.scrabble.Letter
data class PlayerData(
    val name: String = "",
    val score: Int = 0,
    val tiles: List<Letter> = emptyList()
)