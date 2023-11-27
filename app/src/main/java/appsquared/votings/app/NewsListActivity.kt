package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.votings.android.R
import app.votings.android.databinding.ActivityNewsListBinding
import appsquared.votings.app.views.CustomOnClickListener
import appsquared.votings.app.views.TabCustomView
import appsquared.votings.app.rest.Model
import appsquared.votings.app.tag.enums.Style
import kotlin.math.roundToInt


class NewsListActivity : BaseActivity() {

    var mAttributes = Attributes()

    private lateinit var binding: ActivityNewsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_news))

        val workspace: Model.WorkspaceResponse = mWorkspace


        when (workspace.settings.style) {
            Style.RICH -> {
                mAttributes.contentBackgroundColor =
                    getColorTemp(R.color.white_transparent_fill)
                mAttributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                mAttributes.contentBorderWidth = 0
                mAttributes.contentCornerRadius = 10

                mAttributes.contentTextColor = getColorTemp(R.color.black)
                mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                mAttributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                //tab bar
                binding.tabCustom.binding.materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                binding.tabCustom.binding.materialCardViewTab.strokeColor =
                    getColorTemp(R.color.colorAccent)
                binding.tabCustom.binding.materialCardViewTab.strokeWidth = dpToPx(1)
                binding.tabCustom.binding.materialCardViewTab.radius = dpToPx(5).toFloat()

                binding.tabCustom.setTabBackgroundColorSelected(
                    getColorTemp(R.color.colorAccent)
                )
                binding.tabCustom.setTabTextColorSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabBackgroundColorUnSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabTextColorUnSelected(
                    getColorTemp(R.color.colorAccent)
                )
            }

            Style.MINIMAL -> {
                mAttributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                mAttributes.contentBorderColor = getColorTemp(R.color.transparent)
                mAttributes.contentBorderWidth = 0
                mAttributes.contentCornerRadius = 10

                mAttributes.contentTextColor = getColorTemp(R.color.black)
                mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                mAttributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                //tab bar
                binding.tabCustom.binding.materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                binding.tabCustom.binding.materialCardViewTab.strokeColor =
                    getColorTemp(R.color.white)
                binding.tabCustom.binding.materialCardViewTab.strokeWidth = dpToPx(1)
                binding.tabCustom.binding.materialCardViewTab.radius = dpToPx(5).toFloat()

                binding.tabCustom.setTabBackgroundColorSelected(
                    getColorTemp(R.color.colorAccent)
                )
                binding.tabCustom.setTabTextColorSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabBackgroundColorUnSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabTextColorUnSelected(
                    getColorTemp(R.color.colorAccent)
                )
            }

            Style.CLEAN -> {
                mAttributes = setAttributesDefault()

                //tab bar
                binding.tabCustom.binding.materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                binding.tabCustom.binding.materialCardViewTab.strokeColor =
                    getColorTemp(R.color.colorAccent)
                binding.tabCustom.binding.materialCardViewTab.strokeWidth = dpToPx(1)
                binding.tabCustom.binding.materialCardViewTab.radius = dpToPx(5).toFloat()

                binding.tabCustom.setTabBackgroundColorSelected(
                    getColorTemp(R.color.colorAccent)
                )
                binding.tabCustom.setTabTextColorSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabBackgroundColorUnSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabTextColorUnSelected(
                    getColorTemp(R.color.colorAccent)
                )
            }

            else -> {
                mAttributes = setAttributesDefault()

                //tab bar
                binding.tabCustom.binding.materialCardViewTab.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                binding.tabCustom.binding.materialCardViewTab.strokeColor =
                    getColorTemp(R.color.colorAccent)
                binding.tabCustom.binding.materialCardViewTab.strokeWidth = dpToPx(1)
                binding.tabCustom.binding.materialCardViewTab.radius = dpToPx(5).toFloat()

                binding.tabCustom.setTabBackgroundColorSelected(
                    getColorTemp(R.color.colorAccent)
                )
                binding.tabCustom.setTabTextColorSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabBackgroundColorUnSelected(
                    getColorTemp(R.color.white)
                )
                binding.tabCustom.setTabTextColorUnSelected(
                    getColorTemp(R.color.colorAccent)
                )
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


        if (workspaceSettings.tabActiveBackgroundColor.isNotEmpty()) binding.tabCustom.setTabBackgroundColorSelected(
            convertStringToColor(workspaceSettings.tabActiveBackgroundColor)
        )
        if (workspaceSettings.tabActiveForegroundColor.isNotEmpty()) binding.tabCustom.setTabTextColorSelected(
            convertStringToColor(workspaceSettings.tabActiveForegroundColor)
        )

        if (workspaceSettings.tabInactiveBackgroundColor.isNotEmpty()) binding.tabCustom.setTabBackgroundColorUnSelected(
            convertStringToColor(workspaceSettings.tabInactiveBackgroundColor)
        )
        if (workspaceSettings.tabInactiveForegroundColor.isNotEmpty()) binding.tabCustom.setTabTextColorUnSelected(
            convertStringToColor(workspaceSettings.tabInactiveForegroundColor)
        )


        /*
        DIDNT WORK ANYMORE FROM ONE SECOND TO ANOTHER
        tabCustom.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tabCustom.viewTreeObserver.removeOnGlobalLayoutListener(this)


            }
        })
         */

        binding.tabCustom.run {
            val tabCustomHeight = height //height is ready
            setPadding(0, getImageHeaderHeight(), 0, 0)


            setCustomOnClickListener(object : CustomOnClickListener {
                override fun customOnClick(button: Int) {
                    when (button) {
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
                if (it) selectFragment(NotificationListFragment.newInstance(tabCustomHeight))
                else selectFragment(NewsListFragment.newInstance(tabCustomHeight))
            } ?: run {
                selectFragment(NewsListFragment.newInstance(tabCustomHeight))
            }
        }
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

    fun getColorTemp(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun setAttributesDefault(): Attributes {
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
