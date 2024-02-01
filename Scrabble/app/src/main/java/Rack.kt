package com.example.scrabble

import java.util.Observable

open class Rack(val player: Player) : Observable() {
    private val letters: MutableList<Letter> = mutableListOf()

    // Méthode pour ajouter une lettre au chevalet
     fun addLetter(letter: Letter) {
        letters.add(letter)
        setChanged()
        notifyObservers()
    }

    // Méthode pour retirer une lettre du chevalet
     fun removeLetter(letter: Letter) {
        letters.remove(letter)
        setChanged()
        notifyObservers()
    }

    // Méthode pour piocher une lettre du sac et l'ajouter au chevalet
     fun drawLetterFromBag(letterBag: LetterBag) {
        val letter: Letter? = letterBag.drawLetter()
        if (letter != null) {
            addLetter(letter)
        }
        setChanged()
        notifyObservers()
    }

    // Méthode pour échanger des lettres avec le sac
     fun exchangeLetters(letterBag: LetterBag, lettersToExchange: List<Letter>) {
         val lettersNb = lettersToExchange.size
         letters.retainAll{letter -> !lettersToExchange.contains(letter)}
         for(i in 1..lettersNb){
             drawLetterFromBag(letterBag)
         }
         setChanged()
         notifyObservers()
    }

     fun drawMultipleLetters(letterBag: LetterBag, numLetters: Int) {
        repeat(numLetters) {
            val letter: Letter? = letterBag.drawLetter()
            if (letter != null) {
                addLetter(letter)
            }
        }
         setChanged()
         notifyObservers()
    }
     fun getRack() : MutableList<Letter>{
        return letters
    }

    // TODO: Si vous avez des idées pour améliorer cette classe, n'hésitez pas à les partager
}