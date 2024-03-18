package com.example.scrabble


import PlayerData
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SCORE_BOARD_HEIGHT = 60.dp

@Composable
fun PlayerScoresSection(
    playerOneData: PlayerData,
    playerTwoData: PlayerData,
    currentTurnPlayer: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(SCORE_BOARD_HEIGHT).fillMaxWidth()
    ) {
        PlayerScore(
            left = true,
            name = playerOneData.name,
            score = playerOneData.score,
            isCurrentTurnPlayer = playerOneData.name == currentTurnPlayer,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(Modifier.fillMaxHeight())
        PlayerScore(
            left = false,
            name = playerTwoData.name,
            score = playerTwoData.score,
            isCurrentTurnPlayer = playerTwoData.name == currentTurnPlayer,
            modifier = Modifier.weight(1f)
        )
    }
}

private val SCORE_PADDING = 2.dp
private val SCORE_FONT_SIZE = 17.sp
private val PLAYER_FONT_SIZE = 15.sp
private val INTERNAL_PADDING = 8.dp
@Composable
private fun PlayerScore(
    left: Boolean,
    name: String,
    score: Int,
    isCurrentTurnPlayer: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            // Originally, the border was implemented with a then() that conditionally returned
            // Modifier.border(). However, doing it the current way is ideal as we get both padding
            // and border behavior by changing just a single value.
            .then(if (isCurrentTurnPlayer) Modifier.background(Color(31, 220, 34)) else Modifier)
            .padding(SCORE_PADDING)
            .background(Color(200, 200, 200))
            .border(2.dp, Color.Black)
    ) {
        if(left) {
            Text(
                text = name,
                fontSize = PLAYER_FONT_SIZE,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(INTERNAL_PADDING)
            )
            Text(
                text = score.toString(),
                fontSize = SCORE_FONT_SIZE,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(INTERNAL_PADDING)

            )
        } else {
            Text(
                text = score.toString(),
                fontSize = SCORE_FONT_SIZE,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(INTERNAL_PADDING)
            )
            Text(
                text = name,
                fontSize = PLAYER_FONT_SIZE,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(INTERNAL_PADDING)
            )
            }
    }
}
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = Color.Black
) {
    Box(modifier.width(thickness).background(color))
}