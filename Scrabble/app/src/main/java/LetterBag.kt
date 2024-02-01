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

}