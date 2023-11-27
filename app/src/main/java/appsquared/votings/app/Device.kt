package appsquared.votings.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import app.votings.android.BuildConfig
import java.util.UUID

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
}

private fun isInPortrait(context: Context): Boolean {
    return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
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
