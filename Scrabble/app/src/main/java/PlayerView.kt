package com.example.scrabble

import android.widget.TextView

class PlayerView(private val textView: TextView, private val player: Player) {

   public fun updatePlayerScore() {
        val scoreText = "${player.name}: ${player.getScore()} point${if (player.getScore() > 1) "s" else ""}"
        textView.text = scoreText
    }
}