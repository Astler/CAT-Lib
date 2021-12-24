package dev.astler.cat_ui.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

fun View.shake() {
    val animator1 = ObjectAnimator.ofFloat(this, "translationX", -50f)
    animator1.repeatCount = 0
    animator1.duration = 50

    val animator2 = ObjectAnimator.ofFloat(this, "translationX", 50f)
    animator2.repeatCount = 0
    animator2.duration = 50

    val animator3 = ObjectAnimator.ofFloat(this, "translationX", 0f)
    animator3.repeatCount = 0
    animator3.duration = 50

    val set = AnimatorSet()
    set.play(animator1).before(animator2)
    set.play(animator2).before(animator3)
    set.start()
}

fun View.toggle(): Boolean {
    return if (rotation == 0f) {
        animate().setDuration(200).rotation(180f)
        true
    } else {
        animate().setDuration(200).rotation(0f)
        false
    }
}
