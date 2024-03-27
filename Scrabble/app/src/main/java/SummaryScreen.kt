import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scrabble.R

private val HORIZONTAL_PADDING = 40.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(playerOneData: PlayerData, playerTwoData: PlayerData, wordGameViewModel : WordGameViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp),
                title = { Text(text = "Fin de partie", color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(66, 75, 90))
            )
        },
        contentColor = Color(66, 75, 90)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(66, 75, 90)
                )
        ) {
            Image(
                painter = rememberImagePainter(R.drawable.background2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(PaddingValues(horizontal = HORIZONTAL_PADDING))
                    .padding(top = 20.dp, start = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (playerOneData.score > playerTwoData.score) {
                    Text(text = "Bravo à ${playerOneData.name} !", color = Color.White)
                } else if (playerOneData.score < playerTwoData.score) {
                    Text(text = "Bravo à ${playerTwoData.name} !", color = Color.White)
                } else {
                    Text(text = "Bravo aux joueurs !", color = Color.White)
                }
                Text(text = "Résumé de la partie :", color = Color.White)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(PaddingValues(horizontal = HORIZONTAL_PADDING))
                    .padding(top = 100.dp)
            ) {

                PlayerSummary(playerOneData)
                Spacer(Modifier.size(20.dp))
                PlayerSummary(playerTwoData)
                Spacer(Modifier.size(100.dp))
                Button(
                    onClick = {wordGameViewModel.comeBackToMenu() },
                    modifier = Modifier.size(200.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                ) {
                    Text(text = "Retourner au menu", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun PlayerSummary(playerData: PlayerData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(alpha = 0.9f),
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Joueur : ${playerData.name}")
            Text(text = "Score : ${playerData.score}")
        }
    }
}