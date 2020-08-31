package appsquared.votings.app

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @param view
 * @param color
 */
fun setTextColor(view: TextView, color: Int) {
    view.setTextColor(color)
}

/**
 *
 * @param view
 * @param color
 */
fun setBackgroundColor(view: View, color: Int) {
    view.setBackgroundColor(color)
}

/**
 *
 * @param view
 * @param drawable
 */
fun setBackgroundImage(view: ImageView, drawable: Drawable) {
    view.setImageDrawable(drawable)
}

/**
 *
 * @param view
 * @param bitmap
 */
fun setBackgroundImage(view: ImageView, bitmap: Bitmap) {
    view.setImageBitmap(bitmap)
}

/**
 *
 * @param view
 * @param drawable
 */
fun setBackgroundImage(view: View, drawable: Drawable) {
    view.background = drawable
}

/**
 * TODO
 * @param window
 * @param color
 */
fun setNavigationBarBackgroundColor(window: Window, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }
}

/**
 * TODO
 * @param window
 */
fun setNavigationBarTransparent(window: Window) {
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * TODO
 * @param window
 * @param color
 */
fun setStatusBarBackgroundColor(window: Window, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}

/**
 * TODO
 * @param window
 */
fun setStatusBarTransparent(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
    }
}

/**
 * TODO
 * @param contentLayout
 * @param color
 */
fun setTextColorTextViewAll(contentLayout: ViewGroup, color: Int) {
    for (child in contentLayout.children) {
        if (child is TextView) (child).setTextColor(color)
        else if(child is ViewGroup) setTextColorTextViewAll(child, color)
    }
}

/**
 * TODO
 * @param contentLayout
 * @param color
 */
fun setTextColorAll(contentLayout: ViewGroup, color: Int) {
    for (child in contentLayout.children) {
        Log.d("View", child.toString())
    }
}

/**
 * TODO
 * @param context
 * @param color
 */
fun setNavigationButtonColor(context: Context, color: Int, drawableResource: Int) : Drawable {
    val backArrow = ContextCompat.getDrawable(context, drawableResource)
    backArrow!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return  backArrow
}

/**
 * TODO
 * @param window
 * @param lightStatusBar
 */
fun setLightStatusBar(window: Window, lightStatusBar: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        setLightBar(window, lightStatusBar, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
}

/**
 * TODO
 * @param window
 * @param lightNavigationBar
 */
fun setLightNavigationBar(window: Window, lightNavigationBar: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        setLightBar(window, lightNavigationBar, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
}

/**
 * TODO
 * @param window
 * @param light
 * @param systemUiFlag
 */
private fun setLightBar(window: Window, light: Boolean, systemUiFlag: Int) {
    var vis = window.decorView.systemUiVisibility
    if (light) {
        vis = vis or systemUiFlag
    } else {
        vis = vis and systemUiFlag.inv()
    }
    window.decorView.systemUiVisibility = vis
}

/**
 * TODO
 * @param color
 */
fun isLight(color: Int) : Boolean {
    return ColorUtils.calculateLuminance(color) > 0.8
}

fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()
fun dpToPxFloat(dp: Int) = (dp * Resources.getSystem().displayMetrics.density)

fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).toInt()

fun displayMetricsName() : String {
    when(Resources.getSystem().displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> return "ldpi"
        DisplayMetrics.DENSITY_MEDIUM -> return "mdpi"
        DisplayMetrics.DENSITY_HIGH -> return "hdpi"
        DisplayMetrics.DENSITY_XHIGH -> return "xhdpi"
        DisplayMetrics.DENSITY_XXHIGH -> return "xxhdpi"
        DisplayMetrics.DENSITY_XXXHIGH -> return "xxxhdpi"
    }
    return ""
}

/**
 * @return height in dp
 * @param width
 * @param height
 */
fun calculateViewHeight(context: Context, width: Int, height: Int): Int {
    val temp = displayWidthInPx(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).toFloat() / width.toFloat()
    return (temp * height).toInt()
}

fun displayWidthInPx(window: WindowManager) : Int {
    val display = window.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun displayHeightInPx(window: WindowManager) : Int {
    val display = window.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun viewFadeOut(view: View) {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.duration = 500

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeOut)
    view.animation = animation

    Handler().postDelayed({
        view.visibility = View.INVISIBLE
    }, 500)
}

fun viewFadeIn(view: View) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator() //add this
    fadeIn.duration = 500

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeIn)
    view.animation = animation

    Handler().postDelayed({
        view.visibility = View.VISIBLE
    }, 500)
}

fun viewFadeOut(view: View, timeInMs: Long) {
    if(!view.isVisible) return
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.duration = timeInMs

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeOut)
    view.animation = animation

    Handler().postDelayed({
        view.visibility = View.INVISIBLE
    }, timeInMs)
}

fun viewFadeIn(view: View, timeInMs: Long) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator() //add this
    fadeIn.duration = timeInMs

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeIn)
    view.animation = animation

    Handler().postDelayed({
        view.visibility = View.VISIBLE
    }, timeInMs)
}

fun convertStringToColor(color: String) : Int {
    val colorTemp = color.replace("(", "").replace(")", "").replace(" ", "")
    val result = colorTemp.split(",")
    val red = Integer.valueOf(result[0])
    val green = Integer.valueOf(result[1])
    val blue = Integer.valueOf(result[2])
    val a = result[3].toDouble()
    return Color.argb( (a*255).toInt(), red, green, blue)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toastLong(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.toastShort(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun getResId(resName: String, c: Class<*>): Int {
    try {
        val idField = c.getDeclaredField(resName)
        return idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        return -1
    }
}

fun ratingSumAndCountToStars(ratingSum: String, ratingCount: String) : Int {

    // Converts the given sum and count of all ratings
    // into an average value for the stars display
    val count = ratingCount.toInt()
    val sum = ratingSum.toInt()

    // Avoid division by 0:
    if(count == 0) return 0
    return sum/count
}

val dateFormateStringArray = arrayOf("yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd HH:mm:ss.SSS", "EEE MMM dd HH:mm:ss Z yyyy")

fun parseStringToDate(time: String): Date? {

    var date: Date? = null

    for (string in dateFormateStringArray) {
        val sdf = SimpleDateFormat(string)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            date = sdf.parse(time)
            break
        } catch (e: Exception) {
            Log.d("dateformatter", e.stackTrace.toString())
        }
    }
    return date
}

fun getLocalDateStyle(time: String, context: Context) : String {

    val date = parseStringToDate(time) ?: return time

    // https://stackoverflow.com/a/47098904
    // SimpleDateFormat.getDateTimeInstance()
    val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())
    val s = dateFormat.format(date)
    return s
}

fun getTimeDifference(time: String) : Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val date = sdf.parse(time)
    val timeCountdown = date.time

    val timeNow = System.currentTimeMillis()

    return (timeCountdown - timeNow)
}

fun converToReadableDistance(distanceInMeter: String) : String {
    var textDistance = ""
    if(distanceInMeter.isNotEmpty()) {
        val distance = distanceInMeter.toInt()
        textDistance = when(distance) {
            in 0 .. 1 -> "hier"
            in 2 .. 999 -> "${distance} m"
            in 1000 .. 50000 -> "${distance/1000} km"
            else -> "> 50 km"
        }
    }
    return textDistance
}

fun getCleanImageNameOld(icon: String) : String {
    var iconTemp = icon
    if(iconTemp.contains("/")) iconTemp = iconTemp.substring(iconTemp.lastIndexOf("/") + 1)
    if(iconTemp.contains("@")) iconTemp = iconTemp.substringBefore("@")
    if(iconTemp.contains("-")) iconTemp = iconTemp.replace("-", "_")
    if(iconTemp.contains(".")) iconTemp = iconTemp.substring(0, iconTemp.indexOf("."))
    return iconTemp.toLowerCase()
}

fun getCleanImageName(icon: String) : String {
    var iconTemp = icon
    iconTemp = iconTemp.substringAfterLast("/")
    iconTemp = iconTemp.substringBefore("@")
    if(iconTemp.contains("-")) iconTemp = iconTemp.replace("-", "_")
    if(iconTemp.contains(" ")) iconTemp = iconTemp.replace(" ", "_")
    iconTemp = iconTemp.substringBefore(".")
    iconTemp = iconTemp.toLowerCase()
    return iconTemp.toLowerCase()
}

fun isEmailValid(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}