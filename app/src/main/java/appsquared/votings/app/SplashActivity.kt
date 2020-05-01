package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        setLightNavigationBar(window, true)
        setLightStatusBar(window, true)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}
