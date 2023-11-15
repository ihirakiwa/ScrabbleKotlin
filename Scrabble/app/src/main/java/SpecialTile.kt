package com.example.scrabble

class SpecialTile(val type: TileType) {
    enum class TileType {
        DOUBLE_LETTER,
        TRIPLE_LETTER,
        DOUBLE_WORD,
        TRIPLE_WORD
    }

    fun getScoreMultiplier(): Int {
        return when (type) {
            TileType.DOUBLE_LETTER -> 2
            TileType.TRIPLE_LETTER -> 3
            TileType.DOUBLE_WORD -> 2
            TileType.TRIPLE_WORD -> 3
        }
    }

    fun isDoubleLetter(): Boolean = type == TileType.DOUBLE_LETTER

    fun isTripleLetter(): Boolean = type == TileType.TRIPLE_LETTER

    fun isDoubleWord(): Boolean = type == TileType.DOUBLE_WORD

    fun isTripleWord(): Boolean = type == TileType.TRIPLE_WORD

    fun applyMultiplier(score: Int): Int {
        return score * getScoreMultiplier()
    }

    override fun toString(): String {
        return when (type) {
            TileType.DOUBLE_LETTER -> "Double Letter"
            TileType.TRIPLE_LETTER -> "Triple Letter"
            TileType.DOUBLE_WORD -> "Double Word"
            TileType.TRIPLE_WORD -> "Triple Word"
        }
    }


}