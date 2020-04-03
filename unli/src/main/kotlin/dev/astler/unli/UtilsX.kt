package dev.astler.unli

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class UtilsX {
    companion object {
        fun getIdFromStringName(context: Context, stringName: String): Int {
            val id = context.resources.getIdentifier(stringName, "string", context.packageName)
            return if (id != 0) id else R.string.nothing
        }

        fun getStringFromString(
            context: Context,
            stringName: String,
            returnIfNull: String = stringName
        ): String {
            val stringId =
                context.resources.getIdentifier(stringName, "string", context.packageName)
            return if (stringId != 0) context.getString(stringId) else returnIfNull
        }

        fun log(text: String, additionalName: String = "") {
            Log.i("ForAstler $additionalName", text)
        }

        @Suppress("DEPRECATION")
        fun rateIntentForUri(url: String, context: Context): Intent {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(String.format("%s?id=%s", url, context.packageName))
            )

            var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK

            flags = if (Build.VERSION.SDK_INT >= 21) {
                flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            } else {
                flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
            }

            intent.addFlags(flags)

            return intent
        }

        fun showKeyboard(activity: AppCompatActivity, editText: EditText) {
            if (editText.requestFocus()) {
                val inputMethod =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethod.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideKeyboard(activity: Activity) {
            //Находим View с фокусом, так мы сможем получить правильный window token
            //Если такого View нет, то создадим одно, это для получения window token из него
            val view = activity.currentFocus ?: View(activity)
            val inputMethod =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        fun hideKeyboardFrom(context: Context, view: View?) {
            val imm =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        fun isDarkTheme(activity: Activity): Boolean {
            return activity.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }

        fun tintDrawable(
            context: Context,
            @DrawableRes icon: Int,
            @ColorRes colorId: Int
        ): Drawable? {
            val drawable = ContextCompat.getDrawable(context, icon)

            drawable?.let {
                val color = ContextCompat.getColor(context, colorId)
                DrawableCompat.setTint(it, color)
            }

            return drawable
        }

        fun tintDrawableByAttr(
            context: Context,
            @DrawableRes icon: Int,
            @AttrRes attrId: Int
        ): Drawable? {
            val drawable = ContextCompat.getDrawable(context, icon)

            drawable?.let {
                val color = context.getColorFromAttr(attrId)
                DrawableCompat.setTint(it, color)
            }

            return drawable
        }
    }
}

fun Context.canShowAds(): Boolean =
    appPrefs.dayWithoutAds != GregorianCalendar.getInstance()
        .get(Calendar.DAY_OF_MONTH) && isOnline()

fun Context.rateApp() {
    try {
        val rateIntent = UtilsX.rateIntentForUri("market://details", this)
        startActivity(rateIntent)
    } catch (e: ActivityNotFoundException) {
        val rateIntent =
            UtilsX.rateIntentForUri("https://play.google.com/store/apps/details", this)
        startActivity(rateIntent)
    }
}

fun Context.moreApps() {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=4948748506238999540"))
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/dev?id=4948748506238999540")
        )
        startActivity(intent)
    }
}

fun ImageView.setColorTintDrawable(@DrawableRes icon: Int, @ColorRes colorId: Int) {
    setImageDrawable(UtilsX.tintDrawable(context, icon, colorId))
}

fun ImageView.setAttrTintDrawable(@DrawableRes icon: Int, @AttrRes attrId: Int) {
    setImageDrawable(UtilsX.tintDrawableByAttr(context, icon, attrId))
}

fun TextView.customTypeface(boldFont: Boolean = false) {
    typeface = if (boldFont) ResourcesCompat.getFont(context, R.font.google_sans_bold)
    else ResourcesCompat.getFont(context, R.font.google_sans_reg)
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

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

@Suppress("DEPRECATION")
fun Context.isOnline(): Boolean {

    val connMgr =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connMgr.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo?.type == ConnectivityManager.TYPE_MOBILE
    } else {
        val network = connMgr.activeNetwork

        if (network != null) {
            val networkCapabilities = connMgr.getNetworkCapabilities(network)

            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
    }

    return false
}