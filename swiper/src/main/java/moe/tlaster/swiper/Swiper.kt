package moe.tlaster.swiper

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Swiper(
    modifier: Modifier = Modifier,
    state: SwiperState,
    orientation: Orientation = Orientation.Vertical,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    direction: Direction = Direction.Up,
    dismissHeight: Float = DismissHeight.HALF,
    animDurationMillis: Int = 500,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(
        modifier = modifier,
    ) {
        LaunchedEffect(constraints.maxHeight) {
            state.maxHeight = constraints.maxHeight
            state.direction = direction
            state.dismissHeight = dismissHeight
            state.animDuration = animDurationMillis
        }
        Layout(
            modifier = Modifier.draggable(
                orientation = orientation,
                enabled = enabled && !state.dismissed,
                reverseDirection = reverseDirection,
                onDragStopped = { velocity ->
                    scope.launch {
                        state.fling(velocity)
                    }
                },
                onDragStarted = {
                    state.onStart.invoke()
                },
                state = DraggableState { dy ->
                    scope.launch {
                        with(state) {
                            snap(offset + dy)
                        }
                    }
                },
            ),
            content = content,
        ) { measureScope, constraints ->
            layout(constraints.maxWidth, constraints.maxHeight) {
                val offset = state.offset
                val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
                measureScope
                    .map {
                        it.measure(childConstraints)
                    }
                    .forEach { placeable ->
                        // TODO: current this centers each page. We should investigate reading
                        //  gravity modifiers on the child, or maybe as a param to Swiper.
                        val xCenterOffset = (constraints.maxWidth - placeable.width) / 2
                        val yCenterOffset = (constraints.maxHeight - placeable.height) / 2
                        placeable.place(
                            x = xCenterOffset,
                            y = yCenterOffset + offset.roundToInt(),
                        )
                    }
            }
        }
    }
}
