package appsquared.votings.app

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.toolbar_custom.*


class WelcomeActivity : AppCompatActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val workspace: Model.WorkspaceResponse? = AppData().getSavedObjectFromPreference(this, "workspace", Model.WorkspaceResponse::class.java)

        textViewToolbar.text = "Wilkommen"
        imageViewLogo.visibility = View.GONE

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(toolbarCustom) { view, insets ->
            statusBarSize = insets.systemWindowInsetTop

            val params: ViewGroup.LayoutParams = toolbarCustom.layoutParams
            val temp = dpToPx(56)
            params.height = statusBarSize + temp
            toolbarCustom.requestLayout()
            //imageViewLogo.setPadding(0, statusBarSize, 0, 0)

            val lp = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            lp.setMargins(0, statusBarSize, 0,0)
            imageViewLogo.layoutParams = lp

            insets
        }

        if (workspace != null) {
            imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (workspace.main.headerImageUrl.isNotEmpty()) {
                        imageViewHeader.visibility = VISIBLE

                        imageViewHeader.layoutParams.height = calculateViewHeight(
                            this@WelcomeActivity,
                            workspace.main.headerImageSize.split(":")[0].toInt(),
                            workspace.main.headerImageSize.split(":")[1].toInt()
                        )
                        imageViewHeader.requestLayout()

                        Picasso.get()
                            .load(workspace.main.headerImageUrl)
                            .into(imageViewHeader)
                    } else if (workspace.settings.logoImageUrl.isNotEmpty()) {
                        imageViewHeader.visibility = VISIBLE
                        Picasso.get()
                            .load(workspace.settings.logoImageUrl)
                            .into(imageViewHeader)
                    } else imageViewHeader.visibility = GONE

                    var imageViewHeaderHeight = imageViewHeader.height //height is ready

                    if (imageViewHeader.visibility == GONE) imageViewHeaderHeight = 0

                    val spacing = dpToPx(16)
                    scrollView.setPadding(spacing, spacing + imageViewHeaderHeight, spacing, spacing)
                }
            })

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
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }
}
