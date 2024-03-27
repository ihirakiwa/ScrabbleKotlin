
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scrabble.Letter
import model.GridViewModel

@Composable
fun ControlsSection(wordGameViewModel: WordGameViewModel,
                    gridViewModel: GridViewModel,
                    resign: () -> Unit,
                    letter: (List<Pair<Int, Int>>) -> List<Letter>,
                    getPlacing: () -> List<Pair<Int, Int>>,
                    modifier: Modifier = Modifier) {
    val dragContext = LocalTileDragContext.current
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(BUTTON_SPACING),
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedButton(

                onClick = {resign() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
                modifier = Modifier.weight(1.2f)
            ) {
                    Text("Céder")
            }
            OutlinedButton(
                onClick = {
                    dragContext.resetDragTargets()
                    wordGameViewModel.nextTurn(gridViewModel.letterFromListIndex(gridViewModel.getPlacing())) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
                modifier = Modifier.weight(1.2f)
            ) {
                Text("Passer")
            }
            OutlinedButton(
                onClick = {wordGameViewModel.swapTiles(letter(getPlacing()))},
                colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
                modifier = Modifier.weight(1.2f)
            ) {
                Text("Changer")
            }
        }
    }
}

