package com.example.scrabble

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrabble.ui.theme.ScrabbleTheme

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcomepage)
        val jouerButton = findViewById<Button>(R.id.btnJouer)
        mediaPlayer = MediaPlayer.create(this, R.raw.mavie)
        //mediaPlayer?.start()
        jouerButton.setOnClickListener {
            startSecondActivity()

        }
    }

    private fun startSecondActivity() {
        val intent = Intent(this@MainActivity, BoardActivity::class.java)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Libérer les ressources du MediaPlayer lors de la fermeture de l'application
        mediaPlayer?.release()
        mediaPlayer = null
    }
}