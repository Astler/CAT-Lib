package dev.astler.unli

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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
        private fun rateIntentForUri(url: String, context: Context): Intent {
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

        fun rateIntent(context: Context) {
            try {
                val rateIntent = rateIntentForUri("market://details", context)
                context.startActivity(rateIntent)
            } catch (e: ActivityNotFoundException) {
                val rateIntent =
                    rateIntentForUri("https://play.google.com/store/apps/details", context)
                context.startActivity(rateIntent)
            }
        }

        fun showKeyboard(activity: AppCompatActivity, editText: EditText) {
            if (editText.requestFocus()) {
                val inputMethod =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethod.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus ?: View(activity)
            val inputMethod =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        fun hideKeyboardFrom(
            context: Context,
            view: View?
        ) {
            val imm =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        fun isDarkTheme(activity: Activity): Boolean {
            return activity.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }

        fun Context.getColorFromAttr(
            @AttrRes attrColor: Int,
            typedValue: TypedValue = TypedValue(),
            resolveRefs: Boolean = true
        ): Int {
            theme.resolveAttribute(attrColor, typedValue, resolveRefs)
            return typedValue.data
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

        fun canShowAds(context: Context): Boolean =
            context.appPrefs.dayWithoutAds == GregorianCalendar.getInstance()
                .get(Calendar.DAY_OF_MONTH) && isOnline(context)

        @Suppress("DEPRECATION")
        fun isOnline(context: Context): Boolean {

            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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
    }

}