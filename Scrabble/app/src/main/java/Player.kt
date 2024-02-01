package com.example.scrabble

public class Player(val name : String) {
    var score : Int = 0
        private set


     fun addPoints(points: Int) {
        score += points
    }

     fun removePoints(points: Int) {
        score -= points
    }
     fun resetScore() {
        score = 0
    }
    // TODO: Implement playWord
    /*
    fun playWord(word: String, gameBoard: GameBoard) {

    } */


}