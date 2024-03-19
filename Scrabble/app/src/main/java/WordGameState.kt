import com.example.scrabble.Letter
data class WordGameState(
    val gameStatus: GameStatus = GameStatus.NOT_STARTED,
    val playerOneData: PlayerData = PlayerData(),
    val playerTwoData: PlayerData = PlayerData(),
    val currentTurnPlayer: String = "",
    val showUserTiles: Boolean = false
) {
    val currentTurnPlayerTiles: List<Letter>
        get() = when (currentTurnPlayer) {
            playerOneData.name -> playerOneData.tiles
            playerTwoData.name -> playerTwoData.tiles
            else -> throw IllegalStateException("Invalid player: $currentTurnPlayer")
        }

}