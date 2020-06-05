package appsquared.votings.app

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_changelog_card_view.*
import kotlinx.android.synthetic.main.settings_impress_card_view.*
import kotlinx.android.synthetic.main.settings_notification_card_view.*
import kotlinx.android.synthetic.main.settings_privacy_card_view.*
import kotlinx.android.synthetic.main.settings_terms_card_view.*
import kotlin.math.roundToInt


class SettingsActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle("Einstellungen")

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
                    contentAccentColor = getColorTemp(R.color.black)
                    contentBackgroundColor = getColorTemp(R.color.colorAccent)
                    contentCornerRadius = 20
                }
            }
        }

        if(workspace.settings.contentBorderColor.isNotEmpty()) borderColor = convertStringToColor(workspace.settings.contentBorderColor)
        if(workspace.settings.contentBorderWidth.isNotEmpty()) borderWidth = workspace.settings.contentBorderWidth.toInt()

        if(workspace.settings.contentTextColor.isNotEmpty()) contentTextColor = convertStringToColor(workspace.settings.contentTextColor)
        if(workspace.settings.contentAccentColor.isNotEmpty()) contentAccentColor = convertStringToColor(workspace.settings.contentAccentColor)

        if(workspace.settings.contentBackgroundColor.isNotEmpty()) contentBackgroundColor = convertStringToColor(workspace.settings.contentBackgroundColor)
        if(workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius = workspace.settings.contentCornerRadius.toDouble().roundToInt()

        textViewChangelog.setTextColor(contentTextColor)
        textViewNotification.setTextColor(contentTextColor)
        textViewImpress.setTextColor(contentTextColor)
        textViewPrivacy.setTextColor(contentTextColor)
        textViewTerms.setTextColor(contentTextColor)

        textViewChangelog.text = "Changelog"
        textViewNotification.text = "Benachrichtigungen"
        textViewImpress.text = "Impressum"
        textViewPrivacy.text = "Datenschutz"
        textViewTerms.text = "Nutzungsbedingungen"

        imageViewIconChangelog.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconNotification.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconImpress.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconPrivacy.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconTerms.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)

        imageViewCaretChangelog.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretImpress.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretPrivacy.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretTerms.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)

        materialCardViewChangelog.strokeColor = borderColor
        materialCardViewChangelog.strokeWidth = borderWidth
        materialCardViewChangelog.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewChangelog.radius = contentCornerRadius.toFloat()

        materialCardViewImpress.strokeColor = borderColor
        materialCardViewImpress.strokeWidth = borderWidth
        materialCardViewImpress.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewImpress.radius = contentCornerRadius.toFloat()

        materialCardViewPrivacy.strokeColor = borderColor
        materialCardViewPrivacy.strokeWidth = borderWidth
        materialCardViewPrivacy.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewPrivacy.radius = contentCornerRadius.toFloat()

        materialCardViewTerms.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewTerms.strokeColor = borderColor
        materialCardViewTerms.strokeWidth = borderWidth
        materialCardViewTerms.radius = contentCornerRadius.toFloat()

        materialCardViewNotification.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewNotification.strokeColor = borderColor
        materialCardViewNotification.strokeWidth = borderWidth
        materialCardViewNotification.radius = contentCornerRadius.toFloat()

        materialCardViewNotification.setOnClickListener {
            switchNotification.isChecked = !switchNotification.isChecked
        }

        materialCardViewChangelog.setOnClickListener {
            startActivity(Intent(this, ChangelogActivity::class.java))
        }

        materialCardViewImpress.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.IMPRESS))
        }

        materialCardViewPrivacy.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.PRIVACY))
        }

        materialCardViewTerms.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.TERMS))
        }
    }
}
