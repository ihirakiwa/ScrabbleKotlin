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
    private val listRowColonne = mutableListOf<Pair<Int, Int>>()
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
        listRowColonne.add(Pair(row, column))
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
        listRowColonne.remove(Pair(row, column))
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

        val isHorizontal = listRowColonne.all { it.first == listRowColonne.first().first }
        val isVertical = listRowColonne.all { it.second == listRowColonne.first().second }

        if (!isHorizontal && !isVertical) {
            return false
        }

        // Vérifier si chaque mot formé est valide
        if (!areTilesAdjacent()) {
            return false
        }




        // Toutes les conditions sont satisfaites, la configuration est valide
        return true
    }

    private fun areTilesAdjacent(): Boolean {
        for ((row, column) in listRowColonne) {
            if (hasAdjacentTile(row, column) >= 2) {
                _gridState.update { it.copy(adjacentTilesCount = it.adjacentTilesCount + 1)}
            }
        }
        return _gridState.value.adjacentTilesCount >= listRowColonne.size - 2
    }

    private fun hasAdjacentTile(row: Int, column: Int): Int {
        // Compter le nombre de lettres adjacentes
        var adjacentCount = 0
        if (listRowColonne.contains(row - 1 to column)) {
            adjacentCount++
        }
        if (listRowColonne.contains(row + 1 to column)) {
            adjacentCount++
        }
        if (listRowColonne.contains(row to column - 1)) {
            adjacentCount++
        }
        if (listRowColonne.contains(row to column + 1)) {
            adjacentCount++
        }
        // Vérifier si le nombre de lettres adjacentes est suffisant
        return adjacentCount
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