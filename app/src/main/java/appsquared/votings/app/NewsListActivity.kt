package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appsquared.votings.app.views.CustomOnClickListener
import com.squareup.picasso.Picasso
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_news_list.*
import kotlinx.android.synthetic.main.activity_news_list.constraintLayoutRoot
import kotlinx.android.synthetic.main.activity_news_list.imageViewBackground
import kotlinx.android.synthetic.main.activity_news_list.imageViewHeader
import kotlinx.android.synthetic.main.activity_news_list.recyclerView
import kotlinx.android.synthetic.main.activity_news_list.toolbarCustom
import kotlinx.android.synthetic.main.tab_custom.*


class NewsListActivity : AppCompatActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        val workspace: Model.WorkspaceResponse? = AppData().getSavedObjectFromPreference(this, "workspace", Model.WorkspaceResponse::class.java)

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(toolbarCustom) { view, insets ->
            statusBarSize = insets.systemWindowInsetTop

            val params: ViewGroup.LayoutParams = toolbarCustom.layoutParams
            val temp = dpToPx(56)
            params.height = statusBarSize + temp
            toolbarCustom.requestLayout()

            insets
        }
        var attributes = Attributes()
        tabCustom.setCustomOnClickListener(object : CustomOnClickListener {
            override fun customOnClick(button: Int) {
                Toast.makeText(this@NewsListActivity, "Button: $button", Toast.LENGTH_LONG).show()
            }
        })

        if(workspace!!.settings.style.isNotEmpty()) {
            when(workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColor(getColorTemp(R.color.colorAccent), getColorTemp(R.color.white))
                    tabCustom.setTabTextColor(getColorTemp(R.color.white), getColorTemp(R.color.colorAccent))
                }

                "minimal" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 0

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.white)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColor(getColorTemp(R.color.colorAccent), getColorTemp(R.color.white))
                    tabCustom.setTabTextColor(getColorTemp(R.color.white), getColorTemp(R.color.colorAccent))
                }

                "clean" -> {
                    attributes = setAttributesDefault()

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColor(getColorTemp(R.color.colorAccent), getColorTemp(R.color.white))
                    tabCustom.setTabTextColor(getColorTemp(R.color.white), getColorTemp(R.color.colorAccent))
                }
                else -> {
                    attributes = setAttributesDefault()

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()
                }
            }
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

        val workspaceSettings = workspace.settings
        if(workspaceSettings.contentBackgroundColor.isNotEmpty())  attributes.contentBackgroundColor = convertStringToColor(workspaceSettings.contentBackgroundColor)
        if(workspaceSettings.contentBorderColor.isNotEmpty()) attributes.contentBorderColor = convertStringToColor(workspaceSettings.contentBorderColor)
        if(workspaceSettings.contentBorderWidth.isNotEmpty()) attributes.contentBorderWidth = workspaceSettings.contentBorderWidth.toInt()
        if(workspaceSettings.contentCornerRadius.isNotEmpty()) attributes.contentCornerRadius = workspaceSettings.contentCornerRadius.toInt()

        if(workspaceSettings.contentTextColor.isNotEmpty()) attributes.contentTextColor = convertStringToColor(workspaceSettings.contentTextColor)
        if(workspaceSettings.contentAccentColor.isNotEmpty()) attributes.contentAccentColor = convertStringToColor(workspaceSettings.contentAccentColor)

        imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (workspace.main.headerImageUrl.isNotEmpty()) {
                    imageViewHeader.visibility = VISIBLE

                    imageViewHeader.layoutParams.height = calculateViewHeight(
                        this@NewsListActivity,
                        workspace.main.headerImageSize.split(":")[0].toInt(),
                        workspace.main.headerImageSize.split(":")[1].toInt()
                    )
                    imageViewHeader.requestLayout()

                    Picasso.get()
                        .load(workspace.main.headerImageUrl)
                        .into(imageViewHeader)
                } else if(workspace.settings.logoImageUrl.isNotEmpty()) {
                    imageViewHeader.visibility = VISIBLE
                    Picasso.get()
                        .load(workspace.settings.logoImageUrl)
                        .into(imageViewHeader)
                } else imageViewHeader.visibility = GONE

                var imageViewHeaderHeight = imageViewHeader.height //height is ready
                if(imageViewHeader.visibility == GONE) imageViewHeaderHeight = 0

                val tabCustomHeight = tabCustom.height //height is ready

                var spanCount = 2 // 2 columns for phone
                val tabletSize = resources.getBoolean(R.bool.isTablet)
                if (tabletSize) {
                    spanCount = 3 // 3 columns for tablet
                }
                val includeEdge = false

                val spacing = dpToPx(16)

                recyclerView.setPadding(0, imageViewHeaderHeight + tabCustomHeight + spacing + spacing + spacing, 0, 0)
                recyclerView.addItemDecoration(MarginItemDecoration(dpToPx(16)))

                recyclerView.layoutManager = LinearLayoutManager(this@NewsListActivity)

                recyclerView.adapter = NewsAdapter(workspace.news, attributes) { position: Int ->

                }
            }
        })
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun setAttributesDefault() : Attributes {
        val attributes = Attributes()
        attributes.contentBackgroundColor = getColorTemp(R.color.colorAccent)
        attributes.contentBorderColor = getColorTemp(R.color.transparent)
        attributes.contentBorderWidth = 0
        attributes.contentCornerRadius = 10

        attributes.contentTextColor = getColorTemp(R.color.white)
        attributes.contentAccentColor = getColorTemp(R.color.black)
        attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
        return attributes
    }

}
