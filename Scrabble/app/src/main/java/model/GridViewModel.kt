package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.scrabble.GRID
import com.example.scrabble.Letter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlacedTile(val tile: Letter, val isSubmitted: Boolean)

data class GridState(
    val placedTileCount: Int = 0,
    val isSubmitEnabled: Boolean = false
)
class GridViewModel {


    private val _gridState = MutableStateFlow(GridState())
    val gridState = _gridState.asStateFlow()

    // Keeping this state separate from GridState as we want to avoid copying this 2D array repeatedly
    // every time a tile is placed or removed
    private val placedTiles = List<List<MutableState<PlacedTile?>>>(GRID.size) {
        MutableList(GRID.size) {
            mutableStateOf(null)
        }
    }

    fun getTile(row: Int, column: Int) = placedTiles[row][column].value

    fun setTile(tile: Letter, row: Int, column: Int) {
        validateCoordinates(row, column)

        placedTiles[row][column].value = PlacedTile(tile, false)

        val newWords = getNewWords()
        val isValidConfiguration = validateConfiguration()

        _gridState.update {
            it.copy(
                placedTileCount = it.placedTileCount + 1,
                isSubmitEnabled = newWords.isNotEmpty() && isValidConfiguration
            )
        }
    }

    fun removeTile(row: Int, column: Int) {
        validateCoordinates(row, column)

        placedTiles[row][column].value = null

        val newWords = getNewWords()
        val isValidConfiguration = validateConfiguration()

        _gridState.update {
            it.copy(
                placedTileCount = it.placedTileCount - 1,
                isSubmitEnabled = newWords.isNotEmpty() && isValidConfiguration
            )
        }
    }

    private fun getNewWords() = emptyList<String>() //TODO: Implement this

    private fun validateConfiguration() = true //TODO: Implement this

    private fun validateCoordinates(row: Int, column: Int) {
        val rowMax = placedTiles.size - 1
        // Assume there is at least 1 row
        val columnMax = placedTiles[0].size - 1
        if (row < 0 || row > rowMax || column < 0 || column > columnMax) {
            throw IllegalArgumentException("Tile coordinates must be between [0,0] and [$rowMax,$columnMax$]")
        }
    }
}