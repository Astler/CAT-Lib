package dev.astler.unlib_compose.ui.compose.collapse_toolbar.management.states.toolbar

abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {

    final override val offset: Float = 0f

}