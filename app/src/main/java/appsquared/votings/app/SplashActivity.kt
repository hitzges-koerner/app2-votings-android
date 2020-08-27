package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        setLightNavigationBar(window, false)
        setLightStatusBar(window, false)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java).putExtra("app_start", true))
            finish()
        }, 1000)
    }
}
