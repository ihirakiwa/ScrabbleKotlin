import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
private val SPACER_SIZE = 200.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(onSubmitPlayerNames: (String, String) -> Unit) {
    var playerOneName by remember { mutableStateOf("") }
    var playerTwoName by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp),
                title = { Text(text = "Scrabble !", color = Color.White)},
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(66,75,90))

            )
        },
        contentColor = Color(66,75,90)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(66,75,90) // Couleur de fond si l'image n'occupe pas toute la surface
                )
                .padding(0.dp,60.dp,0.dp,0.dp)
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
            ) {

                OutlinedTextField(
                    label = { Text(text = "Joueur 1", color = Color.White) },
                    value = playerOneName,
                    onValueChange = { playerOneName = it },
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(20.dp))
                OutlinedTextField(
                    label = { Text(text = "Joueur 2", color = Color.White) },
                    value = playerTwoName,
                    textStyle = TextStyle(color = Color.White),
                    onValueChange = { playerTwoName = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(SPACER_SIZE))
                Button(
                    enabled = playerOneName.isNotBlank() && playerTwoName.isNotBlank(),
                    onClick = { onSubmitPlayerNames(playerOneName, playerTwoName) },
                    modifier = Modifier.size(160.dp, 50.dp)
                ) {
                    Text(text = "Start", fontSize = 18.sp)
                }
            }
        }
    }
}