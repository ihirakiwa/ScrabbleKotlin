import android.util.Log
import com.example.scrabble.Letter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WordGameViewModel {
    companion object {
        private const val TILES_PER_USER = 7
    }

    private val _uiState = MutableStateFlow(WordGameState())
    val uiState = _uiState.asStateFlow()

    fun startNewGame(playerOneName: String, playerTwoName: String, startPlayer: String) {
        val initialTiles = getInitialTiles()

        _uiState.value = WordGameState(
            playerOneData = PlayerData(
                name = playerOneName,
                tiles = getUserTiles(initialTiles)
            ),
            playerTwoData = PlayerData(
                name = playerTwoName,
                tiles = getUserTiles(initialTiles)
            ),
            currentTurnPlayer = startPlayer,
            gameStatus = GameStatus.STARTED,
            remainingTiles = initialTiles
        )
    }

    fun setShowUserTiles(showUserTiles: Boolean) {
        _uiState.update { it.copy(showUserTiles = showUserTiles) }
    }

    fun nextTurn() {
        val currentPlayer = uiState.value.currentTurnPlayer
        val nextPlayer = if (currentPlayer == uiState.value.playerOneData.name) {
            uiState.value.playerTwoData.name
        } else {
            uiState.value.playerOneData.name
        }
        setShowUserTiles(false)
        setUserTiles(currentPlayer, uiState.value.remainingTiles)

        _uiState.update {
            it.copy(
                currentTurnPlayer = nextPlayer
            )
        }
    }

    private fun getUserTiles(remainingTiles: MutableList<Letter>): List<Letter> {
        val userTiles = mutableListOf<Letter>()
        while (userTiles.size < TILES_PER_USER && remainingTiles.isNotEmpty()) {
            val randomTile = remainingTiles.random()
            remainingTiles.remove(randomTile)
            userTiles.add(randomTile)
        }
        return userTiles
    }

    private fun getInitialTiles() =
        Letter.values().flatMap { tile -> List(tile.frequency) { tile } }.toMutableList()

    private fun setUserTiles(playerName: String, remainingTiles: MutableList<Letter>) {
        val list = getUserTiles(remainingTiles)
        _uiState.update {
            when (playerName) {
                it.playerOneData.name -> it.copy(playerOneData = it.playerOneData.copy(tiles = getUserTiles(remainingTiles)))
                it.playerTwoData.name -> it.copy(playerTwoData = it.playerTwoData.copy(tiles = getUserTiles(remainingTiles)))
                else -> throw IllegalStateException("Invalid player: $playerName")
            }
        }
    }
}