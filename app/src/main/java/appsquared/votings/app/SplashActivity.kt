package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        setLightNavigationBar(window, false)
        setLightStatusBar(window, false)

        when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> {
                toastShort("ldpi")
            }
            DisplayMetrics.DENSITY_MEDIUM -> {
                toastShort("mdpi")
            }
            DisplayMetrics.DENSITY_HIGH -> {
                toastShort("hdpi")
            }
            DisplayMetrics.DENSITY_XHIGH -> {
                toastShort("xhdpi")
            }
            DisplayMetrics.DENSITY_XXHIGH -> {
                toastShort("xxhdpi")
            }
            DisplayMetrics.DENSITY_XXXHIGH -> {
                toastShort("xxxhdpi")
            }
        }

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java).putExtra("app_start", true))
            finish()
        }, 1000)
    }
}
