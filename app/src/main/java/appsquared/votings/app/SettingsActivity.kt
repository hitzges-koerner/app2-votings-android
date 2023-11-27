package appsquared.votings.app

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import app.votings.android.BuildConfig
import app.votings.android.R
import app.votings.android.databinding.ActivitySettingsBinding
import app.votings.android.databinding.SettingsChangelogCardViewBinding
import app.votings.android.databinding.SettingsImpressCardViewBinding
import app.votings.android.databinding.SettingsNotificationCardViewBinding
import app.votings.android.databinding.SettingsPrivacyCardViewBinding
import app.votings.android.databinding.SettingsTermsCardViewBinding
import app.votings.android.databinding.SettingsTutorialCardViewBinding
import appsquared.votings.app.tag.enums.Style
import com.google.android.material.card.MaterialCardView
import kotlin.math.roundToInt


class SettingsActivity : BaseActivity() {

    private var contentCornerRadius: Int = 20
    private var contentBackgroundColor: Int = getColorTemp(R.color.white)
    private var contentAccentColor: Int = getColorTemp(R.color.colorAccent)
    private var contentTextColor: Int = getColorTemp(R.color.black)
    private var borderWidth: Int = 0
    private var borderColor: Int = getColorTemp(R.color.transparent)

    var statusBarSize = 0

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var bindingChangelog: SettingsChangelogCardViewBinding
    private lateinit var bindingImpress: SettingsImpressCardViewBinding
    private lateinit var bindingNotification: SettingsNotificationCardViewBinding
    private lateinit var bindingPrivacy: SettingsPrivacyCardViewBinding
    private lateinit var bindingTerms: SettingsTermsCardViewBinding
    private lateinit var bindingTutorial: SettingsTutorialCardViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        bindingChangelog = binding.layoutSettingsChangelog
        bindingImpress = binding.layoutSettingsImpress
        bindingNotification = binding.layoutSettingsNotification
        bindingPrivacy = binding.layoutSettingsPrivacy
        bindingTerms = binding.layoutSettingsTerms
        bindingTutorial = binding.layoutSettingsTutorial
        setContentView(binding.root)
    }

    fun getColorTemp(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_settings))
        binding.textViewVersionName.text =
            "${getString(R.string.version)} ${BuildConfig.VERSION_NAME}"

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        binding.scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)


        when (workspace.settings.style) {
            Style.RICH -> {
                borderColor = getColorTemp(R.color.transparent)
                borderWidth = 0
                contentTextColor = getColorTemp(R.color.black)
                contentAccentColor = getColorTemp(R.color.colorAccent)
                contentBackgroundColor = getColorTemp(R.color.white_transparent)
                contentCornerRadius = 20
            }

            Style.MINIMAL -> {
                borderColor = getColorTemp(R.color.transparent)
                borderWidth = 0
                contentTextColor = getColorTemp(R.color.black)
                contentAccentColor = getColorTemp(R.color.colorAccent)
                contentBackgroundColor = getColorTemp(R.color.white)
                contentCornerRadius = 20
            }

            Style.CLEAN -> {
                borderColor = getColorTemp(R.color.transparent)
                borderWidth = 0
                contentTextColor = getColorTemp(R.color.white)
                contentAccentColor = getColorTemp(R.color.white)
                contentBackgroundColor = getColorTemp(R.color.colorAccent)
                contentCornerRadius = 20
            }

            else -> {}
        }

        if (workspace.settings.contentBorderColor.isNotEmpty()) borderColor =
            convertStringToColor(workspace.settings.contentBorderColor)
        if (workspace.settings.contentBorderWidth.isNotEmpty()) borderWidth =
            workspace.settings.contentBorderWidth.toInt()

        if (workspace.settings.contentTextColor.isNotEmpty()) contentTextColor =
            convertStringToColor(workspace.settings.contentTextColor)
        if (workspace.settings.contentAccentColor.isNotEmpty()) contentAccentColor =
            convertStringToColor(workspace.settings.contentAccentColor)

        if (workspace.settings.contentBackgroundColor.isNotEmpty()) contentBackgroundColor =
            convertStringToColor(workspace.settings.contentBackgroundColor)
        if (workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius =
            workspace.settings.contentCornerRadius.toDouble().roundToInt()


        spaghetti(
            bindingChangelog.textViewChangelog,
            bindingChangelog.imageViewIconChangelog,
            bindingChangelog.imageViewCaretChangelog,
            bindingChangelog.materialCardViewChangelog,
            getString(R.string.changelog)
        )
        spaghetti(
            bindingTutorial.textViewTutorial,
            bindingTutorial.imageViewIconTutorial,
            bindingTutorial.imageViewCaretTutorial,
            bindingTutorial.materialCardViewTutorial,
            getString(R.string.tutorial)
        )
        spaghetti(
            bindingNotification.textViewNotification,
            bindingNotification.imageViewIconNotification,
            null,
            bindingNotification.materialCardViewNotification,
            getString(R.string.tile_notifications)
        )
        spaghetti(
            bindingImpress.textViewImpress,
            bindingImpress.imageViewIconImpress,
            bindingImpress.imageViewCaretImpress,
            bindingImpress.materialCardViewImpress,
            getString(R.string.title_imprint)
        )
        spaghetti(
            bindingPrivacy.textViewPrivacy,
            bindingPrivacy.imageViewIconPrivacy,
            bindingPrivacy.imageViewCaretPrivacy,
            bindingPrivacy.materialCardViewPrivacy,
            getString(R.string.title_privacy)
        )
        spaghetti(
            bindingTerms.textViewTerms,
            bindingTerms.imageViewIconTerms,
            bindingTerms.imageViewCaretTerms,
            bindingTerms.materialCardViewTerms,
            getString(R.string.title_terms_of_use)
        )


        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val showNotification = pref.getBoolean(
            PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME,
            true
        )
        bindingNotification.switchNotification.isChecked = showNotification

        bindingNotification.materialCardViewNotification.setOnClickListener {
            bindingNotification.switchNotification.isChecked =
                !bindingNotification.switchNotification.isChecked
            pref.edit().putBoolean(
                PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME,
                bindingNotification.switchNotification.isChecked
            ).apply()
        }

        bindingChangelog.materialCardViewChangelog.setOnClickListener {
            startActivity(Intent(this, ChangelogActivity::class.java))
        }

        bindingTutorial.materialCardViewTutorial.setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        bindingImpress.materialCardViewImpress.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LegalDocsActivity::class.java
                ).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.IMPRINT)
            )
        }

        bindingPrivacy.materialCardViewPrivacy.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LegalDocsActivity::class.java
                ).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.PRIVACY)
            )
        }

        bindingTerms.materialCardViewTerms.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LegalDocsActivity::class.java
                ).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.TERMS)
            )
        }
    }

    private fun spaghetti(
        textView: TextView,
        imageViewIcon: ImageView,
        imageViewCaret: ImageView?,
        materialCardView: MaterialCardView,
        text: String
    ) {
        textView.setTextColor(contentTextColor)
        textView.text = text
        imageViewIcon.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)
        imageViewCaret?.setColorFilter(contentTextColor, PorterDuff.Mode.SRC_ATOP)

        materialCardView.strokeColor = borderColor
        materialCardView.strokeWidth = borderWidth
        materialCardView.setCardBackgroundColor(contentBackgroundColor)
        materialCardView.radius = contentCornerRadius.toFloat()
    }
}
