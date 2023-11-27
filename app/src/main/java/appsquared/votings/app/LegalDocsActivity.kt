package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.votings.android.R
import app.votings.android.databinding.ActivityWelcomeBinding


class LegalDocsActivity : BaseActivity() {

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

        val spacing = dpToPx(16)
        binding.scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        val workspace = mWorkspace

        val type = intent.extras?.getInt(LEGAL_DOC_TYPE) ?: 0

        val colorBlack = ContextCompat.getColor(this, R.color.black)

        when(type) {
            0 -> finish()
            IMPRINT -> {
                setScreenTitle(getString(R.string.title_imprint))
                setText(workspace.legalImprint, colorBlack, colorBlack)
            }
            PRIVACY -> {
                setScreenTitle(getString(R.string.title_privacy))
                setText(workspace.legalPrivacy, colorBlack, colorBlack)
            }
            TERMS -> {
                setScreenTitle(getString(R.string.title_terms_of_use))
                setText(workspace.legalTerms, colorBlack, colorBlack)
            }
        }
    }

    fun setText(text: String, contentAccentColor: Int, contentTextColor: Int) {
        PseudoMarkDown.styleTextView(text, binding.textViewContent, contentAccentColor, contentTextColor)
    }

    fun setText(file: Int, contentAccentColor: Int, contentTextColor: Int) {
        val res = resources
        val in_s = res.openRawResource(file)

        val b = ByteArray(in_s.available())
        in_s.read(b)
        binding.textViewContent.text = String(b)

        PseudoMarkDown.styleTextView(String(b), binding.textViewContent, contentAccentColor, contentTextColor)
    }

    companion object {
        val LEGAL_DOC_TYPE = "legal_doc_type"
        val ACCOUNT_REGISTER_TERMS = "account_register_terms"

        val IMPRINT = 1
        val PRIVACY = 2
        val TERMS = 3
    }
}
