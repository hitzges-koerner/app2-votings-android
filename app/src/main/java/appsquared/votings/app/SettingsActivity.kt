package appsquared.votings.app

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_changelog_card_view.*
import kotlinx.android.synthetic.main.settings_changelog_card_view.materialCardViewChangelog
import kotlinx.android.synthetic.main.settings_impress_card_view.*
import kotlinx.android.synthetic.main.settings_notification_card_view.*
import kotlinx.android.synthetic.main.settings_privacy_card_view.*
import kotlinx.android.synthetic.main.settings_terms_card_view.*
import kotlinx.android.synthetic.main.settings_tutorial_card_view.*
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

        setScreenTitle(getString(R.string.tile_settings))
        textViewVersionName.text = "${getString(R.string.version)} ${BuildConfig.VERSION_NAME}"

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
        if(workspace.settings.contentBorderWidth.isNotEmpty()) borderWidth = workspace.settings.contentBorderWidth.toInt()

        if(workspace.settings.contentTextColor.isNotEmpty()) contentTextColor = convertStringToColor(workspace.settings.contentTextColor)
        if(workspace.settings.contentAccentColor.isNotEmpty()) contentAccentColor = convertStringToColor(workspace.settings.contentAccentColor)

        if(workspace.settings.contentBackgroundColor.isNotEmpty()) contentBackgroundColor = convertStringToColor(workspace.settings.contentBackgroundColor)
        if(workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius = workspace.settings.contentCornerRadius.toDouble().roundToInt()

        textViewChangelog.setTextColor(contentTextColor)
        textViewTutorial.setTextColor(contentTextColor)
        textViewNotification.setTextColor(contentTextColor)
        textViewImpress.setTextColor(contentTextColor)
        textViewPrivacy.setTextColor(contentTextColor)
        textViewTerms.setTextColor(contentTextColor)

        textViewChangelog.text = getString(R.string.changelog)
        textViewTutorial.text = getString(R.string.tutorial)
        textViewNotification.text = getString(R.string.tile_notifications)
        textViewImpress.text = getString(R.string.title_imprint)
        textViewPrivacy.text = getString(R.string.title_privacy)
        textViewTerms.text = getString(R.string.title_terms_of_use)

        imageViewIconChangelog.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconTutorial.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconNotification.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconImpress.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconPrivacy.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewIconTerms.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)

        imageViewCaretChangelog.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretTutorial.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretImpress.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretPrivacy.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaretTerms.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)

        materialCardViewChangelog.strokeColor = borderColor
        materialCardViewChangelog.strokeWidth = borderWidth
        materialCardViewChangelog.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewChangelog.radius = contentCornerRadius.toFloat()

        materialCardViewTutorial.strokeColor = borderColor
        materialCardViewTutorial.strokeWidth = borderWidth
        materialCardViewTutorial.setCardBackgroundColor(contentBackgroundColor)
        materialCardViewTutorial.radius = contentCornerRadius.toFloat()

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


        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val showNotification = pref.getBoolean(PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME, true)
        switchNotification.isChecked = showNotification

        materialCardViewNotification.setOnClickListener {
            switchNotification.isChecked = !switchNotification.isChecked
            pref.edit().putBoolean(PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME, switchNotification.isChecked).apply()
        }

        materialCardViewChangelog.setOnClickListener {
            startActivity(Intent(this, ChangelogActivity::class.java))
        }

        materialCardViewTutorial.setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        materialCardViewImpress.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.IMPRINT))
        }

        materialCardViewPrivacy.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.PRIVACY))
        }

        materialCardViewTerms.setOnClickListener {
            startActivity(Intent(this, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.TERMS))
        }
    }
}
