package appsquared.votings.app

import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import appsquared.votings.app.views.CustomOnClickListener
import appsquared.votings.app.views.TabCustomView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_news_list.*
import kotlinx.android.synthetic.main.tab_custom.*
import kotlin.math.roundToInt


class NewsListActivity : BaseActivity() {

    var mAttributes = Attributes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

    }

    override fun childOnlyMethod() {
        val workspace: Model.WorkspaceResponse = mWorkspace

        if (workspace.settings.style.isNotEmpty()) {
            when (workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    mAttributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 10

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColorSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                    tabCustom.setTabTextColorSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabBackgroundColorUnSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabTextColorUnSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                }

                "minimal" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 10

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.white)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColorSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                    tabCustom.setTabTextColorSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabBackgroundColorUnSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabTextColorUnSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                }

                "clean" -> {
                    mAttributes = setAttributesDefault()

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColorSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                    tabCustom.setTabTextColorSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabBackgroundColorUnSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabTextColorUnSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                }
                else -> {
                    mAttributes = setAttributesDefault()

                    //tab bar
                    materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewTab.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewTab.strokeWidth = dpToPx(1)
                    materialCardViewTab.radius = dpToPx(5).toFloat()

                    tabCustom.setTabBackgroundColorSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                    tabCustom.setTabTextColorSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabBackgroundColorUnSelected(
                        getColorTemp(R.color.white)
                    )
                    tabCustom.setTabTextColorUnSelected(
                        getColorTemp(R.color.colorAccent)
                    )
                }
            }
        }

        val workspaceSettings = workspace.settings
        if (workspaceSettings.contentBackgroundColor.isNotEmpty()) mAttributes.contentBackgroundColor =
            convertStringToColor(workspaceSettings.contentBackgroundColor)
        if (workspaceSettings.contentBorderColor.isNotEmpty()) mAttributes.contentBorderColor =
            convertStringToColor(workspaceSettings.contentBorderColor)
        if (workspaceSettings.contentBorderWidth.isNotEmpty()) mAttributes.contentBorderWidth =
            workspaceSettings.contentBorderWidth.toDouble().roundToInt()
        if (workspaceSettings.contentCornerRadius.isNotEmpty()) mAttributes.contentCornerRadius =
            workspaceSettings.contentCornerRadius.toDouble().roundToInt()

        if (workspaceSettings.contentTextColor.isNotEmpty()) mAttributes.contentTextColor =
            convertStringToColor(workspaceSettings.contentTextColor)
        if (workspaceSettings.contentAccentColor.isNotEmpty()) mAttributes.contentAccentColor =
            convertStringToColor(workspaceSettings.contentAccentColor)


        if (workspaceSettings.tabActiveBackgroundColor.isNotEmpty()) tabCustom.setTabBackgroundColorSelected(convertStringToColor(workspaceSettings.tabActiveBackgroundColor))
        if (workspaceSettings.tabActiveForegroundColor.isNotEmpty()) tabCustom.setTabTextColorSelected(convertStringToColor(workspaceSettings.tabActiveForegroundColor))

        if (workspaceSettings.tabInactiveBackgroundColor.isNotEmpty()) tabCustom.setTabBackgroundColorUnSelected(convertStringToColor(workspaceSettings.tabInactiveBackgroundColor))
        if (workspaceSettings.tabInactiveForegroundColor.isNotEmpty()) tabCustom.setTabTextColorUnSelected(convertStringToColor(workspaceSettings.tabInactiveForegroundColor))


        tabCustom.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tabCustom.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val tabCustomHeight = tabCustom.height //height is ready
                tabCustom.setPadding(0, getImageHeaderHeight(), 0, 0)


                tabCustom.setCustomOnClickListener(object : CustomOnClickListener {
                    override fun customOnClick(button: Int) {
                        when(button) {
                            TabCustomView.LEFT -> {
                                removeAllFragments(supportFragmentManager)
                                selectFragment(NewsListFragment.newInstance(tabCustomHeight))
                            }
                            TabCustomView.RIGHT -> {
                                removeAllFragments(supportFragmentManager)
                                selectFragment(NotificationListFragment.newInstance(tabCustomHeight))
                            }
                        }
                    }
                })

                val showNotifications = intent.extras?.getBoolean("show_notifications")
                showNotifications?.let {
                    if(it) selectFragment(NotificationListFragment.newInstance(tabCustomHeight))
                    else selectFragment(NewsListFragment.newInstance(tabCustomHeight))
                } ?: run {
                    selectFragment(NewsListFragment.newInstance(tabCustomHeight))
                }
            }
        })
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    private fun removeAllFragments(fragmentManager: FragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate()
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
        attributes.contentAccentColor = getColorTemp(R.color.white)
        attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
        return attributes
    }

}
