
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrabble.Letter
import com.example.scrabble.PlayerScoresSection
import model.GridViewModel


val LocalTileDragContext = compositionLocalOf { DragContext<Letter>() }
@Composable
private fun gridViewModel(wordList:HashMap<String,Int>) = remember { GridViewModel(wordList) }
@Composable
fun GameScreen(
    wordGameViewModel: WordGameViewModel,
    wordGameState: WordGameState,
    wordList: HashMap<String, Int>,
    gridViewModel: GridViewModel = gridViewModel(wordList),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val gridState by gridViewModel.gridState.collectAsState()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            color = Color(66,75,90)
        )) {
        Column(modifier.padding(16.dp, 40.dp, 16.dp, 5.dp)) {
            PlayerScoresSection(
                playerOneData = wordGameState.playerOneData,
                playerTwoData = wordGameState.playerTwoData,
                currentTurnPlayer = wordGameState.currentTurnPlayer
            )
            Spacer(Modifier.size(40.dp))
            GridSection(
                onGetTile = remember { gridViewModel::getTile },
                onSetTile = remember { gridViewModel::setTile },
                onRemoveTile = remember { gridViewModel::removeTile }
            )
            Spacer(Modifier.size(20.dp))
            PlayerTilesSection(
                tiles = wordGameState.currentTurnPlayerTiles,
                tileVisibility = wordGameState.showUserTiles,
                onTileVisibilityChanged = wordGameViewModel::setShowUserTiles,
                isSubmitEnabled = gridState.isSubmitEnabled,
                getPlacing = gridViewModel::getPlacing,
                setPlacingEmpty = gridViewModel::setPlacingEmpty,
                setAlreadyPlaced = gridViewModel::setAlreadyPlaced,
                setTileSubmitted = gridViewModel::setTileSubmitted,
                onSubmit = gridViewModel::submitWord,
                nextTurn = wordGameViewModel::nextTurn,
                setSubmitEnabled = gridViewModel::setSubmitEnabled,
                letter = gridViewModel::letterFromListIndex,
                setScore = wordGameViewModel::setScore,
                getScore = gridViewModel::getScore
            )
            Spacer(Modifier.size(20.dp))
            ControlsSection()
        }
    }
}

