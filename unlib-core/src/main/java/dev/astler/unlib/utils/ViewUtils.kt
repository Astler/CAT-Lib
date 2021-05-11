package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun ImageView.setColorTintDrawable(@DrawableRes icon: Int, @ColorRes colorId: Int) {
    setImageDrawable(context.tintDrawable(icon, colorId))
}

fun ImageView.setAttrTintDrawable(@DrawableRes icon: Int, @AttrRes attrId: Int) {
    setImageDrawable(context.tintDrawableByAttr(icon, attrId))
}

fun RecyclerView.hideFABOnScroll(fab: FloatingActionButton) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0)
                fab.hide()
            else if (dy < 0)
                fab.show()
        }
    })
}

fun NestedScrollView.hideFABOnScroll(fab: FloatingActionButton) {
    setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        if (scrollY > oldScrollY) {
            fab.hide()
        } else {
            fab.show()
        }
    }
}

fun Activity.showKeyboard(editText: EditText) {
    if (editText.requestFocus()) {
        val inputMethod =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Activity.hideKeyboard() {
    //Находим View с фокусом, так мы сможем получить правильный window token
    //Если такого View нет, то создадим одно, это для получения window token из него
    val view = currentFocus ?: View(this)
    val inputMethod =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(
        view.windowToken,
        InputMethodManager.SHOW_IMPLICIT
    )
}

fun Context.hideKeyboardFrom(view: View?) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.showViewWithCondition(pCondition: Boolean) {
    if (pCondition) {
        showView()
    }
    else {
        goneView()
    }
}

fun View.goneView() {
    visibility = View.GONE
}

fun Context.simpleTextChip(@StringRes pTextId: Int): View {
    return simpleTextChip(getString(pTextId))
}

fun Context.simpleTextChip(pText: String): View {
    val mChip = Chip(this)
    mChip.text = pText

    return mChip
}

fun View.getScreenShotBitmap(): Bitmap? {
    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        draw(canvas)
    } catch (e: Exception) {
        infoLog("UNLIB: Failed to capture screenshot because:" + e.message)
    }
    return screenshot
}