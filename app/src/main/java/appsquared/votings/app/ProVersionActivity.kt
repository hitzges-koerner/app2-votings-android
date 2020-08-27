package appsquared.votings.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import br.tiagohm.markdownview.css.ExternalStyleSheet
import kotlinx.android.synthetic.main.activity_pro.*
import kotlinx.android.synthetic.main.activity_welcome.scrollView


class ProVersionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_pro_version))

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        var borderColor = getColorTemp(R.color.transparent)
        var borderWidth = 0
        var contentTextColor = getColorTemp(R.color.black)
        var contentAccentColor = getColorTemp(R.color.colorAccent)
        var contentBackgroundColor = getColorTemp(R.color.white)
        var contentCornerRadius = 20

        //PseudoMarkDown.styleTextView(workspace.proVersionText.replace("##", "#"), textViewContent, contentAccentColor, contentTextColor)

        markdownView.addStyleSheet(ExternalStyleSheet.fromAsset("pro.css", ""))
        markdownView.loadMarkdown(workspace.proVersionText)

        buttonProLearnMore.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.votings.app/de/"))
            startActivity(browserIntent)
        }

    }
}
