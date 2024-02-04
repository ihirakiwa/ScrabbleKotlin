package com.example.scrabble

import android.os.Bundle

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView

class BoardActivity : AppCompatActivity() {
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var rack1: Rack



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        // Récupérer les joueurs de l'intent

        player1 = intent.getSerializableExtra("player1") as Player
        player2 = intent.getSerializableExtra("player2") as Player
        val rackView = findViewById<ComposeView>(R.id.rackView)
        val playerScoresComposeView = findViewById<ComposeView>(R.id.playerScoresComposeView)
        playerScoresComposeView.setContent {
            PlayerScoresSection(player1, player2, player1.name)
        }
        val scrabbleGridComposeView = findViewById<ComposeView>(R.id.scrabbleGridComposeView)
        scrabbleGridComposeView.setContent {GridSection()}
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val letterBag = LetterBag()
        rack1 = Rack(player1)
        rack1.drawMultipleLetters(LetterBag(), 7)
        rackView.setContent {
            PlayerTilesSection(rack1.getRack(), false)
        }
        btnRefresh.setOnClickListener {
            rack1.exchangeLetters(letterBag, rack1.getRack())
            rackView.setContent {
                PlayerTilesSection(rack1.getRack(), false)
            }
        }
    }
}