package appsquared.votings.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children

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
        window.statusBarColor = Color.TRANSPARENT;
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
    return ColorUtils.calculateLuminance(color) > 0.8;
}

fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

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