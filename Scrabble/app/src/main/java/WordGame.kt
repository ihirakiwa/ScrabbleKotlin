import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.scrabble.R


@Composable
fun WordGame(viewModel: WordGameViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun createWordListFromFile(context: Context): HashMap<String, Int> {
        val wordList = HashMap<String, Int>()

        val inputStream = context.resources.openRawResource(R.raw.wordlist)
        inputStream.reader().useLines { lines ->
            lines.forEach { line ->
                val (word, countStr) = line.split(" ", limit = 2) // Sépare la ligne en deux parties (mot et compteur)
                val count = countStr.trim().toInt() // Convertit la chaîne représentant un entier en Int
                wordList[word.trim()] = count // Ajoute le mot et son compteur à la HashMap
            }
        }

        return wordList
    }
    val wordList = createWordListFromFile(context)

    when (uiState.gameStatus) {
        GameStatus.NOT_STARTED -> {
            LandingScreen(
                onSubmitPlayerNames = { playerOneName, playerTwoName ->
                    viewModel.startNewGame(
                        playerOneName = playerOneName,
                        playerTwoName = playerTwoName,
                        startPlayer = playerOneName
                    )
                }
            )
        }

        GameStatus.STARTED -> {
            GameScreen(viewModel, uiState, wordList)
            /*if (wordsArray != null) {
                (if (wordsArray.size == context.resources.openRawResource(R.raw.wordlist).bufferedReader().readLines().size) {
                    GameScreen(viewModel, uiState)
                } else {
                    Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                })
            }*/
        }

        GameStatus.FINISHED -> {
            SummaryScreen(
                playerOneData = uiState.playerOneData,
                playerTwoData = uiState.playerTwoData
            )
        }
    }
}

@Composable
private fun viewModel(): WordGameViewModel = remember { WordGameViewModel() }