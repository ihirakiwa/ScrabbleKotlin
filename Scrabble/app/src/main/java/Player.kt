package com.example.scrabble

import java.io.Serializable

public class Player(val name : String) : Serializable{
    private var score : Int = 0
        private set


     fun addPoints(points: Int) {
        score += points
    }

    fun getScore(): Int {
        return score
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