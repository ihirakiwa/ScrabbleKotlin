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
    val placedTileCount: Int = 0,
    val isSubmitEnabled: Boolean = false
)

class GridViewModel {

    private val alreadyPlaced = mutableListOf<Pair<Int, Int>>()
    private val placing = mutableListOf<Pair<Int, Int>>()
    private val _gridState = MutableStateFlow(GridState())
    val gridState = _gridState.asStateFlow()

    private val placedTiles = List<List<MutableState<PlacedTile?>>>(GRID.size) {
        MutableList(GRID.size) {
            mutableStateOf(null)
        }
    }

    fun getTile(row: Int, column: Int) = placedTiles[row][column].value

    fun getPlacing() = placing

    fun setPlacingEmpty() {
        placing.clear()
    }

    fun setAlreadyPlaced(list: List<Pair<Int, Int>>) {
        alreadyPlaced.addAll(list)
    }

    fun setTile(tile: Letter, row: Int, column: Int) {
        validateCoordinates(row, column)

        if (placedTiles[row][column].value?.isSubmitted != true) {
            placedTiles[row][column].value = PlacedTile(tile, false)
            placing.add(Pair(row, column))
            val newWords = getNewWords()
            _gridState.update { it.copy(placedTileCount = it.placedTileCount + 1)}

            val isValidConfiguration = validateConfiguration()

            _gridState.update {
                it.copy(
                    isSubmitEnabled = newWords.isNotEmpty() && isValidConfiguration
                )
            }
        }
    }

    fun removeTile(row: Int, column: Int) {
        validateCoordinates(row, column)

        if (placedTiles[row][column].value?.isSubmitted != true) {
            placedTiles[row][column].value = null
            placing.remove(Pair(row, column))
            val newWords = getNewWords()

            _gridState.update { it.copy(placedTileCount = it.placedTileCount - 1)}

            val isValidConfiguration = validateConfiguration()

            _gridState.update {
                it.copy(
                    isSubmitEnabled = newWords.isNotEmpty() && isValidConfiguration
                )
            }
        }
    }

    fun submitWord(): Boolean {
        val newWords = getNewWords()
        if (newWords.isEmpty()) {
            return false
        }
        for (word in newWords) {
            /*if(!wordList.contains(word)){
                return false
            }*/
        }
        return true
    }

    fun setTileSubmitted(list: List<Pair<Int, Int>>) {
        list.forEach { (row, column) ->
            val currentPlacedTile = placedTiles[row][column].value
            if (currentPlacedTile != null) {
                val newPlacedTile = currentPlacedTile.copy(isSubmitted = true)
                placedTiles[row][column].value = newPlacedTile
            }
        }
    }

    //TOOLS

    private fun getNewWords() : List<String> {
        val newWords = mutableListOf<String>()
        val isHorizontal = placing.all { it.first == placing.first().first }
        val isVertical = placing.all { it.second == placing.first().second }
        if (isHorizontal) {
            newWords.addAll(getNewWordInRow())
            //newWords.addAll(getNewWordsInColumn())
        }
        if (isVertical) {
            newWords.addAll(getNewWordInColumn())
            //newWords.addAll(getNewWordsInRow())
        }
        return newWords
    }

    private fun getNewWordInRow(): List<String> {
        val rowWords = mutableListOf<String>()
        if (placing.isEmpty()){
            return emptyList()
        }
        val row = placing.first().first
        val rowTiles = placedTiles[row]

        val allColumns = (alreadyPlaced + placing).map { it.second }

        val allPlacingColumnsPresent = placing.all { it.second in allColumns }

        if (!allPlacingColumnsPresent) {
            return emptyList()
        }
        val placingColumns = placing.map { it.second }
        var currentWord = ""
        for (col in 0 until GRID.size) {
            val tile = rowTiles[col].value
            if (tile != null) {
                if (col in placingColumns) {
                    currentWord += tile.tile.toString()
                }
                else if (col in allColumns) {
                    currentWord += tile.tile.toString()
                } else {
                    if (currentWord.isNotEmpty()) {
                        rowWords.add(currentWord)
                        currentWord = ""
                    }
                }
            }
        }
        // Ajoute le mot final si nécessaire
        if (currentWord.isNotEmpty()) {
            rowWords.add(currentWord)
        }
        return rowWords
    }

    private fun getNewWordInColumn(): List<String> {
        val columnWords = mutableListOf<String>()
        if (placing.isEmpty()){
            return emptyList()
        }
        val column = placing.first().second
        val columnTiles = (0 until GRID.size).map { row -> placedTiles[row][column].value }
        val allRows = (alreadyPlaced + placing).map { it.first }
        val allPlacingRowsPresent = placing.all { it.first in allRows }
        if (!allPlacingRowsPresent) {
            return emptyList()
        }
        val placingRows = placing.map { it.first }
        var currentWord = ""
        for (row in 0 until GRID.size) {
            val tile = columnTiles[row]
            if (tile != null) {
                if (row in placingRows) {
                    currentWord += tile.tile.toString()
                }
                else if (row in allRows) {
                    currentWord += tile.tile.toString()
                } else {
                    if (currentWord.isNotEmpty()) {
                        columnWords.add(currentWord)
                        currentWord = ""
                    }
                }
            }
        }
        if (currentWord.isNotEmpty()) {
            columnWords.add(currentWord)
        }
        return columnWords
    }



    private fun validateConfiguration(): Boolean {
        if ((placedTiles[7][7].value == null) || _gridState.value.placedTileCount < 2) {
            return false
        }
        if(placing.size == 1){
            if(!checkSurronding(placing.first().first,placing.first().second)){
                return false
            }
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
        if (placing.isEmpty()){
            return false
        }
        val placingOrder = placing.sortedBy { it.second }
        val numberOfTiles = placingOrder.last().second - placingOrder.first().second + 1
        for (i in 0 until numberOfTiles){
            if (placedTiles[placingOrder.first().first][placingOrder.first().second + i].value == null){
                return false
            }
            if(alreadyPlaced.contains(Pair(placingOrder.first().first,placingOrder.first().second + i))){
                if(!checkSurronding(placingOrder.first().first,placingOrder.first().second + i)){
                    return false
                }
            }

        }
        return true
    }


    private fun column() : Boolean{
        if (placing.isEmpty()){
            return false
        }
        val placingOrder = placing.sortedBy { it.first }
        val numberOfTiles = placingOrder.last().first - placingOrder.first().first + 1
        for (i in 0 until numberOfTiles){
            if (placedTiles[placingOrder.first().first + i][placingOrder.first().second].value == null){
                return false
            }
            if(alreadyPlaced.contains(Pair(placingOrder.first().first + i,placingOrder.first().second))){
                if(!checkSurronding(placingOrder.first().first + i,placingOrder.first().second)){
                    return false
                }
            }
        }
        return true
    }
    private fun checkSurronding(row:Int, column: Int) : Boolean{
        val neighbors = listOf(
            Pair(row - 1, column),
            Pair(row + 1, column),
            Pair(row, column - 1),
            Pair(row, column + 1)
        )
        for ((neighborRow, neighborColumn) in neighbors) {
            if (alreadyPlaced.contains(Pair(neighborRow, neighborColumn))) {
                return true
            }
        }

        return false
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