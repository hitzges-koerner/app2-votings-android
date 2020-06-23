package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlin.math.roundToInt


class LegalDocsActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        var borderColor = getColorTemp(R.color.transparent)
        var borderWidth = 0
        var contentTextColor = getColorTemp(R.color.black)
        var contentAccentColor = getColorTemp(R.color.colorAccent)
        var contentBackgroundColor = getColorTemp(R.color.white)
        var contentCornerRadius = 20

        if(workspace.settings.style.isNotEmpty()) {
            when(workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    borderColor = getColorTemp(R.color.transparent)
                    borderWidth = 0
                    contentTextColor = getColorTemp(R.color.black)
                    contentAccentColor = getColorTemp(R.color.colorAccent)
                    contentBackgroundColor = getColorTemp(R.color.white_transparent)
                    contentCornerRadius = 20
                }

                "minimal" -> {
                    borderColor = getColorTemp(R.color.transparent)
                    borderWidth = 0
                    contentTextColor = getColorTemp(R.color.black)
                    contentAccentColor = getColorTemp(R.color.colorAccent)
                    contentBackgroundColor = getColorTemp(R.color.white)
                    contentCornerRadius = 20
                }

                "clean" -> {
                    borderColor = getColorTemp(R.color.transparent)
                    borderWidth = 0
                    contentTextColor = getColorTemp(R.color.white)
                    contentAccentColor = getColorTemp(R.color.white)
                    contentBackgroundColor = getColorTemp(R.color.colorAccent)
                    contentCornerRadius = 20
                }
            }
        }

        if(workspace.settings.contentBorderColor.isNotEmpty()) borderColor = convertStringToColor(workspace.settings.contentBorderColor)
        if(workspace.settings.contentBorderWidth.isNotEmpty()) borderWidth = workspace.settings.contentBorderWidth.toDouble().roundToInt()

        if(workspace.settings.contentTextColor.isNotEmpty()) contentTextColor = convertStringToColor(workspace.settings.contentTextColor)
        if(workspace.settings.contentAccentColor.isNotEmpty()) contentAccentColor = convertStringToColor(workspace.settings.contentAccentColor)

        if(workspace.settings.contentBackgroundColor.isNotEmpty()) contentBackgroundColor = convertStringToColor(workspace.settings.contentBackgroundColor)
        if(workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius = workspace.settings.contentCornerRadius.toDouble().roundToInt()

        materialCardView.setCardBackgroundColor(contentBackgroundColor)
        materialCardView.strokeColor = borderColor
        materialCardView.strokeWidth = borderWidth
        materialCardView.radius = dpToPx(contentCornerRadius).toFloat()

        val type = intent.extras?.getInt(LEGAL_DOC_TYPE) ?: 0

        when(type) {
            0 -> finish()
            IMPRINT -> {
                setScreenTitle("Impressum")
                setText(workspace.legalImprint, contentAccentColor, contentTextColor)
            }
            PRIVACY -> {
                setScreenTitle("Datenschutz")
                setText(workspace.legalPrivacy, contentAccentColor, contentTextColor)
            }
            TERMS -> {
                setScreenTitle("Terms of Use")
                setText(workspace.legalTerms, contentAccentColor, contentTextColor)
            }
        }
    }

    fun setText(text: String, contentAccentColor: Int, contentTextColor: Int) {
        PseudoMarkDown.styleTextView(text, textViewContent, contentAccentColor, contentTextColor)
    }

    fun setText(file: Int, contentAccentColor: Int, contentTextColor: Int) {
        val res = resources
        val in_s = res.openRawResource(file)

        val b = ByteArray(in_s.available())
        in_s.read(b)
        textViewContent.text = String(b)

        PseudoMarkDown.styleTextView(String(b), textViewContent, contentAccentColor, contentTextColor)
    }

    companion object {
        val LEGAL_DOC_TYPE = "legal_doc_type"

        val IMPRINT = 1
        val PRIVACY = 2
        val TERMS = 3
    }
}
