import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.example.scrabble.Letter
import com.example.scrabble.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

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

    fun setScore(score: Int) {
        _uiState.update {
            if (it.playerOneData.name == getCurrentTurnPlayer()) {
                it.copy(playerOneData = it.playerOneData.copy(score = score + it.playerOneData.score))
            } else {
                it.copy(playerTwoData = it.playerTwoData.copy(score = score+ it.playerTwoData.score))
            }
        }
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

    private fun getCurrentTurnPlayer(): String {
        return uiState.value.currentTurnPlayer
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


    fun downloadPDF(context: Context) {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.reglementscrabble)

            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val outputFile = File(directory, "reglementscrabbleeee.pdf")

            copyInputStreamToFile(inputStream, outputFile)

            Toast.makeText(context, "PDF téléchargé dans Téléchargements", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        val outputStream = FileOutputStream(file)
        try {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } finally {
            outputStream.close()
            inputStream.close()
        }
    }


}