package appsquared.votings.app

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_news.*
import kotlin.math.roundToInt


class NewsActivity : AppCompatActivity() {

    private lateinit var mNews: Model.News
    private var mImageViewHeaderHeight: Int = 0
    var mWorkspace: Model.WorkspaceResponse = Model.WorkspaceResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        mWorkspace = AppData().getSavedObjectFromPreference(this, PreferenceNames.WORKSPACE, Model.WorkspaceResponse::class.java)
            ?: Model.WorkspaceResponse()

        intent.extras?.let {

            mNews = it["news_item"] as Model.News

            imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (mNews.headerImageUrl.isNotEmpty()) {
                        imageViewHeader.visibility = View.VISIBLE
                        Picasso.get()
                            .load(mNews.headerImageUrl)
                            .into(imageViewHeader)
                    } else imageViewHeader.visibility = View.GONE

                    mImageViewHeaderHeight = imageViewHeader.height //height is ready
                    if (imageViewHeader.visibility == View.GONE) mImageViewHeaderHeight = 0

                    childOnlyMethod()
                }
            })
        }


    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun childOnlyMethod() {

        val workspace = mWorkspace

        val spacing = dpToPx(16)
        scrollView.setPadding(spacing, spacing + mImageViewHeaderHeight, spacing, spacing)

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
        if(workspace.settings.contentBorderWidth.isNotEmpty()) borderWidth = workspace.settings.contentBorderWidth.toDouble().roundToInt()

        if(workspace.settings.contentTextColor.isNotEmpty()) contentTextColor = convertStringToColor(workspace.settings.contentTextColor)
        if(workspace.settings.contentAccentColor.isNotEmpty()) contentAccentColor = convertStringToColor(workspace.settings.contentAccentColor)

        if(workspace.settings.contentBackgroundColor.isNotEmpty()) contentBackgroundColor = convertStringToColor(workspace.settings.contentBackgroundColor)
        if(workspace.settings.contentCornerRadius.isNotEmpty()) contentCornerRadius = workspace.settings.contentCornerRadius.toDouble().roundToInt()

        materialCardView.setCardBackgroundColor(contentBackgroundColor)
        materialCardView.strokeColor = borderColor
        materialCardView.strokeWidth = borderWidth
        materialCardView.radius = dpToPx(contentCornerRadius).toFloat()


        textViewNewsTitle.setTextColor(contentAccentColor)
        textViewNewsTitle.text = mNews.title

        textViewNewsSubTitle.setTextColor(contentAccentColor)
        textViewNewsSubTitle.text = mNews.subTitle

        textViewNewsContent.setTextColor(contentTextColor)
        textViewNewsContent.text = mNews.content
    }
}
