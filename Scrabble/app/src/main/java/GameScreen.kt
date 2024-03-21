
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrabble.GridSection
import com.example.scrabble.Letter
import com.example.scrabble.PlayerScoresSection
import model.GridViewModel


val LocalTileDragContext = compositionLocalOf { DragContext<Letter>() }
@Composable
private fun gridViewModel() = remember { GridViewModel() }
@Composable
fun GameScreen(
    wordGameViewModel: WordGameViewModel,
    wordGameState: WordGameState,
    gridViewModel: GridViewModel = gridViewModel(),
    modifier: Modifier = Modifier
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
            Spacer(Modifier.size(50.dp))
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
                nextTurn = wordGameViewModel::nextTurn
            )
            Spacer(Modifier.size(20.dp))
        }
    }
}

