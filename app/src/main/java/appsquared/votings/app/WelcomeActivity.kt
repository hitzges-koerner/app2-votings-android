package appsquared.votings.app

import android.os.Bundle
import android.view.View.VISIBLE
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workspace = mWorkspace

        setContentView(R.layout.activity_welcome)

        val spacing = dpToPx(16)
        scrollView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)

        if(workspace.settings.backgroundColor.isNotEmpty()) constraintLayoutRoot.setBackgroundColor(convertStringToColor(workspace.settings.backgroundColor))
        if(workspace.settings.backgroundImageUrl.isNotEmpty()) {
            imageViewBackground.visibility = VISIBLE
            Picasso.get()
                .load(workspace.settings.backgroundImageUrl)
                .into(imageViewBackground)
        } else if(workspace.settings.style.equals("rich", true)) {
            imageViewBackground.visibility = VISIBLE
            imageViewBackground.setImageResource(R.drawable.image)
        }

        if(workspace.settings.backgroundColor.isNotEmpty()) constraintLayoutRoot.setBackgroundColor(convertStringToColor(workspace.settings.backgroundColor))
        if(workspace.settings.backgroundImageUrl.isNotEmpty()) {
            imageViewBackground.visibility = VISIBLE
            Picasso.get()
                .load(workspace.settings.backgroundImageUrl)
                .into(imageViewBackground)
        } else if(workspace.settings.style.equals("rich", true)) {
            imageViewBackground.visibility = VISIBLE
            imageViewBackground.setImageResource(R.drawable.image)
        }

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
        if(workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius = workspace.settings.contentCornerRadius.toInt()

        materialCardView.setCardBackgroundColor(contentBackgroundColor)
        materialCardView.strokeColor = borderColor
        materialCardView.strokeWidth = borderWidth
        materialCardView.radius = dpToPx(contentCornerRadius).toFloat()

        PseudoMarkDown.styleTextView(workspace.welcome.text, textViewContent, contentAccentColor, contentTextColor)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }
}
