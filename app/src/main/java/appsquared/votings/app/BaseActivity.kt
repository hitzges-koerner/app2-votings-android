package appsquared.votings.app

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import app.votings.android.R
import app.votings.android.databinding.ActivityBaseBinding
import appsquared.votings.app.rest.Model
import appsquared.votings.app.tag.enums.Style
import com.squareup.picasso.Picasso


abstract class BaseActivity : AppCompatActivity() {

    private var mImageViewHeaderHeight: Int = 0

    var mWorkspace: Model.WorkspaceResponse = Model.WorkspaceResponse()
    var mLoginData: Model.LoginResponse = Model.LoginResponse()


    private lateinit var binding: ActivityBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageViewHeader()
        setBackgroundImage()

        binding.layoutToolbar.imageButtonBack.setOnClickListener {
            finish()
        }

        mWorkspace = AppData().getSavedObjectFromPreference(
            this,
            PreferenceNames.WORKSPACE,
            Model.WorkspaceResponse::class.java
        )
            ?: Model.WorkspaceResponse()
        mLoginData = AppData().getSavedObjectFromPreference(
            this,
            PreferenceNames.LOGIN_DATA,
            Model.LoginResponse::class.java
        )
            ?: Model.LoginResponse()
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun setLoadingIndicatorVisibility(visibility: Int) {
        binding.progressBarLoading.visibility = visibility
    }

    private fun setBackgroundImage() {
        if (mWorkspace.settings.backgroundColor.isNotEmpty()) binding.constraintLayoutRoot.setBackgroundColor(
            convertStringToColor(mWorkspace.settings.backgroundColor)
        )
        if (mWorkspace.settings.backgroundImageUrl.isNotEmpty()) {
            binding.imageViewBackground.visibility = VISIBLE
            Picasso.get()
                .load(mWorkspace.settings.backgroundImageUrl)
                .into(binding.imageViewBackground)
        } else if (mWorkspace.settings.style == Style.RICH) {
            binding.imageViewBackground.visibility = VISIBLE
            binding.imageViewBackground.setImageResource(R.drawable.bg)
        }
    }

    private fun setImageViewHeader() {

        if (mWorkspace.main.headerImageUrl.isNotEmpty()) {
            binding.imageViewHeader.visibility = VISIBLE

            binding.imageViewHeader.layoutParams.height = calculateViewHeight(
                this@BaseActivity,
                mWorkspace.main.headerImageSize.split(":")[0].toInt(),
                mWorkspace.main.headerImageSize.split(":")[1].toInt()
            )
            binding.imageViewHeader.requestLayout()

            Picasso.get()
                .load(mWorkspace.main.headerImageUrl)
                .into(binding.imageViewHeader)
        } else if (mWorkspace.settings.logoImageUrl.isNotEmpty()) {
            binding.imageViewHeader.visibility = VISIBLE
            Picasso.get()
                .load(mWorkspace.settings.logoImageUrl)
                .into(binding.imageViewHeader)
        } else binding.imageViewHeader.visibility = GONE

        mImageViewHeaderHeight = binding.imageViewHeader.height //height is ready
        if (binding.imageViewHeader.visibility == GONE) mImageViewHeaderHeight = 0

        childOnlyMethod()



        /*
        binding.imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)


            }
        })
         */
    }

    open fun childOnlyMethod() {
    }

    open fun clickToolbarCancelButton() {}
    open fun clickToolbarMenuButton() {}

    fun removeToolbarShadow() {
        binding.toolbar.elevation = 0F
        binding.toolbar.translationZ = 0F
    }

    fun setScreenTitle(resId: Int) {
        binding.layoutToolbar.textScreenTitle.text = getString(resId)
    }

    fun setScreenTitle(title: String) {
        binding.layoutToolbar.textScreenTitle.text = title
    }

    fun setCancelButtonActive(b: Boolean) {
        if (b) {
            removeBackButton()
            binding.layoutToolbar.textViewButtonLeft.visibility = VISIBLE
            binding.layoutToolbar.textViewButtonLeft.setOnClickListener {
                clickToolbarCancelButton()
            }
        } else binding.layoutToolbar.textViewButtonLeft.visibility = GONE
    }

    fun removeBackButton() {
        binding.layoutToolbar.imageButtonBack.visibility = GONE
    }

    fun showToolbarLogo() {
        binding.layoutToolbar.imageLogo.visibility = VISIBLE
    }

    fun getBackButton(): ImageButton {
        return binding.layoutToolbar.imageButtonBack
    }

    fun getImageHeaderHeight(): Int {
        return mImageViewHeaderHeight
    }

    fun removeImageHeader() {
        binding.imageViewHeader.visibility = GONE
    }

    fun setMenuImageButton(imageId: Int, color: Int) {
        binding.layoutToolbar.imageButtonMenu.visibility = VISIBLE
        binding.layoutToolbar.imageButtonMenu.setImageResource(imageId)
        binding.layoutToolbar.imageButtonMenu.imageTintList = ColorStateList.valueOf(color)
        binding.layoutToolbar.imageButtonMenu.setOnClickListener {
            clickToolbarMenuButton()
        }
    }

    fun setMenuButton(stringId: Int, color: Int) {
        binding.layoutToolbar.buttonMenu.visibility = VISIBLE
        binding.layoutToolbar.buttonMenu.setTextColor(color)
        binding.layoutToolbar.buttonMenu.setText(getString(stringId))
        binding.layoutToolbar.buttonMenu.setOnClickListener {
            clickToolbarMenuButton()
        }
    }

    fun removeMenuButton() {
        binding.layoutToolbar.buttonMenu.visibility = GONE
    }

    fun removeMenuImageButton() {
        binding.layoutToolbar.imageButtonMenu.visibility = GONE
    }

    fun hideErrorView() {
        binding.linearLayoutBaseErrorView.visibility = GONE
    }

    fun setErrorView(message: String?, listener: () -> Unit) {
        binding.linearLayoutBaseErrorView.visibility = VISIBLE
        binding.buttonBaseRetry.visibility = VISIBLE
        message?.let {
            binding.textViewBaseErrorMessage.text = message
        }

        binding.buttonBaseRetry.setOnClickListener {
            listener()
            binding.linearLayoutBaseErrorView.visibility = GONE
        }
    }

    fun setErrorView(message: String?) {
        binding.linearLayoutBaseErrorView.visibility = VISIBLE
        binding.buttonBaseRetry.visibility = GONE
        message?.let {
            binding.textViewBaseErrorMessage.text = message
        }
    }
}