package com.example.scrabble.com.example.scrabble

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.scrabble.*

class PlayersActivity: AppCompatActivity(){
    private lateinit var editTextPlayer1: EditText
    private lateinit var editTextPlayer2: EditText
    private lateinit var buttonStart: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.players)
        editTextPlayer1 = findViewById(R.id.editTextPlayer1)
        buttonStart = findViewById(R.id.buttonStart)
        buttonStart.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        // Récupérer les noms des joueurs depuis les champs de texte
        val player1Name = editTextPlayer1.text.toString()
        val player2Name = editTextPlayer2.text.toString()

        // Vous pouvez ajouter ici le code pour démarrer le jeu avec les noms des joueurs
        // Par exemple, lancer une nouvelle activité ou effectuer une action spécifique.
    }

}