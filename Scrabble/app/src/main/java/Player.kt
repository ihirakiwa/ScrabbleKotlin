package com.example.scrabble

class Player(val name : String) {
    var score : Int = 0
        private set

    val rack : MutableList<Letter> = mutableListOf()

    fun addPoints(points: Int) {
        score += points
    }

    fun drawLetterFromBag(letterBag: LetterBag) {
        val letter: Letter? = letterBag.drawLetter()
        if(letter != null) {
            rack.add(letter)
        }
    }

    fun exchangeLetters(letterBag: LetterBag, lettersToExchange: List<Letter>) {
        for (letter in lettersToExchange) {
            rack.remove(letter)
        }

        rack.addAll(letterBag.exchangeLetters(lettersToExchange))
    }
    // TODO: Implement playWord
    /*
    fun playWord(word: String, gameBoard: GameBoard) {

    } */

    fun displayRack() {
        println("$name's Rack: $rack")
    }
}