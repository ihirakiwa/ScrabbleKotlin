package com.example.scrabble.com.example.scrabble

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        editTextPlayer2 = findViewById(R.id.editTextPlayer2)
        buttonStart = findViewById(R.id.buttonStart)
        buttonStart.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        // Récupérer les noms des joueurs depuis les champs de texte
        val player1Name = editTextPlayer1.text.toString()
        val player2Name = editTextPlayer2.text.toString()
        if(!player1Name.isNullOrBlank() && !player2Name.isNullOrBlank()){
            // Créer les joueurs
            val player1 = Player(player1Name)
            val player2 = Player(player2Name)
            val intent = Intent(this,BoardActivity::class.java)
            intent.putExtra("player1", player1)
            intent.putExtra("player2", player2)
            startActivity(intent)
        }else{
            // Afficher un message d'erreur
            Toast.makeText(this, "Veuillez saisir les noms des deux joueurs", Toast.LENGTH_SHORT).show()
        }
    }

}