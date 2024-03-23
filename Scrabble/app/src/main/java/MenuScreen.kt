import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scrabble.R

private val HORIZONTAL_PADDING = 40.dp
private val SPACER_SIZE = 40.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(start2v2Game: () -> Unit){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp),
                title = { Text(text = "Scrabble", color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(66, 75, 90))

            )
        },
        contentColor = Color(66, 75, 90)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(66, 75, 90) // Couleur de fond si l'image n'occupe pas toute la surface
                )
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
        ) {
            // Ajouter l'image de fond
            Image(
                painter = rememberImagePainter(R.drawable.background2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(PaddingValues(horizontal = HORIZONTAL_PADDING))
                    .fillMaxWidth()
            ) {
                Text(text = "Bienvenue au Scrabble", color = Color.White, fontSize = 24.sp)
                Spacer(Modifier.size(10.dp))
                Text(text = "Veuillez choisir un mode de jeu :", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.size(SPACER_SIZE))
                Button(
                    onClick = { start2v2Game() },
                    modifier = Modifier.size(160.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "2 Joueurs", fontSize = 18.sp)
                }
                Spacer(Modifier.size(SPACER_SIZE))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(160.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "3 Joueurs", fontSize = 18.sp)
                }
                Spacer(Modifier.size(SPACER_SIZE))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(160.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "4 Joueurs", fontSize = 18.sp)
                }
                Spacer(Modifier.size(100.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(200.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Règles du jeu", fontSize = 18.sp)
                }
            }
        }
    }
}