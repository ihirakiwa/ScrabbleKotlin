package model


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.scrabble.CellType
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
    private var score = 0
    private var wordMultiplier = 1
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
        var jokerLetter: Char? = null
        for (word in newWords) {
            val jokerIndex = word.indexOf('_')
            if (jokerIndex == -1) {
                if (!wordList.contains(word.lowercase())) {
                    return false
                }
            } else {
                if (jokerLetter == null) {
                    for (char in 'A'..'Z') {
                        val replacedWord = word.substring(0, jokerIndex) + char + word.substring(jokerIndex + 1)
                        if (wordList.contains(replacedWord.lowercase())) {
                            jokerLetter = char
                            Log.d("jokerLetter", jokerLetter.toString())
                            break
                        }
                    }
                    if (jokerLetter == null) {
                        return false
                    }
                } else {
                    val replacedWord = word.substring(0, jokerIndex) + jokerLetter + word.substring(jokerIndex + 1)
                    Log.d("replacedWord", replacedWord)
                    if (!wordList.contains(replacedWord.lowercase())) {
                        return false
                    }
                }
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
        score *= wordMultiplier

        // Appliquer le bonus Scrabble si toutes les tuiles ont été utilisées
        if (placing.size == 7) {
            score += 50
        }


        return score
    }

    //TOOLS


        private fun getNewWords() : List<String> {
            val newWords = mutableListOf<String>()
            val isHorizontal = placing.all { it.first == placing.first().first }
            val isVertical = placing.all { it.second == placing.first().second }
            var scoreInte = 0
            var multi = 1
            val pairRowScore: Pair<List<String>,Int>
            val pairColScore: Pair<List<String>,Int>
            if (isHorizontal && isVertical) {
                pairRowScore = getNewWordInRow(-1)
                multi = wordMultiplier
                scoreInte += pairRowScore.second
                newWords.addAll(pairRowScore.first)
                pairColScore = getNewWordInColumn(-1)
                newWords.addAll(pairColScore.first)
                if (newWords.filter { it.length > 1 }.distinct().size > 1){
                    scoreInte += pairColScore.second
                }
            }else {
                if (isHorizontal) {
                    pairRowScore  = getNewWordInRow(-1)
                    multi = wordMultiplier
                    Log.d("ligne168", pairRowScore.toString())
                    scoreInte += pairRowScore.second
                    newWords.addAll(pairRowScore.first)
                    val pairColScores = getNewWordsInColumn()
                    multi *= wordMultiplier
                    Log.d("ligne169", pairColScores.toString())
                    newWords.addAll(pairColScores.first)
                    scoreInte += pairColScores.second
                }
                if (isVertical) {
                    pairColScore = getNewWordInColumn(-1)
                    multi = wordMultiplier
                    Log.d("ligne169", pairColScore.toString())
                    newWords.addAll(pairColScore.first)
                    scoreInte += pairColScore.second
                    val pairRowScores = getNewWordsInRow()
                    multi *= wordMultiplier
                    Log.d("ligne168", pairRowScores.toString())
                    newWords.addAll(pairRowScores.first)
                    scoreInte += pairRowScores.second
                }
            }
            wordMultiplier = multi
            score = scoreInte
            Log.d("ligne170", score.toString())
            val test = newWords.filter { it.length > 1 }.distinct()
            Log.d("ligne179", test.toString())
            return test
        }

        private fun getNewWordsInRow(): Pair<List<String>,Int> {
            val rowWords = mutableListOf<String>()
            if (placing.isEmpty()) {
                return Pair(emptyList(),0)
            }
            var scoreInte = 0
            val rowsWithPlacedLetters = placing.map { it.first }.distinct()
            Log.d("ligne200", rowsWithPlacedLetters.toString())
            for (row in rowsWithPlacedLetters) {
                val pairRowScore = getNewWordInRow(row)
                Log.d("ligne201", pairRowScore.toString())
                if (pairRowScore.first[0].length > 1){
                    scoreInte += pairRowScore.second
                    rowWords.addAll(pairRowScore.first)
                    Log.d("ScoreWords", scoreInte.toString())
                }
            }
            return Pair(rowWords.filter { it.length > 1 }, scoreInte)
        }

        private fun getNewWordsInColumn(): Pair<List<String>,Int> {
            val columnWords = mutableListOf<String>()
            if (placing.isEmpty()) {
                return Pair(emptyList(),0)
            }
            var scoreInte = 0
            val columnsWithPlacedLetters = placing.map { it.second }.distinct()
            Log.d("ligne200", columnsWithPlacedLetters.toString())
            for (column in columnsWithPlacedLetters) {
                val pairColScore = getNewWordInColumn(column)
                Log.d("Col", pairColScore.toString())
                if (pairColScore.first[0].length > 1){
                    scoreInte += pairColScore.second
                    columnWords.addAll(pairColScore.first)
                    Log.d("ScoreWords", scoreInte.toString())
                }
            }
            return Pair(columnWords.filter { it.length > 1 }, scoreInte)
        }

        private fun getNewWordInRow(rowInd: Int): Pair<List<String>,Int>{
            if (placing.isEmpty()) {
                return Pair(emptyList(),0)
            }
            var row = rowInd
            if (row < 0){
                row = placing.first().first
            }
            val rowTiles = placedTiles[row]

            var firstTileIndex = rowTiles.indexOfFirst { (it.value != null) }
            if (firstTileIndex == -1) {
                return Pair(emptyList(),0)
            }
            while (!placing.contains(Pair(row, firstTileIndex))) {
                firstTileIndex += 1
                if (firstTileIndex >= GRID.size) {
                    return Pair(emptyList(),0)
                }
            }
            return getWordAtPositionRow(row, firstTileIndex)
        }

        private fun getWordAtPositionRow(row: Int, column: Int): Pair<List<String>,Int> {
            val tile = placedTiles[row][column].value?.tile ?: return Pair(emptyList(),0)
            score = 0
            wordMultiplier = 1
            if (placedTiles[row][column].value!!.isSubmitted){
                score += tile.score
            }else{
                scoreAdd(row, column, tile)
            }

            val wordToLeft = getWordToLeft(row, column - 1)
            val wordToRight = getWordToRight(row, column + 1)
            val list = listOf(wordToLeft, if(tile == Letter.BLANK) "_" else tile.toString(), wordToRight).filter { it.isNotEmpty() }

            return Pair(listOf(list.joinToString("")),score)
        }

        private fun getWordToLeft(row: Int, column: Int): String {
            return if (column < 0 || placedTiles[row][column].value == null) {
                ""
            } else {
                val tile = placedTiles[row][column].value!!.tile
                if (placedTiles[row][column].value!!.isSubmitted){
                    score += tile.score
                }else{
                    scoreAdd(row, column, tile)
                }
                getWordToLeft(row, column - 1) + (if(tile == Letter.BLANK) "_" else tile.toString())
            }
        }

        private fun getWordToRight(row: Int, column: Int): String {
            val rowTiles = placedTiles[row]
            return if (column >= GRID.size || rowTiles[column].value == null) {
                ""
            } else {
                val tile = rowTiles[column].value!!.tile
                if (placedTiles[row][column].value!!.isSubmitted){
                    score += tile.score
                }else{
                    scoreAdd(row, column, tile)
                }
                (if(tile == Letter.BLANK) "_" else tile.toString()) + getWordToRight(row, column + 1)
            }
        }

        private fun getNewWordInColumn(columnInd: Int): Pair<List<String>, Int> {
            if (placing.isEmpty()) {
                return Pair(emptyList(),0)
            }
            var column = columnInd
            if (column < 0){
                column = placing.first().second
            }

            var firstTileIndex = (0 until GRID.size).indexOfFirst { row -> placedTiles[row][column].value != null }
            if (firstTileIndex == -1) {
                return Pair(emptyList(),0)
            }
            while (!placing.contains(Pair(firstTileIndex, column))) {
                firstTileIndex += 1
                if (firstTileIndex >= GRID.size) {
                    return Pair(emptyList(),0)
                }
            }

            return getWordAtPositionCol(firstTileIndex, column)
        }

        private fun getWordAtPositionCol(row: Int, column: Int): Pair<List<String>,Int>{
            val tile = placedTiles[row][column].value?.tile ?: return Pair(emptyList(),0)
            score = 0
            wordMultiplier = 1
            if (placedTiles[row][column].value!!.isSubmitted){
                score += tile.score
            }else{
                scoreAdd(row, column, tile)
            }
            val wordAbove = getWordAbove(row - 1, column)
            val wordBelow = getWordBelow(row + 1, column)
            val list = listOf(wordAbove, if(tile == Letter.BLANK) "_" else tile.toString(), wordBelow).filter { it.isNotEmpty() }

            return Pair(listOf(list.joinToString("")),score)
        }

        private fun getWordAbove(row: Int, column: Int): String {
            return if (row < 0 || placedTiles[row][column].value == null) {
                ""
            } else {
                val tile = placedTiles[row][column].value!!.tile
                if (placedTiles[row][column].value!!.isSubmitted){
                    score += tile.score
                }else{
                    scoreAdd(row, column, tile)
                }
                getWordAbove(row - 1, column) + (if(tile == Letter.BLANK) "_" else tile.toString())
            }
        }

        private fun getWordBelow(row: Int, column: Int): String {
            return if (row >= GRID.size || placedTiles[row][column].value == null) {
                ""
            } else {
                val tile = placedTiles[row][column].value!!.tile
                if (placedTiles[row][column].value!!.isSubmitted){
                    score += tile.score
                }else{
                    scoreAdd(row, column, tile)
                }
                (if(tile == Letter.BLANK) "_" else tile.toString() )+ getWordBelow(row + 1, column)
            }
        }


    private fun scoreAdd(row: Int, column: Int, tile: Letter){
        val cellType = GRID[row][column]
        when (cellType) {
            CellType.LD -> score += tile.score * 2
            CellType.LT -> score += tile.score * 3
            CellType.MD, CellType.ST -> {
                score += tile.score
                wordMultiplier *= 2
            }
            CellType.MT -> {
                score += tile.score
                wordMultiplier *= 3
            }
            CellType.BL -> score += tile.score
        }
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
