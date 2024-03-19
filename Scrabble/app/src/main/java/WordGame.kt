import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.scrabble.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


@Composable
fun WordGame(viewModel: WordGameViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val wordsArray = loadWordsAsArray(context)
    for(i in 0 until 5){
        //Log.d("WordGame", "Word: ${wordsArray?.get(i)}")
    }
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
            if (wordsArray != null) {
                (if (wordsArray.size == context.resources.openRawResource(R.raw.wordlist).bufferedReader().readLines().size) {
                    GameScreen(viewModel, uiState)
                } else {
                    Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                })
            }
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