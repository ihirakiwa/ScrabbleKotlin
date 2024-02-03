package com.example.scrabble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class BoardActivity : AppCompatActivity() {
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var rack1: Rack
    private lateinit var player1View: PlayerView
    private lateinit var player2View: PlayerView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        // Récupérer les joueurs de l'intent
        player1 = intent.getSerializableExtra("player1") as Player
        player2 = intent.getSerializableExtra("player2") as Player
        val rackView = findViewById<RackView>(R.id.rackView)
        player1View = PlayerView(findViewById(R.id.textViewPlayer1ScoreandName),player1)
        player1.addPoints(10)
        player1View.updatePlayerScore()
        player2View = PlayerView(findViewById(R.id.textViewPlayer2ScoreandName),player2)
        player2View.updatePlayerScore()


        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val letterbag = LetterBag()
        rack1 = Rack(player1)
        rack1.drawMultipleLetters(LetterBag(), 7)
        rackView.updateView(rack1.getRack())
        rackView.visibility = View.VISIBLE
        btnRefresh.setOnClickListener {
            rack1.exchangeLetters(letterbag, rack1.getRack())
            rackView.updateView(rack1.getRack())
        }
    }


}