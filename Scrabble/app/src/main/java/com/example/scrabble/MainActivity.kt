package com.example.scrabble

import android.os.Bundle
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
import com.example.scrabble.ui.theme.ScrabbleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrabbleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp) // Ajoute un espacement général
                            .fillMaxSize(), // Remplit toute la hauteur et la largeur
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Ajoute un espacement vertical entre les composants
                    ) {
                        val letterBag = LetterBag()
                        val initialLetters = letterBag.drawMultipleLetters(7)
                        val initialNumberOfLetters = letterBag.getNumberOfLetters()

                        Text(text = "Initial Letters: $initialLetters ")
                        Text(text = "Initial Number of Letters: $initialNumberOfLetters")

                        val newLetters = letterBag.exchangeLetters(initialLetters)
                        val afterNumberOfLetters = letterBag.getNumberOfLetters()
                        Text(text = "New Letters: $newLetters")
                        Text(text = "After Number of Letters: $afterNumberOfLetters")
                    }
                }
            }
        }
    }
}