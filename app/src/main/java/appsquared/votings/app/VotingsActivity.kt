package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_votings_list.recyclerView
import kotlin.math.roundToInt

class VotingsActivity : BaseActivity() {

    private val mVotings = mutableListOf<Model.Voting>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votings_list)
    }

    override fun childOnlyMethod() {
        val workspace: Model.WorkspaceResponse = mWorkspace

        var attributes = Attributes()

        if (workspace.settings.style.isNotEmpty()) {
            when (workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)
                }

                "minimal" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
                }

                "clean" -> {
                    attributes = setAttributesDefault()
                }
                else -> {
                    attributes = setAttributesDefault()
                }
            }
        }

        val workspaceSettings = workspace.settings
        if (workspaceSettings.contentBackgroundColor.isNotEmpty()) attributes.contentBackgroundColor =
            convertStringToColor(workspaceSettings.contentBackgroundColor)
        if (workspaceSettings.contentBorderColor.isNotEmpty()) attributes.contentBorderColor =
            convertStringToColor(workspaceSettings.contentBorderColor)
        if (workspaceSettings.contentBorderWidth.isNotEmpty()) attributes.contentBorderWidth =
            workspaceSettings.contentBorderWidth.toDouble().roundToInt()
        if (workspaceSettings.contentCornerRadius.isNotEmpty()) attributes.contentCornerRadius =
            workspaceSettings.contentCornerRadius.toDouble().roundToInt()

        if (workspaceSettings.contentTextColor.isNotEmpty()) attributes.contentTextColor =
            convertStringToColor(workspaceSettings.contentTextColor)
        if (workspaceSettings.contentAccentColor.isNotEmpty()) attributes.contentAccentColor =
            convertStringToColor(workspaceSettings.contentAccentColor)

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)

        recyclerView.setPadding(0, getImageHeaderHeight(), 0, 0)
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        recyclerView.layoutManager = GridLayoutManager(this@VotingsActivity, spanCount)

        recyclerView.adapter = VotingsListAdapter(mVotings, attributes) { position: Int ->
            startActivity(Intent(this@VotingsActivity, NewsActivity::class.java).putExtra("news_item", workspace.news[position]))
        }
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
