
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import model.PlacedTile


private val TILE_SPACING = 2.dp

    @Composable
     internal fun GridSection(
        onGetTile: (row: Int, column: Int) -> PlacedTile?,
        onSetTile: (tile: Letter, row: Int, column: Int) -> Unit,
        onRemoveTile: (row: Int, column: Int) -> Unit,
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
                                    row = i,
                                    column = j,
                                    cellType = cellType,
                                    cellSize = cellSize,
                                    onGetTile = onGetTile,
                                    onSetTile = onSetTile,
                                    onRemoveTile = onRemoveTile
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun GridCell(
        row: Int,
        column: Int,
        cellType: CellType,
        cellSize: Dp,
        onGetTile: (row: Int, column: Int) -> PlacedTile?,
        onSetTile: (tile: Letter, row: Int, column: Int) -> Unit,
        onRemoveTile: (row: Int, column: Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        withDragContext(LocalTileDragContext.current) {
            DropTarget(
                onDragTargetAdded = { onSetTile(it, row, column) },
                onDragTargetRemoved = { onRemoveTile(row, column) }
            ) { hovered->

                val placedTile = onGetTile(row, column)?.tile
                if (placedTile == null) {
                    EmptyCell(cellType, cellSize, hovered == DropTargetStatus.HOVERED, modifier)
                } else {
                    TileCell(placedTile, cellSize)
                }
            }
        }
    }
    private val HOVERED_BORDER_WIDTH = 2.dp
    private val EMPTY_CELL_FONT_SIZE = 8.sp
    private val TILE_ROUNDING = 4.dp

    @Composable
    private fun EmptyCell(
        cellType: CellType,
        cellSize: Dp,
        isHovered: Boolean,
        modifier: Modifier = Modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(cellSize)
                .clip(RoundedCornerShape(TILE_ROUNDING))
                .background(cellType.color)
                .clickable {
                    Log.d("GridEmpty", "Clicked on cell at $cellType")
                }
                .then(
                    if (isHovered) {
                        Modifier.border(HOVERED_BORDER_WIDTH, Color.Red)
                    } else {
                        Modifier
                    }
                )
        ) {
            if (cellType == CellType.ST) {
                Icon(Icons.Filled.Star, null, tint = Color.White)
            }else if(cellType != CellType.BL){
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
                .background(Color.LightGray)
                .clickable { Log.d("GridTile", "Clicked on tile $tile")}
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
