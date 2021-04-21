package com.vanpra.composematerialdialogs.datetime

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.defaultDecayAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @brief Interface used to pass data to children of ViewPager
 */
interface ViewPagerScope {
    /**
     * @brief Index of child
     * **/
    val index: Int

    /**
     * @brief Scroll viewpager to next page
     */
    suspend fun next()

    /**
     * @brief Scroll viewpager to previous page
     */
    suspend fun previous()

    /**
     * @brief Changes current index based on the value given
     */
    suspend fun plusPages(pages: Int)
}

private data class ViewPagerImpl(
    override val index: Int,
    val increment: suspend (Int) -> Unit,
    val moveBy: suspend (Int) -> Unit
) : ViewPagerScope {
    override suspend fun next() {
        increment(1)
    }

    override suspend fun previous() {
        increment(-1)
    }

    override suspend fun plusPages(pages: Int) {
        moveBy(pages)
    }
}

/**
 * @brief Basic ViewPage implementation in compose
 */
@Composable
fun ViewPager(
    indexRange: IntRange,
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    enabled: Boolean = true,
    content: @Composable ViewPagerScope.() -> Unit
) {
    BoxWithConstraints(Modifier.background(Color.Transparent)) {
        val coroutineScope = rememberCoroutineScope()
        val width = remember(constraints) { constraints.maxWidth.toFloat() }
        val offset = remember { Animatable(initialValue = 0f) }

        val draggableState = rememberDraggableState {
            coroutineScope.launch {
                val old = offset.value
                offset.snapTo(offset.value - it)
                offset.value - old
            }
        }

        val anchors = remember { listOf(-width, 0f, width) }
        val index = remember { mutableStateOf(0) }

        when {
            index.value == indexRange.first && index.value == indexRange.last -> offset.updateBounds(0f, 0f)
            index.value == indexRange.first -> offset.updateBounds(-width, 0f)
            index.value == indexRange.last -> offset.updateBounds(0f, width)
            else -> offset.updateBounds(-width, width)
        }

        val increment: suspend (Int) -> Unit = { increment: Int ->
            val animationResult = offset.animateTo(width * -increment)
            if (animationResult.endReason == AnimationEndReason.Finished ||
                animationResult.endReason == AnimationEndReason.BoundReached
            ) {
                if ((index.value + increment) in indexRange) {
                    index.value += increment
                    offset.snapTo(0f)
                }
            }
        }

        val moveBy: suspend (Int) -> Unit = { pages: Int ->
            if ((index.value + pages) in indexRange) {
                index.value += pages
                offset.snapTo(0f)
            }
        }

        val decayAnimation = defaultDecayAnimationSpec()
        val draggable = remember {
            modifier.draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    val initialTarget =
                        decayAnimation.calculateTargetValue(offset.value, -2f * velocity)
                    val target = anchors.minByOrNull { abs(it - initialTarget) } ?: 0f
                    val flingResult = offset.animateTo(target, spring())
                    offset.snapTo(0f)

                    if (flingResult.endReason == AnimationEndReason.Finished) {
                        if (flingResult.endState.value < 0) {
                            if ((index.value + 1) in indexRange) {
                                index.value += 1
                                onNext()
                            }
                        } else if (flingResult.endState.value > 0) {
                            if ((index.value - 1) in indexRange) {
                                index.value -= 1
                                onPrevious()
                            }
                        }
                    }
                },
                reverseDirection = true,
                enabled = enabled
            )
        }

        Layout(
            content = {
                val shownIndexes = remember(offset.value) {
                    when {
                        offset.value < 0 -> listOf(0, 1)
                        offset.value > 0 -> listOf(-1, 0)
                        else -> listOf(0)
                    }
                }

                shownIndexes.forEach { x ->
                    Column(
                        Modifier
                            .width(this@BoxWithConstraints.maxWidth)
                            .layoutId(x)
                    ) {
                        if (index.value in indexRange) {
                            val viewPagerImpl = ViewPagerImpl(index.value + x, increment, moveBy)
                            content(viewPagerImpl)
                        }
                    }
                }
            },
            modifier = draggable
        ) { measurables, constraints ->
            val placeables = measurables.map { it.layoutId to it.measure(constraints) }
            val height = placeables.maxByOrNull { it.second.height }?.second?.height ?: 0

            layout(constraints.maxWidth, height) {
                placeables.forEach { (layoutId, placeable) ->
                    placeable.place(
                        x = offset.value.toInt() + (layoutId as Int) * constraints.maxWidth,
                        y = 0
                    )
                }
            }
        }
    }
}
