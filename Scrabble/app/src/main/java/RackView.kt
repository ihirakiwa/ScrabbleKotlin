package com.example.scrabble

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class RackView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val rackTextViews: MutableList<TextView> = mutableListOf()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    // Méthode pour mettre à jour la vue du chevalet avec les lettres actuelles
    fun updateView(letters: List<Letter>) {
        removeAllViews()
        rackTextViews.clear()

        for (letter in letters) {
            val letterTextView = createLetterTextView(letter)
            addView(letterTextView)
            rackTextViews.add(letterTextView)
        }
    }

    // Méthode pour créer un TextView représentant une lettre
    @SuppressLint("ClickableViewAccessibility")
    private fun createLetterTextView(letter: Letter): TextView {
        val textView = TextView(context)
        textView.text = letter.displayName
        textView.textSize = 42f
        textView.setTextColor(Color.BLACK)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.setMargins(8, 8, 8, 8)
        textView.layoutParams = params

        textView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Action lorsque la lettre est touchée
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = DragShadowBuilder(view)
                view.startDragAndDrop(data, shadowBuilder, view, 0)

                true
            } else {
                false
            }

        }
        return textView
    }
}
