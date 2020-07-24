package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import appsquared.votings.app.views.DecisionDialog
import kotlin.math.roundToInt


class AccountRegistrationActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_registration)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle("Neuer Benutzer")
        setCancelButtonActive(true)

        val workspace = mWorkspace

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
    }

    override fun clickToolbarCancelButton() {
        super.clickToolbarCancelButton()

        DecisionDialog(this) {
            if (it == DecisionDialog.LEFT) return@DecisionDialog
            if (it == DecisionDialog.RIGHT) {
                finish()
            }
        }.generate()
            .setButtonRightName("Ja")
            .setButtonLeftName("Nein")
            .setMessage("Möchten Sie die Registrierung wirklich abbrechen")
            .show()
    }
}
