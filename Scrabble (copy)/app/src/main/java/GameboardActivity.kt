package com.example.scrabble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrabble.ui.theme.ScrabbleTheme



class GameBoardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrabbleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameBoard()
                }
            }
        }
    }
}

@Composable
fun GameBoard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Pour chaque ligne dans le plateau
        repeat(15) { row ->
            // Créer une ligne
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                // Pour chaque colonne dans la ligne
                repeat(15) { col ->
                    // Appeler le composable représentant une cellule
                    Cell("A", Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun Cell(letter: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Gray)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameBoardPreview() {
    ScrabbleTheme {
        GameBoard()
    }
}