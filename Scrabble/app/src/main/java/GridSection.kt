package com.example.scrabble

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrabble.CellType
import com.example.scrabble.GRID
import com.example.scrabble.Letter




    private val TILE_SPACING = 2.dp

    @Composable
     internal fun GridSection(

         modifier: Modifier = Modifier
    ) {
        BoxWithConstraints(modifier) {
            val cellSize = (maxWidth - (TILE_SPACING * (GRID.size - 1))) / GRID.size

            // Assuming we are in portrait mode, the board is a square that is width * width in size
            Box(Modifier.size(maxWidth)) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    GRID.forEachIndexed { i, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            row.forEachIndexed { j, cellType ->
                                GridCell(
                                    cellType = cellType,
                                    cellSize = cellSize,

                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private val HOVERED_BORDER_WIDTH = 2.dp

    @Composable
    private fun GridCell(
        cellType: CellType,
        cellSize: Dp,
        modifier: Modifier = Modifier
    ) {
        EmptyCell(cellType, cellSize)
    }

    private val EMPTY_CELL_FONT_SIZE = 8.sp
    private val TILE_ROUNDING = 4.dp

    @Composable
    private fun EmptyCell(
        cellType: CellType,
        cellSize: Dp,
        modifier: Modifier = Modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(cellSize)
                .clip(RoundedCornerShape(TILE_ROUNDING))
                .background(cellType.color)
        ) {
            if (cellType == CellType.ST) {
                Icon(Icons.Filled.Star, null, tint = Color.White)
            } else {
                Text(
                    cellType.name,
                    fontSize = EMPTY_CELL_FONT_SIZE,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    private val TILE_LETTER_FONT_SIZE = 10.sp
    private val TILE_POINTS_FONT_SIZE = 6.sp
    private val TILE_POINTS_PADDING = 2.dp

    @Composable
    private fun TileCell(tile: Letter, cellSize: Dp, modifier: Modifier = Modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(cellSize)
                .clip(RoundedCornerShape(TILE_ROUNDING))
                .background(Color.Yellow)
        ) {
            if (tile != Letter.BLANK) {
                Text(
                    text = tile.name,
                    fontSize = TILE_LETTER_FONT_SIZE,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tile.score.toString(),
                    fontSize = TILE_POINTS_FONT_SIZE,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(TILE_POINTS_PADDING)
                )
            }
        }
    }
