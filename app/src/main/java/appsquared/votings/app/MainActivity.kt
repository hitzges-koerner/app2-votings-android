package appsquared.votings.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import app.votings.android.BuildConfig
import app.votings.android.R
import app.votings.android.databinding.ActivityMainBinding
import app.votings.android.databinding.DialogDecisionBinding
import app.votings.android.databinding.DialogInfoBinding
import appsquared.votings.app.adapter.TilesAdapter
import appsquared.votings.app.rest.Model
import appsquared.votings.app.tag.enums.Style
import appsquared.votings.app.views.DecisionDialog
import appsquared.votings.app.views.InfoDialog
import com.google.android.material.navigation.NavigationView


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var statusBarSize = 0

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun clickToolbarMenuButton() {
        super.clickToolbarMenuButton()
        if (mLoginData.isAMSUser == "1") {
            startActivity(Intent(this, VotingCreateActivity::class.java))
        } else {
            InfoDialog(this) {
            }.generate(DialogInfoBinding.inflate(layoutInflater)).setButtonName(R.string.ok)
                .setTitle(R.string.dialog_error_create_voting_title)
                .setMessage(R.string.dialog_error_create_voting_message)
                .show()
        }
    }

    override fun childOnlyMethod() {

        val workspace: Model.WorkspaceResponse = mWorkspace

        checkVersion(workspace.version.android_CurrentVersion, workspace.version.android_RequiredVersion)

        removeBackButton()
        showToolbarLogo()
        setMenuImageButton(R.drawable.ic_baseline_add_circle_24, ContextCompat.getColor(this, R.color.colorAccent))

        /*
        setNavigationDrawerData(mLoginData.workspace, mLoginData.email, mLoginData.avatarUrl)
        setNavigationDrawerButton()
        navigationView.setNavigationItemSelectedListener(this)
         */

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
        when(workspace.planName.toLowerCase()) {
            "free" -> {
                list.add(Item(getString(R.string.tile_my_profil), R.drawable.tile_icons_profil, Item.PROFIL))
                list.add(Item(getString(R.string.tile_users), R.drawable.tile_icons_teilnehmer, Item.ATTENDEES))
                list.add(Item(getString(R.string.tile_current_votings), R.drawable.tile_icons_voting, Item.VOTING_ACTIV))
                list.add(Item(getString(R.string.tile_archive), R.drawable.tile_icons_vergangene_abstimmungen, Item.VOTING_PAST))
                list.add(Item(getString(R.string.tile_notifications), R.drawable.tile_icons_push, Item.NOTIFICATION))
                list.add(Item(getString(R.string.tile_faq), R.drawable.tile_icons_faq, Item.FAQ))
                list.add(Item(getString(R.string.tile_pro_version), R.drawable.tile_icons_pro, Item.PRO))
                list.add(Item(getString(R.string.tile_settings), R.drawable.tile_icons_einstellungen, Item.SETIINGS))
            }
            "trial",
            "pro"-> {
                list.add(Item(getString(R.string.tile_welcome), R.drawable.tile_icons_info, Item.WELCOME))
                list.add(Item(getString(R.string.tile_my_profil), R.drawable.tile_icons_profil, Item.PROFIL))
                list.add(Item(getString(R.string.tile_current_votings), R.drawable.tile_icons_voting, Item.VOTING_ACTIV))
                list.add(Item(getString(R.string.tile_news), R.drawable.tile_icons_news, Item.NEWS))
                list.add(Item(getString(R.string.tile_upcoming_votings), R.drawable.tile_icons_kalender, Item.VOTING_FUTURE))
                list.add(Item(getString(R.string.tile_archive), R.drawable.tile_icons_vergangene_abstimmungen, Item.VOTING_PAST))
                list.add(Item(getString(R.string.tile_users), R.drawable.tile_icons_teilnehmer, Item.ATTENDEES))
                list.add(Item(getString(R.string.tile_settings), R.drawable.tile_icons_einstellungen, Item.SETIINGS))
            }
        }
        var attributes = Attributes()

        when(workspace.settings.style) {
            Style.RICH -> {
                attributes.tilesBackgroundColor = getColorTemp(R.color.white_transparent)
                attributes.tilesBorderColor = getColorTemp(R.color.white_transparent)
                attributes.tilesBorderWidth = 0
                attributes.tilesCornerRadius = 20

                attributes.tilesTextColor = getColorTemp(R.color.black)

                attributes.tilesIconBackgroundColor = getColorTemp(R.color.colorAccent)
                attributes.tilesIconCornerRadius = 50
                attributes.tilesIconTintColor = getColorTemp(R.color.white)
            }

            Style.MINIMAL -> {
                attributes.tilesBackgroundColor = getColorTemp(R.color.transparent)
                attributes.tilesBorderColor = getColorTemp(R.color.transparent)
                attributes.tilesBorderWidth = 0
                attributes.tilesCornerRadius = 0

                attributes.tilesTextColor = getColorTemp(R.color.colorAccent)

                attributes.tilesIconBackgroundColor = getColorTemp(R.color.colorAccent)
                attributes.tilesIconCornerRadius = 50
                attributes.tilesIconTintColor = getColorTemp(R.color.white)
            }

            Style.CLEAN -> {
                attributes = setAttributesDefault()
            }
            else -> {
                attributes = setAttributesDefault()
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

        binding.recyclerView.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

        binding.recyclerView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)

        binding.recyclerView.adapter = TilesAdapter(list, attributes) { position: Int ->
            when(list[position].type) {
                Item.PROFIL -> {
                    startActivityTemp(MyProfileActivity())
                }
                Item.NOTIFICATION -> {
                    startActivityTemp(NotificationListActivity())
                }
                Item.NEWS -> {
                    startActivityTemp(NewsListActivity())
                }
                Item.WELCOME -> {
                    startActivityTemp(WelcomeActivity())
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
                Item.VOTING_PAST -> {
                    startActivity(Intent(this@MainActivity, VotingsListActivity::class.java).putExtra(VotingsListActivity.STATUS, VotingsListActivity.PAST))
                }
                Item.VOTING_FUTURE -> {
                    startActivity(Intent(this@MainActivity, VotingsListActivity::class.java).putExtra(VotingsListActivity.STATUS, VotingsListActivity.FUTURE))
                }
                Item.PRO -> {
                    startActivity(Intent(this@MainActivity, ProVersionActivity::class.java))
                }
                Item.FAQ -> {
                    startActivity(Intent(this@MainActivity, FaqListActivity::class.java))
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

        Log.d(Constant.TAG, "installed version ${getAppVersion()}")
        Log.d(Constant.TAG, "current version $currentVersionInner")
        Log.d(Constant.TAG, "required version $requiredVersionInner")

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
            VersionChecker.OUTDATED -> DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    openPlayStoreUrl(url)
                }
            }.generate(DialogDecisionBinding.inflate(layoutInflater))
                .setTitle(getString(R.string.new_version_available_dialog_title))
                .setButtonRightName(getString(R.string.new_version_available_dialog_download), false)
                .setButtonLeftName(getString(R.string.new_version_available_dialog_cancel))
                .setMessage(String.format(getString(R.string.new_version_available_dialog_messagge), currentVersion))
                .show()
            VersionChecker.DEPRECATED -> {
                DecisionDialog(this) {
                    if (it == DecisionDialog.LEFT) {
                        finish()
                    }
                    if (it == DecisionDialog.RIGHT) {
                        openPlayStoreUrl(url)
                        finish()
                    }
                }.generate(DialogDecisionBinding.inflate(layoutInflater))
                    .setCancelable(false)
                    .setTitle(getString(R.string.new_version_required_dialog_title))
                    .setButtonRightName(getString(R.string.new_version_required_dialog_download), false)
                    .setButtonLeftName(getString(R.string.new_version_required_dialog_close))
                    .setMessage(String.format(getString(R.string.new_version_required_dialog_messagge), currentVersion, BuildConfig.VERSION_NAME))
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.navBarAddWorkspace -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}
