package appsquared.votings.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import java.util.*

fun shouldAddNavigationBarPadding(context: Context): Boolean {
    return isInPortrait(context) && hasSystemNavigationBar(context)
}

fun getSystemNavigationBarHeight(context: Context): Int {
    val res = context.resources

    val id = res.getIdentifier("navigation_bar_height", "dimen", "android")
    var height = 0

    if (id > 0) {
        height = res.getDimensionPixelSize(id)
    }

    return height
}

fun hasSystemNavigationBar(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val d = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    } else {
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }
}

private fun isInPortrait(context: Context): Boolean {
    return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun atLeastMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun atLeastLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun getSystemVersion(): String {
    return "AND_" + Build.VERSION.RELEASE.toString()
}

fun getAppVersion(): String {
    return BuildConfig.VERSION_NAME
}

fun getDeviceId(context: Context): String {
    var emul = ""
    // Check running emul
    if (context.getApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
        emul = "DEV-AND_"
    }

    val deviceId = UUID.randomUUID().toString().toUpperCase()
    return emul + deviceId
}
