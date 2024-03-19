

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val tileSize = (maxWidth - (TILE_SPACING * (tiles.size - 1))) / tiles.size
        val tileSizePx = with(LocalDensity.current) { tileSize.toPx() }
        val tileSpacingPx = with(LocalDensity.current) { TILE_SPACING.toPx() }

        // Calculates the offset from the 0 position (parent's left boundary in LTR) for a given tile
        fun offsetForIndex(index: Int) = index * (tileSizePx + tileSpacingPx)

        // Tile offsets that can be manipulated by user interactions (e.g. rearranged, shuffled)
        val tileOffsets = remember { List(tiles.size) { offsetForIndex(it) }.toMutableStateList() }

        // Repositions all tiles to the appropriate offsets based on the relative order that they
        // appear in the tile area
        fun normalizeOffsets() {
            val sortedOffsets = tileOffsets
                .mapIndexed { index, offset -> Pair(index, offset) }
                .sortedBy { it.second }
            sortedOffsets.forEachIndexed { sortedIndex, (originalIndex, _) ->
                tileOffsets[originalIndex] = offsetForIndex(sortedIndex)
            }
        }

        Column(modifier = modifier) {
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
                isSubmitEnabled = isSubmitEnabled
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

    // While normally a Row would get used to compose horizontally arranged elements, here we
    // do not do so because we must be able to manually position each tile with an offset (e.g.
    // for rearranging or shuffling tiles).
    // Importantly, we want offsets to all start at the same start location (i.e. 0 means the
    // left bound of the parent), which would not be true in a Row (i.e. 0 would mean wherever
    // Row placed the tile relative to its siblings).
    Box(modifier) {
        tiles.forEachIndexed { index, tile ->
            Column(
                modifier = Modifier
                    .zIndex(zIndices[index])
                    .offset { IntOffset(x = tileOffsets[index].roundToInt(), y = 0) }
                    .graphicsLayer {
                        // Cannot use `Modifier.shadow()` because that will place this entire composable
                        // into a new graphics layer, which will obviously break UI functionality
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
                    modifier = Modifier.width(tileSize)
                ) {
                    //Icon(painterResource("drag_icon.xml"), null)
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
                // Even though we set the tile's alpha to zero in a dropped state (see below), this
                // scaling must still be applied so that the tile has the correct bounds in case it
                // is dragged again in the future.
                onDropScaleX = TILE_DROP_SCALE_FACTOR,
                onDropScaleY = TILE_DROP_SCALE_FACTOR,
                snapPosition = SnapPosition.CENTER
            )
        ) { dragStatus ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    // It is the responsibility of the grid to display information for a placed tile
                    // (vs simply keeping the placed tile visible at all times). This is because a
                    // placed tile is susceptible to visual jitter if its parent composable were to
                    // be moved, for example while rearranging or shuffling the tiles.
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
    modifier: Modifier = Modifier
) {
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
            onClick = { }, //TODO: Implement this
            modifier = Modifier.weight(1f),
            enabled = isSubmitEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color(98, 138, 169)),
        ) {
            Text("Envoyer")
        }
    }
}