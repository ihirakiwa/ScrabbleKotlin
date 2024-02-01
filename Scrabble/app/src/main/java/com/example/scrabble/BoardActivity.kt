package com.example.scrabble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class BoardActivity : AppCompatActivity() {
    private lateinit var rack: Rack
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        val rackView = findViewById<RackView>(R.id.rackView)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val player = Player("Joueur 1")
        val letterbag = LetterBag()
        rack = Rack(player)
        rack.drawMultipleLetters(LetterBag(), 7)
        rackView.updateView(rack.getRack())
        rackView.visibility = View.VISIBLE
        btnRefresh.setOnClickListener {
            rack.exchangeLetters(letterbag, rack.getRack())
            rackView.updateView(rack.getRack())
        }
    }


}