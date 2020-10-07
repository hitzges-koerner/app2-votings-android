package appsquared.votings.app

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlin.math.roundToInt


class WelcomeActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(R.string.tile_welcome)

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        val colorBlack = ContextCompat.getColor(this, R.color.black)

        if(workspace.welcome.text.isNotEmpty()) PseudoMarkDown.styleTextView(workspace.welcome.text, textViewContent, colorBlack, colorBlack)
        else setErrorView(getString(R.string.error_no_welcome_message))
    }
}
