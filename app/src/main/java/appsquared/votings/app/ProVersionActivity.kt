package appsquared.votings.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.votings.android.R
import app.votings.android.databinding.ActivityProBinding
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.core.CoreProps
import io.noties.markwon.core.MarkwonTheme
import org.commonmark.node.Heading


class ProVersionActivity : BaseActivity() {

    private lateinit var binding: ActivityProBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProBinding.inflate(layoutInflater)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_pro_version))

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        binding.scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        var borderColor = getColorTemp(R.color.transparent)
        var borderWidth = 0
        var contentTextColor = getColorTemp(R.color.black)
        var contentAccentColor = getColorTemp(R.color.colorAccent)
        var contentBackgroundColor = getColorTemp(R.color.white)
        var contentCornerRadius = 20

        //PseudoMarkDown.styleTextView(workspace.proVersionText.replace("##", "#"), textViewContent, contentAccentColor, contentTextColor)


        // obtain an instance of Markwon
        val markwon = Markwon.builder(this)
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    val origin = builder.requireFactory(Heading::class.java)
                    // register you own
                    builder.setFactory(Heading::class.java) { configuration, props -> // here you can also check for heading level for further customizations
                        val level = CoreProps.HEADING_LEVEL.require(props)
                        // return an array of spans (origin heading + our color)
                        arrayOf(
                            origin.getSpans(configuration, props),
                            ForegroundColorSpan(ContextCompat.getColor(this@ProVersionActivity, R.color.colorAccent))
                        )
                    }
                }
            })
            .usePlugin(object : AbstractMarkwonPlugin() {
                @Override
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder.headingBreakHeight(0);
                }
            }).build()


        // set markdown
        markwon.setMarkdown(binding.textViewContent, workspace.proVersionText)

        binding.buttonProLearnMore.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.votings.app/de/"))
            startActivity(browserIntent)
        }

    }
}
