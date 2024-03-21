package model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.scrabble.GRID
import com.example.scrabble.Letter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlacedTile(val tile: Letter, val isSubmitted: Boolean)

data class GridState(
    val adjacentTilesCount: Int = 0,
    val placedTileCount: Int = 0,
    val isSubmitEnabled: Boolean = false
)
class GridViewModel {

    private val alreadyPlaced = mutableListOf<Pair<Int, Int>>()
    private val placing = mutableListOf<Pair<Int, Int>>()
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
        placing.add(Pair(row, column))
        val newWords = getNewWords()
        _gridState.update { it.copy(placedTileCount = it.placedTileCount + 1)}
        val isValidConfiguration = validateConfiguration()

        _gridState.update {
            it.copy(
                isSubmitEnabled = newWords.isEmpty() && isValidConfiguration
            )
        }
    }

    fun removeTile(row: Int, column: Int) {
        validateCoordinates(row, column)

        placedTiles[row][column].value = null
        placing.remove(Pair(row, column))
        val newWords = getNewWords()

        _gridState.update { it.copy(placedTileCount = it.placedTileCount - 1)}

        val isValidConfiguration = validateConfiguration()

        _gridState.update {
            it.copy(
                isSubmitEnabled = newWords.isEmpty() && isValidConfiguration
            )
        }
    }

    private fun getNewWords() = emptyList<String>() //TODO: Implement this

    private fun validateConfiguration(): Boolean {
        if ((placedTiles[7][7].value == null) || _gridState.value.placedTileCount < 2) {
            return false
        }

        val isHorizontal = placing.all { it.first == placing.first().first }
        val isVertical = placing.all { it.second == placing.first().second }

        if (!isHorizontal && !isVertical) {
            return false
        }
        if(isHorizontal){
            if (!row()){
                return false
            }
        }
        if(isVertical){
            if (!column()){
                return false
            }
        }
        return true
    }


    private fun row() : Boolean{
        val placing_order = placing.sortedBy { it.second }
        val number_of_tiles = placing_order.last().second - placing_order.first().second + 1
        for (i in 0 until number_of_tiles){
            if (placedTiles[placing_order.first().first][placing_order.first().second + i].value == null){
                return false
            }
        }
        return true
    }

    private fun column() : Boolean{
        val placing_order = placing.sortedBy { it.first }
        val number_of_tiles = placing_order.last().first - placing_order.first().first + 1
        for (i in 0 until number_of_tiles){
            if (placedTiles[placing_order.first().first + i][placing_order.first().second].value == null){
                return false
            }
        }
        return true
    }


    private fun validateCoordinates(row: Int, column: Int) {
        val rowMax = placedTiles.size - 1
        // Assume there is at least 1 row
        val columnMax = placedTiles[0].size - 1
        if (row < 0 || row > rowMax || column < 0 || column > columnMax) {
            throw IllegalArgumentException("Tile coordinates must be between [0,0] and [$rowMax,$columnMax$]")
        }
    }
}