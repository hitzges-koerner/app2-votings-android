package appsquared.votings.app

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base.view.constraintLayoutRoot
import kotlinx.android.synthetic.main.dialog_info.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

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

        mWorkspace = AppData().getSavedObjectFromPreference(this, PreferenceNames.WORKSPACE, Model.WorkspaceResponse::class.java)
            ?: Model.WorkspaceResponse()
        mLoginData = AppData().getSavedObjectFromPreference(this, PreferenceNames.LOGIN_DATA, Model.LoginResponse::class.java)
            ?: Model.LoginResponse()
    }

    override fun setContentView(layoutResID: Int) {
        val constraintLayout: ConstraintLayout = layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        val activityContainer: FrameLayout = constraintLayout.findViewById(R.id.layout_container)
        mTextViewScreenTitle = constraintLayout.findViewById(R.id.text_screen_title) as TextView
        mTextViewButtonLeft = constraintLayout.findViewById(R.id.textViewButtonLeft) as TextView
        mImageButtonBack = constraintLayout.findViewById(R.id.imageButtonBack)
        mImageViewHeader = constraintLayout.findViewById(R.id.imageViewHeader)
        mImageViewBackground = constraintLayout.findViewById(R.id.imageViewBackground)


        mImageButtonBack.setOnClickListener {
            finish()
        }

        setLightStatusBar(window, true)

        constraintLayout.constraintLayoutRoot.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        setImageViewHeader()

        setBackgroundImage()

        layoutInflater.inflate(layoutResID, activityContainer, true)

        super.setContentView(constraintLayout)
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
                } else mImageViewHeader.visibility = View.GONE

                mImageViewHeaderHeight = mImageViewHeader.height //height is ready
                if (mImageViewHeader.visibility == View.GONE) mImageViewHeaderHeight = 0

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
        toolBar.title = getString(resId)
        toolbar.title = getString(resId)
    }

    fun setScreenTitle(title: String) {
        mTextViewScreenTitle.text = title
        toolBar.title = title
        toolbar.title = title
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
}