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

class GridViewModel(private val wordList: HashMap<String, Int>) {
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

            _gridState.update { it.copy(placedTileCount = it.placedTileCount + 1) }

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

    fun letterFromListIndex(list: List<Pair<Int, Int>>): List<Letter> {
        val letters = mutableListOf<Letter>()
        for (pair in list) {
            letters.add(placedTiles[pair.first][pair.second].value!!.tile)
        }
        return letters
    }

    fun submitWord(): Boolean {
        val newWords = getNewWords()

        if (newWords.isEmpty()) {
            return false
        }
        for (word in newWords) {
            if(!wordList.contains(word.lowercase())){
                return false
            }
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

    fun setSubmitEnabled(isEnabled: Boolean) {
        _gridState.update { it.copy(isSubmitEnabled = isEnabled) }
    }

    fun getScore(): Int {
        /*var score = 0
        var wordMultiplier = 1

        // Parcourir les nouveaux mots formés
        val newWords = getNewWords()
        for (word in newWords) {
            if (word.length > 1) { // Ne pas compter les mots d'une seule lettre
                var wordScore = 0
                var wordMultiplierTemp = 1 // Multiplicateur de mot temporaire pour chaque mot

                for ((row, column) in placing) {
                    val placedTile = placedTiles[row][column].value
                    Log.d("placedTile", placedTile.toString())
                    if (placedTile != null && word.contains(placedTile.tile.toString(), ignoreCase = true)) {
                        val tileScore = placedTile.tile.score
                        Log.d("tileScore", tileScore.toString())

                        // Vérifier s'il y a une case multiplicatrice pour la lettre
                        val cellType = GRID[row][column]
                        Log.d("cellType", cellType.toString())
                        when (cellType) {
                            CellType.LD -> wordScore += tileScore * 2 // Lettre double
                            CellType.LT -> wordScore += tileScore * 3 // Lettre triple
                            CellType.MD -> {wordMultiplierTemp *= 2
                                wordScore += tileScore}// Mot double
                            CellType.MT -> {wordMultiplierTemp *= 3
                                wordScore += tileScore}// Mot triple
                            CellType.BL, CellType.ST -> wordScore += tileScore // Aucun multiplicateur
                        }
                    }
                }

                // Appliquer le score et le multiplicateur du mot
                score += wordScore
                Log.d("Score", score.toString())
                wordMultiplier *= wordMultiplierTemp
                Log.d("WordMultiplier", wordMultiplier.toString())
            }
        }

        // Appliquer le multiplicateur de mot global
        score *= wordMultiplier

        // Appliquer le bonus Scrabble si toutes les tuiles ont été utilisées
        if (placing.size == 7) {
            score += 50
        }

        */
        val score = 5
        return score
    }

    //TOOLS


    private fun getNewWords() : List<String> {
        val newWords = mutableListOf<String>()
        val isHorizontal = placing.all { it.first == placing.first().first }
        val isVertical = placing.all { it.second == placing.first().second }
        if (isHorizontal) {
            newWords.addAll(getNewWordInRow())
            newWords.addAll(getNewWordsInColumn())
        }
        if (isVertical) {
            newWords.addAll(getNewWordInColumn())
            newWords.addAll(getNewWordsInRow())
        }
        return newWords.filter { it.length > 1 }.distinct()
    }

    private fun getNewWordsInRow(): List<String> {
        val rowWords = mutableListOf<String>()
        if (placing.isEmpty()) {
            return emptyList()
        }
        val rowsWithPlacedLetters = placing.map { it.first }.distinct()
        for (row in rowsWithPlacedLetters) {
            val rowWordBuilder = StringBuilder()
            var wordStarted = false
            for (column in 0 until GRID.size) {
                val tile = placedTiles[row][column].value
                if (tile != null) {
                    rowWordBuilder.append(tile.tile)
                    wordStarted = true
                } else if (wordStarted) {
                    rowWords.add(rowWordBuilder.toString())
                    rowWordBuilder.clear()
                    wordStarted = false
                }
            }
            if (wordStarted) {
                rowWords.add(rowWordBuilder.toString())
            }
        }

        return rowWords
    }


    private fun getNewWordsInColumn(): List<String> {
        val columnWords = mutableListOf<String>()
        if (placing.isEmpty()) {
            return emptyList()
        }
        val columnsWithPlacedLetters = placing.map { it.second }.distinct()
        for (column in columnsWithPlacedLetters) {
            val columnWordBuilder = StringBuilder()
            var wordStarted = false
            for (row in 0 until GRID.size) {
                val tile = placedTiles[row][column].value
                if (tile != null) {
                    columnWordBuilder.append(tile.tile)
                    wordStarted = true
                } else if (wordStarted) {
                    columnWords.add(columnWordBuilder.toString())
                    columnWordBuilder.clear()
                    wordStarted = false
                }
            }
            if (wordStarted) {
                columnWords.add(columnWordBuilder.toString())
            }
        }
        return columnWords
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
            if(!checkSurrounding(placing.first().first,placing.first().second)){
                return false
            }
        }
        val isHorizontal = placing.all { it.first == placing.first().first }
        val isVertical = placing.all { it.second == placing.first().second }
        if (!isHorizontal && !isVertical) {
            return false
        }
        if (placing.isEmpty()){
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
        if (alreadyPlaced.isNotEmpty()) {
            if (!checkPlacing()){
                return false
            }
        }
        return true
    }


    private fun row() : Boolean{
        val placingOrder = placing.sortedBy { it.second }
        val numberOfTiles = placingOrder.last().second - placingOrder.first().second + 1
        for (i in 0 until numberOfTiles){
            if (placedTiles[placingOrder.first().first][placingOrder.first().second + i].value == null){
                return false
            }
            if(alreadyPlaced.contains(Pair(placingOrder.first().first,placingOrder.first().second + i))){
                if(!checkSurrounding(placingOrder.first().first,placingOrder.first().second + i)){
                    return false
                }
            }

        }
        return true
    }


    private fun column() : Boolean{
        val placingOrder = placing.sortedBy { it.first }
        val numberOfTiles = placingOrder.last().first - placingOrder.first().first + 1
        for (i in 0 until numberOfTiles){
            if (placedTiles[placingOrder.first().first + i][placingOrder.first().second].value == null){
                return false
            }
            if(alreadyPlaced.contains(Pair(placingOrder.first().first + i,placingOrder.first().second))){
                if(!checkSurrounding(placingOrder.first().first + i,placingOrder.first().second)){
                    return false
                }
            }
        }
        return true
    }
    private fun checkSurrounding(row:Int, column: Int) : Boolean{
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
    fun checkPlacing() : Boolean{
        var hasTrue = false

        for ((row, column) in placing) {
            if (checkSurrounding(row, column)) {
                hasTrue = true
                break
            }
        }

        return hasTrue
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
