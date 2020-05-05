package appsquared.votings.app

import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_base.view.*

abstract class BaseActivity : AppCompatActivity() {

    private var mImageViewHeaderHeight: Int = 0
    lateinit var mTextViewScreenTitle: TextView
    lateinit var mImageButtonBack: ImageButton
    lateinit var mImageViewHeader: ImageView

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
        mImageButtonBack = constraintLayout.findViewById(R.id.image_back_button)
        mImageViewHeader = constraintLayout.findViewById(R.id.imageViewHeader)

        setLightStatusBar(window, true)

        constraintLayout.constraintLayoutRoot.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        setImageViewHeader()

        layoutInflater.inflate(layoutResID, activityContainer, true)

        super.setContentView(constraintLayout)
    }

    private fun setImageViewHeader() {
        mImageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mImageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (mWorkspace.main.headerImageUrl.isNotEmpty()) {
                    mImageViewHeader.visibility = View.VISIBLE

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
                    mImageViewHeader.visibility = View.VISIBLE
                    Picasso.get()
                        .load(mWorkspace.settings.logoImageUrl)
                        .into(mImageViewHeader)
                } else mImageViewHeader.visibility = View.GONE

                mImageViewHeaderHeight = mImageViewHeader.height //height is ready
                if (mImageViewHeader.visibility == View.GONE) mImageViewHeaderHeight = 0
            }
        })
    }

    fun setScreenTitle(resId: Int) {
        mTextViewScreenTitle.text = getString(resId)
    }

    fun setScreenTitle(title: String) {
        mTextViewScreenTitle.text = title
    }

    fun getBackButton(): ImageButton {
        return mImageButtonBack
    }

    fun getImageHeaderHeight() : Int {
        return mImageViewHeaderHeight
    }
}