package com.example.scrabble

import android.content.Context
import android.widget.GridLayout
import android.widget.TextView

class ScrabbleBoard(context: Context) : GridLayout(context) {

    init {
        // Créer un GridLayout
        layoutParams = GridLayout.LayoutParams()

        // Définir le nombre de lignes et de colonnes dans le GridLayout
        rowCount = 15
        columnCount = 15

        // Créer et ajouter des vues (TextView) au GridLayout
        for (i in 0 until 15) {
            for (j in 0 until 15) {
                val textView = TextView(context)
                // Ici, vous pouvez définir les lettres pour chaque cellule en fonction de la logique du Scrabble
                val letter = getScrabbleLetter(i, j)
                textView.text = letter.toString()
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.setOnClickListener {
                    // Gérer le clic sur la vue
                }

                // Ajouter la vue au GridLayout
                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(i, GridLayout.FILL, 1f)
                params.columnSpec = GridLayout.spec(j, GridLayout.FILL, 1f)
                textView.layoutParams = params

                addView(textView)
            }
        }
    }

    private fun getScrabbleLetter(row: Int, column: Int): Char {
        // Ici, vous pouvez définir la logique pour obtenir la lettre en fonction de la position dans le Scrabble
        // Par exemple, vous pouvez utiliser un tableau ou une logique plus complexe
        return 'A'
    }
}
