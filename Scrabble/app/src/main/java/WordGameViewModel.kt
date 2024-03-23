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

    fun start2v2Game(){
        _uiState.update { it.copy(gameStatus = GameStatus.NOT_STARTED) }
    }

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

    fun nextTurn(list: List<Letter>) {
        val player1 = uiState.value.playerOneData
        val player2 = uiState.value.playerTwoData
        val currentPlayer = uiState.value.currentTurnPlayer
        val nextPlayer = if (currentPlayer == player1.name) {
            player2.name
        } else {
            player1.name
        }
        setShowUserTiles(false)

        if  (uiState.value.remainingTiles.size < TILES_PER_USER) { //TODO: Check condition d'arret
            _uiState.update {
                it.copy(
                    gameStatus = GameStatus.FINISHED,
                    currentTurnPlayer = nextPlayer
                )
            }
            return
        }

        val oldRack = mutableListOf<Letter>()
        if (currentPlayer == player1.name){
            player1.tiles.forEach {
                if (!list.contains(it)){
                    oldRack.add(it)
                }
            }
        } else {
            player2.tiles.forEach {
                if (!list.contains(it)){
                    oldRack.add(it)
                }
            }
        }

        // Distribuer de nouvelles lettres pour le prochain tour
        while (oldRack.size < TILES_PER_USER) {
            val randomTile = uiState.value.remainingTiles.random()
            uiState.value.remainingTiles.remove(randomTile)
            oldRack.add(randomTile)
        }



        _uiState.update {
            it.copy(
                currentTurnPlayer = nextPlayer,
                playerOneData = if (currentPlayer == it.playerOneData.name) {
                    it.playerOneData.copy(tiles = oldRack)
                } else {
                    it.playerOneData
                },
                playerTwoData = if (currentPlayer == it.playerTwoData.name) {
                    it.playerTwoData.copy(tiles = oldRack)
                } else {
                    it.playerTwoData
                }
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

    fun showRules() {
        TODO("Not yet implemented")
    }


}