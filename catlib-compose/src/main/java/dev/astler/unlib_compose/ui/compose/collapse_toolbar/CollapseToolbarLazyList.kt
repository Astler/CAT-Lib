package dev.astler.unlib_compose.ui.compose.collapse_toolbar

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import dev.astler.catlib.compose.R
import dev.astler.unlib_compose.ui.compose.collapse_toolbar.management.states.toolbar.ToolbarState
import dev.astler.unlib_compose.ui.compose.collapse_toolbar.management.states.toolbar.scrollflags.EnterAlwaysCollapsedState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

private val MinToolbarHeight = 96.dp
private val MaxToolbarHeight = 176.dp

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = EnterAlwaysCollapsedState.Saver) {
        EnterAlwaysCollapsedState(toolbarHeightRange)
    }
}

@Composable
fun CollapsingToolbarContent(
    modifier: Modifier = Modifier,
    topReached: (() -> Boolean)? = null,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }

    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val scope = rememberCoroutineScope()

    var topReachedValue by remember { mutableStateOf(topReached?.invoke() ?: false) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                topReachedValue = topReached?.invoke() ?: false
                toolbarState.scrollTopLimitReached = topReachedValue
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0) {
                    scope.launch {
                        animateDecay(
                            initialValue = toolbarState.height + toolbarState.offset,
                            initialVelocity = available.y,
                            animationSpec = FloatExponentialDecaySpec()
                        ) { value, velocity ->
                            topReachedValue = topReached?.invoke() ?: false
                            toolbarState.scrollTopLimitReached = topReachedValue

                            toolbarState.scrollOffset =
                                toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        content(Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { scope.coroutineContext.cancelChildren() }
                )
            })

        CollapsingToolbar(
            backgroundImageResId = if (topReachedValue) R.drawable.banner else R.drawable.kb,
            progress = toolbarState.progress,
            onPrivacyTipButtonClicked = {},
            onSettingsButtonClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset }
        )

        if (topReachedValue) {
            Text(text = "TOP REACHED", modifier = Modifier.fillMaxWidth())
        }
    }
}