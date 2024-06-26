

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrabble.Letter
import kotlin.math.roundToInt

private val TILES_ROW_BOTTOM_PADDING = 8.dp
@Composable
fun PlayerTilesSection(
    tiles: List<Letter>,
    tileVisibility: Boolean,
    onTileVisibilityChanged: (Boolean) -> Unit,
    isSubmitEnabled: Boolean,
    getPlacing: () -> List<Pair<Int, Int>>,
    setPlacingEmpty: () -> Unit,
    setAlreadyPlaced: (List<Pair<Int, Int>>) -> Unit,
    setTileSubmitted: (List<Pair<Int, Int>>) -> Unit,
    onSubmit: () -> Boolean,
    nextTurn: (List<Letter>) -> Unit,
    setSubmitEnabled: (Boolean) -> Unit,
    letter: (List<Pair<Int, Int>>) -> List<Letter>,
    setScore: (Int) -> Unit,
    getScore: () -> Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val tileSize = (maxWidth - (TILE_SPACING * (tiles.size - 1))) / tiles.size
        val tileSizePx = with(LocalDensity.current) { tileSize.toPx() }
        val tileSpacingPx = with(LocalDensity.current) { TILE_SPACING.toPx() }
        fun offsetForIndex(index: Int) = index * (tileSizePx + tileSpacingPx)
        val tileOffsets = remember { List(tiles.size) { offsetForIndex(it) }.toMutableStateList() }
        fun normalizeOffsets() {
            val sortedOffsets = tileOffsets
                .mapIndexed { index, offset -> Pair(index, offset) }
                .sortedBy { it.second }
            sortedOffsets.forEachIndexed { sortedIndex, (originalIndex, _) ->
                tileOffsets[originalIndex] = offsetForIndex(sortedIndex)
            }
        }
        Column(modifier = modifier.padding(top = 10.dp)) {
            TileVisibilitySwitch(
                visibility = tileVisibility,
                onVisibilityChanged = onTileVisibilityChanged
            )
            PlayerTiles(
                tiles = tiles,
                tileSize = tileSize,
                tileOffsets = tileOffsets,
                tileVisibility = tileVisibility,
                onNormalizeOffsets = ::normalizeOffsets,
                modifier = Modifier.padding(bottom = TILES_ROW_BOTTOM_PADDING)
            )
            TileControls(
                isSubmitEnabled = isSubmitEnabled,
                getPlacing = getPlacing,
                setPlacingEmpty = setPlacingEmpty,
                setAlreadyPlaced = setAlreadyPlaced,
                setTileSubmitted = setTileSubmitted,
                onSubmit = onSubmit,
                nextTurn = nextTurn,
                setSubmitEnabled = setSubmitEnabled,
                letter = letter,
                setScore = setScore,
                getScore = getScore
            )
        }
    }
}
@Composable
private fun TileVisibilitySwitch(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    onVisibilityChanged: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Afficher les lettres :", color = Color.White)
        Switch(
            checked = visibility,
            onCheckedChange = onVisibilityChanged,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color(98, 138, 169)
            )
        )
    }
}

private val TILE_SPACING = 8.dp
private const val TILE_CONTAINER_SHADOW_ELEVATION = 10f
@Composable
private fun PlayerTiles(
    tiles: List<Letter>,
    tileSize: Dp,
    tileVisibility: Boolean,
    tileOffsets: SnapshotStateList<Float>,
    onNormalizeOffsets: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zIndices = remember { List(tiles.size) { 0f }.toMutableStateList() }
    Box(modifier) {
        tiles.forEachIndexed { index, tile ->
            Column(
                modifier = Modifier
                    .zIndex(zIndices[index])
                    .offset { IntOffset(x = tileOffsets[index].roundToInt(), y = 0) }
                    .graphicsLayer {
                        shadowElevation = TILE_CONTAINER_SHADOW_ELEVATION
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { zIndices[index] = 1f },
                            onDragEnd = {
                                zIndices[index] = 0f
                                onNormalizeOffsets()
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                tileOffsets[index] += dragAmount.x
                            }
                        )
                    }
                    .background(Color(40, 40, 40))
            ) {
                if (tileVisibility) {
                    Tile(
                        tile = tile,
                        tileSize = tileSize
                    )
                } else {
                    HiddenTile(tileSize)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(tileSize).background(Color(98, 138, 169))
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Repositionner",
                        tint = Color.White
                    )
                }
            }
        }
    }
}



private const val TILE_DRAG_SCALE_FACTOR = 0.5f
private const val TILE_DROP_SCALE_FACTOR = 0.5f
private val TILE_LETTER_FONT_SIZE = 22.sp
private val TILE_POINTS_FONT_SIZE = 10.sp
private val TILE_POINTS_PADDING = 2.dp
@Composable
private fun Tile(tile: Letter, tileSize: Dp, modifier: Modifier = Modifier) {
    withDragContext(LocalTileDragContext.current) {
        DragTarget(
            data = tile,
            options = DragOptions(
                onDragScaleX = TILE_DRAG_SCALE_FACTOR,
                onDragScaleY = TILE_DRAG_SCALE_FACTOR,
                onDropScaleX = TILE_DROP_SCALE_FACTOR,
                onDropScaleY = TILE_DROP_SCALE_FACTOR,
                snapPosition = SnapPosition.CENTER
            )
        ) { dragStatus ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .alpha(if (dragStatus == DragTargetStatus.DROPPED) 0f else 1f)
                    .size(tileSize)
                    .background(Color.LightGray)
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
    }
}

@Composable
private fun HiddenTile(tileSize: Dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(tileSize).background(Color(40, 40, 40))
    ) {
        Text(
            text = "?",
            fontSize = TILE_LETTER_FONT_SIZE,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
val BUTTON_SPACING = 8.dp
@Composable
private fun TileControls(
    isSubmitEnabled: Boolean,
    getPlacing: () -> List<Pair<Int, Int>>,
    setPlacingEmpty: () -> Unit,
    setAlreadyPlaced: (List<Pair<Int, Int>>) -> Unit,
    setTileSubmitted: (List<Pair<Int, Int>>) -> Unit,
    onSubmit: () -> Boolean,
    nextTurn: (List<Letter>) -> Unit,
    setSubmitEnabled: (Boolean) -> Unit,
    letter: (List<Pair<Int, Int>>) -> List<Letter>,
    setScore: (Int) -> Unit,
    getScore: () -> Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dragContext = LocalTileDragContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(BUTTON_SPACING),
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { dragContext.resetDragTargets() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
        ) {
            Text(
                "Annuler",
                modifier = Modifier
            )
        }
        Button(
            onClick = {
                if (onSubmit()) {
                    setAlreadyPlaced(getPlacing())
                    setTileSubmitted(getPlacing())
                    val score = getScore()
                    setScore(score)
                    nextTurn(letter(getPlacing()))
                    setSubmitEnabled(false)
                    setPlacingEmpty()
                }else{
                    val message = "Le mot n'est pas valide"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.weight(1f),
            enabled = isSubmitEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
        ) {
            Text("Envoyer")
        }
    }
}