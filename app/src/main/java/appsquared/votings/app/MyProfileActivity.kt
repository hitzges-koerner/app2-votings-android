package appsquared.votings.app

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import appsquared.votings.app.views.MyProfileEditCardView
import com.squareup.picasso.Picasso
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.constraintLayoutRoot
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_user_list.imageViewBackground
import kotlinx.android.synthetic.main.activity_welcome.scrollView
import org.json.JSONObject

class MyProfileActivity : BaseActivity(),
    TextView.OnEditorActionListener, MyProfileEditCardView.OnMyProfileEditButtonClickListener {

    private var mAttributes: Attributes = Attributes()
    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private lateinit var mUserListDownloaded: MutableList<Model.User>
    private lateinit var mUserList: MutableList<Model.User>

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
    }

    override fun onStart() {
        super.onStart()

        val workspace = mWorkspace
        val loginData = mLoginData

        val imageHeaderHeight = getImageHeaderHeight()

        val spacing = dpToPx(16)
        scrollView.setPadding(
            spacing,
            spacing + imageHeaderHeight,
            spacing,
            spacing
        )

        if(workspace.settings.style.isNotEmpty()) {
            when(workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    mAttributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 10

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                    mAttributes.contentAccentContrastColor = getColorTemp(R.color.white)
                    mAttributes.contentPlaceholderColor = getColorTemp(R.color.grey_144)
                }

                "minimal" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 0

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                    mAttributes.contentAccentContrastColor = getColorTemp(R.color.white)
                    mAttributes.contentPlaceholderColor = getColorTemp(R.color.grey_144)
                }

                "clean" -> {
                    mAttributes = setAttributesDefault()
                }
                else -> {
                    mAttributes = setAttributesDefault()
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
        if(workspaceSettings.contentBackgroundColor.isNotEmpty())  mAttributes.contentBackgroundColor = convertStringToColor(workspaceSettings.contentBackgroundColor)
        if(workspaceSettings.contentBorderColor.isNotEmpty()) mAttributes.contentBorderColor = convertStringToColor(workspaceSettings.contentBorderColor)
        if(workspaceSettings.contentBorderWidth.isNotEmpty()) mAttributes.contentBorderWidth = workspaceSettings.contentBorderWidth.toInt()
        if(workspaceSettings.contentCornerRadius.isNotEmpty()) mAttributes.contentCornerRadius = workspaceSettings.contentCornerRadius.toInt()

        if(workspaceSettings.contentTextColor.isNotEmpty()) mAttributes.contentTextColor = convertStringToColor(workspaceSettings.contentTextColor)
        if(workspaceSettings.contentAccentColor.isNotEmpty()) mAttributes.contentAccentColor = convertStringToColor(workspaceSettings.contentAccentColor)
        if(workspaceSettings.contentPlaceholderColor.isNotEmpty()) mAttributes.contentPlaceholderColor = convertStringToColor(workspaceSettings.contentPlaceholderColor)
        if(workspaceSettings.contentAccentContrastColor.isNotEmpty()) mAttributes.contentAccentContrastColor = convertStringToColor(workspaceSettings.contentAccentContrastColor)

        myProfileEditCardViewNameOne.setBackgroundColor(mAttributes.contentBackgroundColor)
        myProfileEditCardViewNameOne.setTextButtonLeft("cancel")
        myProfileEditCardViewNameOne.setButtonsBackgroundColor(mAttributes.contentAccentColor)
        myProfileEditCardViewNameOne.setButtonsTextColor(mAttributes.contentAccentContrastColor)
        myProfileEditCardViewNameOne.setIconTintColor(mAttributes.contentTextColor)
        myProfileEditCardViewNameOne.setTextColor(mAttributes.contentTextColor)
        myProfileEditCardViewNameOne.setPlaceholderColor(mAttributes.contentPlaceholderColor)
        if(loginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(loginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
            R.string.placeholder_name_first))
        myProfileEditCardViewNameOne.setOnMyProfileEditButtonClickListener(this)

        myProfileEditCardViewNameTwo.setBackgroundColor(mAttributes.contentBackgroundColor)
        myProfileEditCardViewNameTwo.setTextButtonLeft("cancel")
        myProfileEditCardViewNameTwo.setButtonsBackgroundColor(mAttributes.contentAccentColor)
        myProfileEditCardViewNameTwo.setButtonsTextColor(mAttributes.contentAccentContrastColor)
        myProfileEditCardViewNameTwo.setIconTintColor(mAttributes.contentTextColor)
        myProfileEditCardViewNameTwo.setTextColor(mAttributes.contentTextColor)
        myProfileEditCardViewNameTwo.setPlaceholderColor(mAttributes.contentPlaceholderColor)
        if(loginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(loginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
            R.string.placeholder_name_last))
        myProfileEditCardViewNameTwo.setOnMyProfileEditButtonClickListener(this)

        myProfileEditCardViewMail.setBackgroundColor(mAttributes.contentBackgroundColor)
        myProfileEditCardViewMail.setTextButtonLeft("cancel")
        myProfileEditCardViewMail.setButtonsBackgroundColor(mAttributes.contentAccentColor)
        myProfileEditCardViewMail.setButtonsTextColor(mAttributes.contentAccentContrastColor)
        myProfileEditCardViewMail.setIconTintColor(mAttributes.contentTextColor)
        myProfileEditCardViewMail.setTextColor(mAttributes.contentTextColor)
        myProfileEditCardViewMail.setPlaceholderColor(mAttributes.contentPlaceholderColor)
        if(loginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(loginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
            R.string.placeholder_email))
        myProfileEditCardViewMail.setOnMyProfileEditButtonClickListener(this)

        myProfileEditCardViewPhoneNo.setBackgroundColor(mAttributes.contentBackgroundColor)
        myProfileEditCardViewPhoneNo.setTextButtonLeft("cancel")
        myProfileEditCardViewPhoneNo.setButtonsBackgroundColor(mAttributes.contentAccentColor)
        myProfileEditCardViewPhoneNo.setButtonsTextColor(mAttributes.contentAccentContrastColor)
        myProfileEditCardViewPhoneNo.setIconTintColor(mAttributes.contentTextColor)
        myProfileEditCardViewPhoneNo.setTextColor(mAttributes.contentTextColor)
        myProfileEditCardViewPhoneNo.setPlaceholderColor(mAttributes.contentPlaceholderColor)
        if(loginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(loginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
            R.string.placeholder_phone_number))
        myProfileEditCardViewPhoneNo.setOnMyProfileEditButtonClickListener(this)

        // profile image
        imageViewProfile.setBackgroundColor(mAttributes.contentBackgroundColor)
        if(loginData.avatarUrl.isNotEmpty() && URLUtil.isValidUrl(loginData.avatarUrl)) {
            Picasso.get()
                .load(loginData.avatarUrl)
                .into(imageViewProfile)
        } else {
            imageViewProfile.setImageResource(R.drawable.icon_placeholder)
            imageViewProfile.setColorFilter(mAttributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
        }

        textViewImageEdit.setBackgroundColor(mAttributes.contentAccentColor)
        textViewImageEdit.setTextColor(mAttributes.contentAccentContrastColor)

        materialCardViewProfile.setOnClickListener {

            ListDialog(this) { tag: String ->
                when(tag) {
                    "camera" -> {
                        startActivityForResult(Intent(this, CameraActivity::class.java).putExtra("type", CameraActivity.CAMERA), 1)
                    }
                    "photo" -> {
                        startActivityForResult(Intent(this, CameraActivity::class.java).putExtra("type", CameraActivity.PICKER), 1)
                    }
                    "delete" -> {
                        deleteAvatar()
                    }
                }
            }
                .generate()
                .addButton("camera", R.string.camera)
                .addButton("photo", R.string.gallery)
                .addButton("delete", R.string.delete_avatar)
                .addCancelButton()
                .show()

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

        attributes.contentAccentContrastColor = getColorTemp(R.color.colorAccent)
        attributes.contentPlaceholderColor = getColorTemp(R.color.grey_190)
        return attributes
    }

    private fun sendUserData(loginData: Model.LoginResponse) {

        val firstName = if(myProfileEditCardViewNameOne.getText().isNotEmpty()) myProfileEditCardViewNameOne.getText() else loginData.firstName
        val lastName = if(myProfileEditCardViewNameTwo.getText().isNotEmpty()) myProfileEditCardViewNameOne.getText() else loginData.firstName
        val email = if(myProfileEditCardViewMail.getText().isNotEmpty()) myProfileEditCardViewNameOne.getText() else loginData.email
        val phoneNo = if(myProfileEditCardViewPhoneNo.getText().isNotEmpty()) myProfileEditCardViewNameOne.getText() else loginData.phoneNo

        myProfileEditCardViewNameOne.getText()
        myProfileEditCardViewNameTwo.getText()
        myProfileEditCardViewPhoneNo.getText()
        myProfileEditCardViewMail.getText()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspaceName = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        val jsonData = JSONObject()
        jsonData.put("workspace", workspaceName)
        jsonData.put("userId", loginData.userId)
        jsonData.put("firstname", firstName)
        jsonData.put("lastname", lastName)
        jsonData.put("email", email)
        jsonData.put("avatarUrl", "")
        jsonData.put("phoneNo", phoneNo)

        disposable = apiService.sendUserData( "Bearer $token", workspaceName!!, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Toast.makeText(this, "Super dupa", Toast.LENGTH_LONG).show()
                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        } else if(error.code() == 409) {
                            Toast.makeText(this, "Email schon vergeben", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
    }

    private fun deleteAvatar() {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspaceName = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.deleteAvatar( "Bearer $token", workspaceName!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Toast.makeText(this, "Super dupa", Toast.LENGTH_LONG).show()
                    imageViewProfile.setImageResource(R.drawable.icon_placeholder)
                    imageViewProfile.setColorFilter(mAttributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        } else if(error.code() == 409) {
                            Toast.makeText(this, "Email schon vergeben", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
    }

    private fun addSection(userList: MutableList<Model.User>): MutableList<Model.User> {
        val userListTemp = mutableListOf<Model.User>()
        for((index, user) in userList.withIndex()) {
            if (index == 0) {
                userListTemp.add(Model.User(user.lastName[0].toString().toUpperCase()))
            } else {
                if(!user.lastName[0].toString().equals(userList[index-1].lastName[0].toString(), true)) {
                    userListTemp.add(Model.User(user.lastName[0].toString().toUpperCase()))
                }
            }
            userListTemp.add(user)
        }
        return userListTemp
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_DONE) {
            this.hideKeyboard()
            return true
        }
        return false
    }

    override fun onClick(myProfileEditCardView: MyProfileEditCardView) {
        focusOnView(myProfileEditCardView)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUrl = data.getStringExtra("imageUrl")
                imageUrl?.let {
                    if(it.isNotEmpty() && URLUtil.isValidUrl(it)) {
                        imageViewProfile.colorFilter = null
                        Picasso.get()
                            .load(it)
                            .into(imageViewProfile)
                    }
                }
            }
        }
    }

    private fun focusOnView(view: View) {
        scrollView.post {
            scrollView.scrollTo(0, view.bottom)
        }
    }
}