package appsquared.votings.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import appsquared.votings.app.views.DecisionDialog
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun childOnlyMethod() {

        val workspace: Model.WorkspaceResponse = mWorkspace

        checkVersion(workspace.version.android_CurrentVersion, workspace.version.android_RequiredVersion)

        removeBackButton()
        showToolbarLogo()

        /*
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
         */

        //toolBar.logo = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)

        val list = mutableListOf<Item>()
        list.add(Item("Willkommen", R.drawable.tile_icons_info, Item.INFO))
        list.add(Item("Mein Profil", R.drawable.tile_icons_profil, Item.PROFIL))
        list.add(Item("Aktuelle Abstimmungen", R.drawable.tile_icons_voting, Item.VOTING_ACTIV))
        list.add(Item("Neuigkeiten", R.drawable.tile_icons_news, Item.NEWS))
        list.add(Item("Kommende Abstimmungen", R.drawable.tile_icons_kalender, Item.VOTING_FUTURE))
        list.add(Item("Archiv", R.drawable.tile_icons_vergangene_abstimmungen, Item.VOTING_PAST))
        list.add(Item("Teilnehmer", R.drawable.tile_icons_teilnehmer, Item.ATTENDEES))
        list.add(Item("Einstellungen", R.drawable.ico_einstellungen, Item.SETIINGS))
        var attributes = Attributes()

        if(workspace.settings.style.isNotEmpty()) {
            when(workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    attributes.tilesBackgroundColor = getColorTemp(R.color.white_transparent)
                    attributes.tilesBorderColor = getColorTemp(R.color.white_transparent)
                    attributes.tilesBorderWidth = 0
                    attributes.tilesCornerRadius = 20

                    attributes.tilesTextColor = getColorTemp(R.color.black)

                    attributes.tilesIconBackgroundColor = getColorTemp(R.color.colorAccent)
                    attributes.tilesIconCornerRadius = 50
                    attributes.tilesIconTintColor = getColorTemp(R.color.white)
                }

                "minimal" -> {
                    attributes.tilesBackgroundColor = getColorTemp(R.color.transparent)
                    attributes.tilesBorderColor = getColorTemp(R.color.transparent)
                    attributes.tilesBorderWidth = 0
                    attributes.tilesCornerRadius = 0

                    attributes.tilesTextColor = getColorTemp(R.color.colorAccent)

                    attributes.tilesIconBackgroundColor = getColorTemp(R.color.colorAccent)
                    attributes.tilesIconCornerRadius = 50
                    attributes.tilesIconTintColor = getColorTemp(R.color.white)
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
        if(workspaceSettings.tilesBackgroundColor.isNotEmpty())  attributes.tilesBackgroundColor = convertStringToColor(workspaceSettings.tilesBackgroundColor)
        if(workspaceSettings.tilesBorderColor.isNotEmpty()) attributes.tilesBorderColor = convertStringToColor(workspaceSettings.tilesBorderColor)
        if(workspaceSettings.tilesBorderWidth.isNotEmpty()) attributes.tilesBorderWidth = workspaceSettings.tilesBorderWidth.toInt()
        if(workspaceSettings.tilesCornerRadius.isNotEmpty()) attributes.tilesCornerRadius = workspaceSettings.tilesCornerRadius.toInt()

        if(workspaceSettings.tilesTextColor.isNotEmpty()) attributes.tilesTextColor = convertStringToColor(workspaceSettings.tilesTextColor)

        if(workspaceSettings.tilesIconBackgroundColor.isNotEmpty()) attributes.tilesIconBackgroundColor = convertStringToColor(workspaceSettings.tilesIconBackgroundColor)
        if(workspaceSettings.tilesIconCornerRadius.isNotEmpty()) attributes.tilesIconCornerRadius = convertStringToColor(workspaceSettings.tilesIconCornerRadius)
        if(workspaceSettings.tilesIconTintColor.isNotEmpty()) attributes.tilesIconTintColor = convertStringToColor(workspaceSettings.tilesIconTintColor)

        /*
        imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (workspace.main.headerImageUrl.isNotEmpty()) {
                    imageViewHeader.visibility = VISIBLE

                    imageViewHeader.layoutParams.height = calculateViewHeight(
                        this@MainActivity,
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


            }
        })

         */


        var spanCount = 2 // 2 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 3 // 3 columns for tablet
        }
        val includeEdge = false

        var tilesSpacing = 16
        if(workspace.settings.tilesSpacing.isNotEmpty()) tilesSpacing = workspace.settings.tilesSpacing.toInt()

        val spacing = dpToPx(tilesSpacing)

        recyclerView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)

        recyclerView.adapter = TilesAdapter(list, attributes) { position: Int ->

            when(position) {
                Item.INFO -> {
                    startActivityTemp(WelcomeActivity())
                }
                Item.PROFIL -> {
                    startActivityTemp(MyProfileActivity())
                }
                Item.NEWS -> {
                    startActivityTemp(NewsListActivity())
                }
                Item.ATTENDEES -> {
                    startActivityTemp(UserListActivity())
                }
                Item.SETIINGS -> {
                    startActivityTemp(SettingsActivity())
                }
                Item.VOTING_ACTIV -> {
                    startActivity(Intent(this@MainActivity, VotingsListActivity::class.java).putExtra(VotingsListActivity.STATUS, VotingsListActivity.CURRENT))
                }
                Item.VOTING_FUTURE -> {
                    startActivity(Intent(this@MainActivity, VotingsListActivity::class.java).putExtra(VotingsListActivity.STATUS, VotingsListActivity.FUTURE))
                }
                Item.VOTING_PAST -> {
                    startActivity(Intent(this@MainActivity, VotingsListActivity::class.java).putExtra(VotingsListActivity.STATUS, VotingsListActivity.PAST))
                }
                else -> {
                    startActivityTemp(LoginActivity())
                    finish()
                }
            }
        }
    }

    private fun startActivityTemp(activity: Activity) {
        startActivity(Intent(this@MainActivity, activity.javaClass))
    }

    fun startActivity() {

    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun setAttributesDefault() : Attributes {
        val attributes = Attributes()
        attributes.tilesBackgroundColor = getColorTemp(R.color.colorAccent)
        attributes.tilesBorderColor = getColorTemp(R.color.transparent)
        attributes.tilesBorderWidth = 0
        attributes.tilesCornerRadius = 20

        attributes.tilesTextColor = getColorTemp(R.color.white)

        attributes.tilesIconBackgroundColor = getColorTemp(R.color.white)
        attributes.tilesIconCornerRadius = 50
        attributes.tilesIconTintColor = getColorTemp(R.color.colorAccent)
        return attributes
    }

    private fun checkVersion(currentVersion : String, requiredVersion: String) : Boolean {
        var currentVersionInner = currentVersion
        var requiredVersionInner = requiredVersion

        if(currentVersionInner.isEmpty()) currentVersionInner = "0.0.0"
        if(requiredVersionInner.isEmpty())  requiredVersionInner = "0.0.0"

        val versionConverter = VersionChecker(currentVersionInner, requiredVersionInner)
        versionConverter.init()
        if(versionConverter.isDeprecated()) {
            showVersionDialog(VersionChecker.DEPRECATED, currentVersion)
            return false
        } else if(versionConverter.isOutdated()) {
            showVersionDialog(VersionChecker.OUTDATED, currentVersion)
            return false
        }
        return true
    }

    private fun showVersionDialog(type: Int, currentVersion: String) {

        val url = mWorkspace.version.android_AppStoreLink

        when(type) {
            VersionChecker.OUTDATED -> {
                DecisionDialog(this) {
                    if (it == DecisionDialog.LEFT) return@DecisionDialog
                    if (it == DecisionDialog.RIGHT) {
                        openPlayStoreUrl(url)
                    }
                }.generate()
                    .setTitle("Neue Version verfügbar")
                    .setButtonRightName("Herunterladen")
                    .setButtonLeftName("Nein, danke")
                    .setMessage("Im Play Store steht eine neue Version ($currentVersion) zur Verfügung.\n\nBitte laden Sie die neueste Version aus dem Play Store herunter.")
                    .show()
            }
            VersionChecker.DEPRECATED -> {
                DecisionDialog(this) {
                    if (it == DecisionDialog.LEFT) {
                        finish()
                    }
                    if (it == DecisionDialog.RIGHT) {
                        openPlayStoreUrl(url)
                        finish()
                    }
                }.generate()
                    .setCancelable(false)
                    .setTitle("Neue Version erforderlich")
                    .setButtonRightName("Herunterladen")
                    .setButtonLeftName("Beenden")
                    .setMessage("Im Play Store steht eine neue Version ($currentVersion) zur Verfügung. Die installierte Version (${getAppVersion()}) wird nicht mehr unterstützt.\n\nBitte laden Sie die neueste Version aus dem Play Store herunter.")
                    .show()
            }
        }
    }

    private fun openPlayStoreUrl(url: String) {
        val webPage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        }
    }
}
