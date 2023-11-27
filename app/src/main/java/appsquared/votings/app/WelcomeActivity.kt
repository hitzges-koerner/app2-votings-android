package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.votings.android.R
import app.votings.android.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseActivity() {

    var statusBarSize = 0

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(R.string.tile_welcome)

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        binding.scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        val colorBlack = ContextCompat.getColor(this, R.color.black)

        if(workspace.welcome.text.isNotEmpty()) PseudoMarkDown.styleTextView(workspace.welcome.text, binding.textViewContent, colorBlack, colorBlack)
        else setErrorView(getString(R.string.error_no_welcome_message))
    }
}