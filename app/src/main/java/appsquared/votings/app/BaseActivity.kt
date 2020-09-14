package appsquared.votings.app

import android.R.attr.*
import android.app.ActionBar
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.Gravity.LEFT
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base.constraintLayoutRoot
import kotlinx.android.synthetic.main.activity_base.view.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.nav_header_main.*


abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    lateinit var mToolBar: Toolbar
    private var mImageViewHeaderHeight: Int = 0
    lateinit var mTextViewScreenTitle: TextView
    lateinit var mTextViewButtonLeft: TextView
    lateinit var mImageButtonBack: ImageButton
    lateinit var mImageViewHeader: ImageView
    lateinit var mImageViewBackground: ImageView

    var mWorkspace: Model.WorkspaceResponse = Model.WorkspaceResponse()
    var mLoginData: Model.LoginResponse = Model.LoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mWorkspace = AppData().getSavedObjectFromPreference(this,
            PreferenceNames.WORKSPACE,
            Model.WorkspaceResponse::class.java)
            ?: Model.WorkspaceResponse()
        mLoginData = AppData().getSavedObjectFromPreference(this,
            PreferenceNames.LOGIN_DATA,
            Model.LoginResponse::class.java)
            ?: Model.LoginResponse()
    }

    override fun setContentView(layoutResID: Int) {
        val constraintLayout: DrawerLayout = layoutInflater.inflate(R.layout.activity_base,
            null) as DrawerLayout
        val activityContainer: FrameLayout = constraintLayout.findViewById(R.id.layout_container)
        mTextViewScreenTitle = constraintLayout.findViewById(R.id.text_screen_title) as TextView
        mTextViewButtonLeft = constraintLayout.findViewById(R.id.textViewButtonLeft) as TextView
        mImageButtonBack = constraintLayout.findViewById(R.id.imageButtonBack)
        mImageViewHeader = constraintLayout.findViewById(R.id.imageViewHeader)
        mImageViewBackground = constraintLayout.findViewById(R.id.imageViewBackground)
        mToolBar = constraintLayout.findViewById(R.id.toolBar)
        val constraintLayoutRoot = constraintLayout.findViewById(R.id.constraintLayoutRoot) as ConstraintLayout
        val statusBarPlaceHolder = constraintLayout.findViewById(R.id.statusBarPlaceHolder) as View
        mDrawerLayout = constraintLayout.findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        mImageButtonBack.setOnClickListener {
            finish()
        }

        setLightStatusBar(window, true)
        constraintLayoutRoot.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        /*
        val w: Window = window
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        w.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            getStatusBarHeight()
        )
        statusBarPlaceHolder.layoutParams = params
        */

        setImageViewHeader()
        setBackgroundImage()
        
        layoutInflater.inflate(layoutResID, activityContainer, true)

        super.setContentView(constraintLayout)
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun setLoadingIndicatorVisibility(visibility: Int){
        progressBarLoading.visibility = visibility
    }

    private fun setBackgroundImage() {
        if (mWorkspace.settings.backgroundColor.isNotEmpty()) constraintLayoutRoot.setBackgroundColor(
            convertStringToColor(mWorkspace.settings.backgroundColor)
        )
        if (mWorkspace.settings.backgroundImageUrl.isNotEmpty()) {
            mImageViewBackground.visibility = VISIBLE
            Picasso.get()
                .load(mWorkspace.settings.backgroundImageUrl)
                .into(mImageViewBackground)
        } else if (mWorkspace.settings.style.equals("rich", true)) {
            mImageViewBackground.visibility = VISIBLE
            mImageViewBackground.setImageResource(R.drawable.bg)
        }
    }

    private fun setImageViewHeader() {
        mImageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mImageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (mWorkspace.main.headerImageUrl.isNotEmpty()) {
                    mImageViewHeader.visibility = VISIBLE

                    mImageViewHeader.layoutParams.height = calculateViewHeight(
                        this@BaseActivity,
                        mWorkspace.main.headerImageSize.split(":")[0].toInt(),
                        mWorkspace.main.headerImageSize.split(":")[1].toInt()
                    )
                    mImageViewHeader.requestLayout()

                    Picasso.get()
                        .load(mWorkspace.main.headerImageUrl)
                        .into(mImageViewHeader)
                } else if (mWorkspace.settings.logoImageUrl.isNotEmpty()) {
                    mImageViewHeader.visibility = VISIBLE
                    Picasso.get()
                        .load(mWorkspace.settings.logoImageUrl)
                        .into(mImageViewHeader)
                } else mImageViewHeader.visibility = GONE

                mImageViewHeaderHeight = mImageViewHeader.height //height is ready
                if (mImageViewHeader.visibility == GONE) mImageViewHeaderHeight = 0

                childOnlyMethod()
            }
        })
    }

    open fun childOnlyMethod() {
    }

    open fun clickToolbarCancelButton() {}
    open fun clickToolbarMenuButton() {}

    fun removeToolbarShadow() {
        toolBar.elevation = 0F
        toolBar.translationZ = 0F
    }

    fun setScreenTitle(resId: Int) {
        mTextViewScreenTitle.text = getString(resId)
    }

    fun setScreenTitle(title: String) {
        mTextViewScreenTitle.text = title
    }

    fun setCancelButtonActive(b: Boolean) {
        if(b) {
            removeBackButton()
            mTextViewButtonLeft.visibility = VISIBLE
            mTextViewButtonLeft.setOnClickListener {
                clickToolbarCancelButton()
            }
        } else mTextViewButtonLeft.visibility = GONE
    }

    fun removeBackButton() {
        mImageButtonBack.visibility = GONE
    }

    fun showToolbarLogo() {
        image_logo.visibility = VISIBLE
    }

    fun getBackButton(): ImageButton {
        return mImageButtonBack
    }

    fun getImageHeaderHeight() : Int {
        return mImageViewHeaderHeight
    }

    fun removeImageHeader() {
        mImageViewHeader.visibility = GONE
    }

    fun setMenuImageButton(imageId: Int, color: Int) {
        imageButtonMenu.visibility = VISIBLE
        imageButtonMenu.setImageResource(imageId)
        imageButtonMenu.imageTintList = ColorStateList.valueOf(color)
        imageButtonMenu.setOnClickListener {
            clickToolbarMenuButton()
        }
    }

    fun setMenuButton(stringId: Int, color: Int) {
        buttonMenu.visibility = VISIBLE
        buttonMenu.setTextColor(color)
        buttonMenu.setText(getString(stringId))
        buttonMenu.setOnClickListener {
            clickToolbarMenuButton()
        }
    }

    fun removeMenuButton() {
        buttonMenu.visibility = GONE
    }

    fun removeMenuImageButton() {
        imageButtonMenu.visibility = GONE
    }

    fun hideErrorView() {
        linearLayoutBaseErrorView.visibility = GONE
    }

    fun setErrorView(message: String?, listener: () -> Unit) {
        linearLayoutBaseErrorView.visibility = VISIBLE
        buttonBaseRetry.visibility = VISIBLE
        message?.let {
            textViewBaseErrorMessage.text = message
        }

        buttonBaseRetry.setOnClickListener {
            listener()
            linearLayoutBaseErrorView.visibility = GONE
        }
    }

    fun setErrorView(message: String?) {
        linearLayoutBaseErrorView.visibility = VISIBLE
        buttonBaseRetry.visibility = GONE
        message?.let {
            textViewBaseErrorMessage.text = message
        }
    }

    fun openNavigationDrawer() {
        mDrawerLayout.openDrawer(LEFT)
    }

    fun setNavigationDrawerData(workspace: String, email: String, avatarUrl: String) {
        textViewNavDrawerWorkspace.text = workspace
        textViewNavDrawerMail.text = email

        if(avatarUrl.isNotEmpty() && URLUtil.isValidUrl(avatarUrl)) {
            Picasso.get()
                .load(avatarUrl)
                .transform(CircleTransform())
                .into(imageViewNavDrawerProfile)
        }
    }

    fun setNavigationDrawerButton() {
        imageButtonNavDrawer.visibility = VISIBLE
        imageButtonBack.visibility = GONE
        imageButtonNavDrawer.setOnClickListener {
            openNavigationDrawer()
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}