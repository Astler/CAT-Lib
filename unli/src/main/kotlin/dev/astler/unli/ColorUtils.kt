package dev.astler.unli

import android.graphics.Color
import android.text.Html
import androidx.appcompat.app.AppCompatActivity

fun Int.darkenColor(): Int {
    if (this == Color.WHITE) {
        return -2105377
    } else if (this == Color.BLACK) {
        return Color.BLACK
    }

    val DARK_FACTOR = 8
    var hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    val hsl = hsv2hsl(hsv)
    hsl[2] -= DARK_FACTOR / 100f
    if (hsl[2] < 0)
        hsl[2] = 0f
    hsv = hsl2hsv(hsl)
    return Color.HSVToColor(hsv)
}

private fun hsl2hsv(hsl: FloatArray): FloatArray {
    val hue = hsl[0]
    var sat = hsl[1]
    val light = hsl[2]
    sat *= if (light < .5) light else 1 - light
    return floatArrayOf(hue, 2f * sat / (light + sat), light + sat)
}

private fun hsv2hsl(hsv: FloatArray): FloatArray {
    val hue = hsv[0]
    val sat = hsv[1]
    val value = hsv[2]

    val newHue = (2f - sat) * value
    var newSat = sat * value / if (newHue < 1f) newHue else 2f - newHue
    if (newSat > 1f)
        newSat = 1f

    return floatArrayOf(hue, newSat, newHue / 2f)
}

//fun AppCompatActivity.updateActionBarTitle(text: String, color: Int = appPrefs.primaryColor) {
//    supportActionBar?.title = Html.fromHtml("<font color='${color.getContrastColor().toHex()}'>$text</font>")
//}
//
//fun AppCompatActivity.updateActionBarSubtitle(text: String) {
//    supportActionBar?.subtitle = Html.fromHtml("<font color='${appPrefs.primaryColor.getContrastColor().toHex()}'>$text</font>")
//}