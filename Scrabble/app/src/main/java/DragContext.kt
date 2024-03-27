import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

data class DragOptions(
    val onDragScaleX: Float = 1.0f,
    val onDragScaleY: Float = 1.0f,
    val onDropScaleX: Float = 1.0f,
    val onDropScaleY: Float = 1.0f,
    val snapPosition: SnapPosition? = null
)

data class DropOptions(
    val maxDragTargets: Int = 1
)

enum class SnapPosition(val calculateOffset: (Rect) -> Offset) {
    CENTER(Rect::center),
}

enum class DragTargetStatus { NONE, DRAGGED, DROPPED }
enum class DropTargetStatus { NONE, HOVERED, DROPPED }

class DragContext<T> {
    private inner class DragTargetState(
        val data: T,
        val status: MutableState<DragTargetStatus> = mutableStateOf(DragTargetStatus.NONE),
        val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
        val dropTargets: MutableSet<DropTargetState> = mutableSetOf()
    ) {
        fun resetState() {
            status.value = DragTargetStatus.NONE
            offset.value = Offset.Zero
        }

        fun detachFromDropTargets() {
            dropTargets.forEach { dropTarget ->
                dropTarget.dragTargets.remove(this)
                dropTarget.updateState()
                dropTarget.onDragTargetRemoved(data)
            }
            dropTargets.clear()
        }

        fun onDispose() = detachFromDropTargets()
    }

    private val dragTargetStates = mutableSetOf<DragTargetState>()

    fun resetDragTargets() {
        dragTargetStates.forEach {
            it.detachFromDropTargets()
            it.resetState()
        }
    }

    @Composable
    private fun rememberDragTargetState(
        data: T,
        content: @Composable (DragTargetStatus) -> Unit
    ): DragTargetState {
        val dragTargetState = remember(data, content) { DragTargetState(data) }
        DisposableEffect(data, content) {
            dragTargetStates.add(dragTargetState)
            onDispose {
                dragTargetState.onDispose()
                dragTargetStates.remove(dragTargetState)
            }
        }
        return dragTargetState
    }

    @Composable
    fun DragTarget(
        data: T,
        options: DragOptions = DragOptions(),
        content: @Composable (DragTargetStatus) -> Unit
    ) {
        val dragTargetState = rememberDragTargetState(data, content)
        val statusState = dragTargetState.status
        val offsetState = dragTargetState.offset

        Box(
            modifier = Modifier
                .graphicsLayer {
                    when (statusState.value) {
                        DragTargetStatus.DRAGGED -> {
                            scaleX = options.onDragScaleX
                            scaleY = options.onDragScaleY
                        }

                        DragTargetStatus.DROPPED -> {
                            scaleX = options.onDropScaleX
                            scaleY = options.onDropScaleY
                        }

                        else -> {
                            scaleX = 1f
                            scaleY = 1f
                        }
                    }
                }
                .offset {
                    val dragOffset = offsetState.value
                    when (statusState.value) {
                        DragTargetStatus.DRAGGED, DragTargetStatus.NONE -> {
                            IntOffset(
                                x = dragOffset.x.roundToInt(),
                                y = dragOffset.y.roundToInt()
                            )
                        }

                        DragTargetStatus.DROPPED -> {
                            IntOffset(
                                x = (dragOffset.x * options.onDragScaleY / options.onDropScaleX).roundToInt(),
                                y = (dragOffset.y * options.onDragScaleY / options.onDropScaleY).roundToInt()
                            )
                        }
                    }
                }
                .onGloballyPositioned { coordinates ->
                    val dragTargetRect = coordinates.boundsInWindow()
                    dropTargetStates.forEach { dropTargetState ->
                        if (statusState.value == DragTargetStatus.DRAGGED) {
                            if (dropTargetState.globalRect.contains(dragTargetRect.center)) {
                                if (dropTargetState.dragTargets.size < dropTargetState.options.maxDragTargets) {
                                    dragTargetState.dropTargets.add(dropTargetState)
                                    dropTargetState.dragTargets.add(dragTargetState)
                                }
                            } else {
                                dragTargetState.dropTargets.remove(dropTargetState)
                                dropTargetState.dragTargets.remove(dragTargetState)
                            }
                        }

                        dropTargetState.updateState()
                    }

                    if (statusState.value == DragTargetStatus.DROPPED && options.snapPosition != null) {
                        val lastDropTargetRect = dragTargetState.dropTargets.last().globalRect
                        if (dragTargetRect == Rect.Zero) {
                            offsetState.value = Offset.Zero
                        } else {
                            val snapFromOffset =
                                options.snapPosition.calculateOffset(dragTargetRect)
                            val snapToOffset =
                                options.snapPosition.calculateOffset(lastDropTargetRect)
                            val remainingOffset = snapFromOffset - snapToOffset
                            if (remainingOffset.x.roundToInt() != 0 || remainingOffset.y.roundToInt() != 0) {
                                val snapOffset = Offset(
                                    x = remainingOffset.x / options.onDragScaleX,
                                    y = remainingOffset.y / options.onDragScaleY
                                )
                                offsetState.value -= snapOffset
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            statusState.value = DragTargetStatus.DRAGGED
                            dragTargetState.dropTargets.forEach { dropTarget ->
                                dropTarget.onDragTargetRemoved(data)
                            }
                        },
                        onDragEnd = {
                            if (dragTargetState.dropTargets.isEmpty()) {
                                statusState.value = DragTargetStatus.NONE
                                offsetState.value = Offset.Zero
                            } else {
                                statusState.value = DragTargetStatus.DROPPED
                                dragTargetState.dropTargets.forEach { dropTarget ->
                                    dropTarget.onDragTargetAdded(data)
                                }
                            }
                        },
                        onDragCancel = {
                            dragTargetState.resetState()
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetState.value += dragAmount
                        }
                    )
                }
        ) {
            content(statusState.value)
        }
    }

    private inner class DropTargetState(
        var globalRect: Rect = Rect.Zero,
        val onDragTargetAdded: (T) -> Unit = {},
        val onDragTargetRemoved: (T) -> Unit = {},
        val options: DropOptions = DropOptions(),
        val status: MutableState<DropTargetStatus> = mutableStateOf(DropTargetStatus.NONE),
        val dragTargets: MutableSet<DragTargetState> = mutableSetOf()
    ) {
        fun updateState() {
            var hasDraggedTargets = false
            var hasDroppedTargets = false

            dragTargets.forEach {
                when (it.status.value) {
                    DragTargetStatus.DRAGGED -> hasDraggedTargets = true
                    DragTargetStatus.DROPPED -> hasDroppedTargets = true
                    DragTargetStatus.NONE -> {}
                }
            }
            status.value = if (hasDraggedTargets) {
                DropTargetStatus.HOVERED
            } else if (hasDroppedTargets) {
                DropTargetStatus.DROPPED
            } else {
                DropTargetStatus.NONE
            }
        }

        fun detachFromDragTargets() {
            dragTargets.forEach { it.dropTargets.remove(this) }
            dragTargets.clear()
        }

        fun onDispose() = detachFromDragTargets()
    }

    private val dropTargetStates = mutableListOf<DropTargetState>()

    @Composable
    private fun rememberDropTargetState(
        onDragTargetAdded: (T) -> Unit,
        onDragTargetRemoved: (T) -> Unit,
        options: DropOptions = DropOptions(),
        content: @Composable (DropTargetStatus) -> Unit
    ): DropTargetState {
        val dropTargetState =
            remember(onDragTargetAdded, onDragTargetRemoved, options, content) {
                DropTargetState(
                    onDragTargetAdded = onDragTargetAdded,
                    onDragTargetRemoved = onDragTargetRemoved,
                    options = options
                )
            }
        DisposableEffect(onDragTargetAdded, onDragTargetRemoved, options, content) {
            dropTargetStates.add(dropTargetState)
            onDispose {
                dropTargetState.onDispose()
                dropTargetStates.remove(dropTargetState)
            }
        }
        return dropTargetState
    }

    @Composable
    fun DropTarget(
        onDragTargetAdded: (T) -> Unit,
        onDragTargetRemoved: (T) -> Unit = {},
        options: DropOptions = DropOptions(),
        content: @Composable (DropTargetStatus) -> Unit
    ) {
        val dropTargetState =
            rememberDropTargetState(onDragTargetAdded, onDragTargetRemoved, options, content)
        Box(
            modifier = Modifier.onGloballyPositioned {
                dropTargetState.globalRect = it.boundsInWindow()
            }
        ) {
            content(dropTargetState.status.value)
        }
    }
}

@Composable
fun <T> withDragContext(context: DragContext<T>, body: @Composable DragContext<T>.() -> Unit) {
    context.body()
}