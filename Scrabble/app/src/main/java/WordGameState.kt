import com.example.scrabble.Letter
data class WordGameState(
    val gameStatus: GameStatus = GameStatus.NOT_STARTED,
    val playerOneData: PlayerData = PlayerData(),
    val playerTwoData: PlayerData = PlayerData(),
    val currentTurnPlayer: String = "",
    val remainingTiles: MutableList<Letter> = mutableListOf(),
    val showUserTiles: Boolean = false
) {
    val currentTurnPlayerTiles: List<Letter>
        get() = when (currentTurnPlayer) {
            playerOneData.name -> playerOneData.tiles
            playerTwoData.name -> playerTwoData.tiles
            else -> throw IllegalStateException("Invalid player: $currentTurnPlayer")
        }

    private lateinit var _remainingTileCounts: Map<Letter, Int>
    val remainingTileCounts: Map<Letter, Int>
        get() {
            if (!this::_remainingTileCounts.isInitialized) {
                _remainingTileCounts = remainingTiles.groupingBy { it }.eachCount()
            }
            return _remainingTileCounts
        }

    private lateinit var _remainingTileTypes: List<Letter>
    val remainingTileTypes: List<Letter>
        get() {
            if (!this::_remainingTileTypes.isInitialized) {
                _remainingTileTypes = Letter.values().filter { remainingTileCounts[it] != null }
            }
            return _remainingTileTypes
        }
}