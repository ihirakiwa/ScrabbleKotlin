package com.example.scrabble

import kotlin.random.Random

class LetterBag {
    private val letters : MutableList<Letter> = mutableListOf()
    init{
        for (letter in Letter.values()) {
            repeat(letter.frequency) {
                letters.add(letter)
            }
        }
    }

    fun getLetters(): List<Letter> {
        return letters.toList()
    }

    fun getNumberOfLetters(): Int {
        return letters.size
    }

    fun drawLetter(): Letter? {
        return if (letters.isNotEmpty()) {
            val randomIndex = Random.nextInt(letters.size)
            val drawnLetter = letters.removeAt(randomIndex)
            return drawnLetter
        } else {
            null // Le sac est vide
        }
    }

    fun drawMultipleLetters(count: Int): List<Letter>{
        val drawnLetters = mutableListOf<Letter>()
        repeat(count) {
            val drawnLetter = drawLetter()
            if (drawnLetter != null) {
                drawnLetters.add(drawnLetter)
            }
        }
        return drawnLetters
    }

    fun exchangeLetters(lettersToExchange: List<Letter>): List<Letter> {
        val newLetters = mutableListOf<Letter>()
        for (letter in lettersToExchange) {
            val drawnLetter = drawLetter()
            if (drawnLetter != null) {
                newLetters.add(drawnLetter)
            }
        }
        return newLetters
    }
}