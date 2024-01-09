package com.example.scrabble

class Rack(val player: Player) {
    private val letters: MutableList<Letter> = mutableListOf()

    // Méthode pour ajouter une lettre au chevalet
     fun addLetter(letter: Letter) {
        letters.add(letter)
    }

    // Méthode pour retirer une lettre du chevalet
     fun removeLetter(letter: Letter) {
        letters.remove(letter)
    }

    // Méthode pour piocher une lettre du sac et l'ajouter au chevalet
     fun drawLetterFromBag(letterBag: LetterBag) {
        val letter: Letter? = letterBag.drawLetter()
        if (letter != null) {
            addLetter(letter)
        }
    }

    // Méthode pour échanger des lettres avec le sac
     fun exchangeLetters(letterBag: LetterBag, lettersToExchange: List<Letter>) {
        for (letter in lettersToExchange) {
            removeLetter(letter)
        }

        letters.addAll(letterBag.exchangeLetters(lettersToExchange))
    }

     fun drawMultipleLetters(letterBag: LetterBag, numLetters: Int) {
        repeat(numLetters) {
            val letter: Letter? = letterBag.drawLetter()
            if (letter != null) {
                addLetter(letter)
            }
        }
    }
     fun getRack() : MutableList<Letter>{
        return letters
    }

    // TODO: Si vous avez des idées pour améliorer cette classe, n'hésitez pas à les partager
}