package com.example.scrabble

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrabble.CellType.*

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
        val letterbag = LetterBag()
        rack1 = Rack(player1)
        rack1.drawMultipleLetters(LetterBag(), 7)
        rackView.setContent {
            PlayerTilesSection(rack1.getRack())
        }
        btnRefresh.setOnClickListener {
            rack1.exchangeLetters(letterbag, rack1.getRack())
            rackView.setContent {
                PlayerTilesSection(rack1.getRack())
            }
        }
    }
}