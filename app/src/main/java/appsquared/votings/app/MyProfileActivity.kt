package appsquared.votings.app

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import appsquared.votings.app.views.ListDialog
import appsquared.votings.app.views.MyProfileEditCardView
import com.squareup.picasso.Picasso
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.button_card_view.view.*
import org.json.JSONObject
import kotlin.math.roundToInt

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

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        buttonCardViewLogout.materialCardView.setOnClickListener {

            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit().remove(PreferenceNames.PASSWORD).apply()
            pref.edit().remove(PreferenceNames.WORKSPACE_NAME).apply()
            pref.edit().remove(PreferenceNames.EMAIL).apply()

            val intent = Intent(this@MyProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    override fun childOnlyMethod() {

        textViewWorkspaceName.text = PreferenceManager.getDefaultSharedPreferences(this).getString(PreferenceNames.WORKSPACE_NAME, "")
        setScreenTitle(R.string.tile_my_profil)

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

        val workspaceSettings = workspace.settings
        if(workspaceSettings.contentBackgroundColor.isNotEmpty())  mAttributes.contentBackgroundColor = convertStringToColor(workspaceSettings.contentBackgroundColor)
        if(workspaceSettings.contentBorderColor.isNotEmpty()) mAttributes.contentBorderColor = convertStringToColor(workspaceSettings.contentBorderColor)
        if(workspaceSettings.contentBorderWidth.isNotEmpty()) mAttributes.contentBorderWidth = workspaceSettings.contentBorderWidth.toDouble().roundToInt()
        if(workspaceSettings.contentCornerRadius.isNotEmpty()) mAttributes.contentCornerRadius = workspaceSettings.contentCornerRadius.toDouble().roundToInt()

        if(workspaceSettings.contentTextColor.isNotEmpty()) mAttributes.contentTextColor = convertStringToColor(workspaceSettings.contentTextColor)
        if(workspaceSettings.contentAccentColor.isNotEmpty()) mAttributes.contentAccentColor = convertStringToColor(workspaceSettings.contentAccentColor)
        if(workspaceSettings.contentPlaceholderColor.isNotEmpty()) mAttributes.contentPlaceholderColor = convertStringToColor(workspaceSettings.contentPlaceholderColor)
        if(workspaceSettings.contentAccentContrastColor.isNotEmpty()) mAttributes.contentAccentContrastColor = convertStringToColor(workspaceSettings.contentAccentContrastColor)

        if(loginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(loginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
            R.string.placeholder_name_first))
        myProfileEditCardViewNameOne.setOnMyProfileEditButtonClickListener(this)

        if(loginData.lastName.isNotEmpty()) myProfileEditCardViewNameTwo.setText(loginData.lastName) else myProfileEditCardViewNameTwo.setPlaceHolderText(getString(
            R.string.placeholder_name_last))
        myProfileEditCardViewNameTwo.setOnMyProfileEditButtonClickListener(this)

        if(loginData.email.isNotEmpty()) myProfileEditCardViewMail.setText(loginData.email) else myProfileEditCardViewMail.setPlaceHolderText(getString(
            R.string.placeholder_email))
        myProfileEditCardViewMail.setOnMyProfileEditButtonClickListener(this)
        myProfileEditCardViewMail.disabledEdit()

        if(loginData.phoneNo.isNotEmpty()) myProfileEditCardViewPhoneNo.setText(loginData.phoneNo) else myProfileEditCardViewPhoneNo.setPlaceHolderText(getString(
            R.string.placeholder_phone_number))
        myProfileEditCardViewPhoneNo.setOnMyProfileEditButtonClickListener(this)

        // profile image
        //imageViewProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_144))
        imageViewProfile.setBackgroundResource(R.drawable.background_round_grey)
        if(loginData.avatarUrl.isNotEmpty() && URLUtil.isValidUrl(loginData.avatarUrl)) {
            Picasso.get()
                .load(loginData.avatarUrl)
                .transform(CircleTransform())
                .into(imageViewProfile)
        } else {
            imageViewProfile.setImageResource(R.drawable.icon_placeholder)
            imageViewProfile.setColorFilter(mAttributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
        }

        textViewImageEdit.setBackgroundColor(mAttributes.contentAccentColor)
        textViewImageEdit.setTextColor(mAttributes.contentAccentContrastColor)

        imageViewProfile.setOnClickListener {

            ListDialog(this) { tag: String ->
                when (tag) {
                    "camera" -> {
                        startActivityForResult(
                            Intent(
                                this,
                                CameraActivity::class.java
                            ).putExtra("type", CameraActivity.CAMERA), 1
                        )
                    }
                    "photo" -> {
                        startActivityForResult(
                            Intent(
                                this,
                                CameraActivity::class.java
                            ).putExtra("type", CameraActivity.PICKER), 1
                        )
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

    override fun onStart() {
        super.onStart()
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

    private fun sendUserData() {

        //val firstName = if(myProfileEditCardViewNameOne.getText().isNotEmpty()) myProfileEditCardViewNameOne.getText() else mLoginData.firstName
        //val lastName = if(myProfileEditCardViewNameTwo.getText().isNotEmpty()) myProfileEditCardViewNameTwo.getText() else mLoginData.lastName
        //val email = if(myProfileEditCardViewMail.getText().isNotEmpty()) myProfileEditCardViewMail.getText() else mLoginData.email
        //val phoneNo = if(myProfileEditCardViewPhoneNo.getText().isNotEmpty()) myProfileEditCardViewPhoneNo.getText() else mLoginData.phoneNo

        val firstName = myProfileEditCardViewNameOne.getText()
        val lastName = myProfileEditCardViewNameTwo.getText()
        val email = myProfileEditCardViewMail.getText()
        val phoneNo = myProfileEditCardViewPhoneNo.getText()

        if(firstName.isEmpty()) {
            toast("Feld mit Vorname darf nicht leer sein.")
            return
        }
        if(lastName.isEmpty()) {
            toast("Feld mit Nachname darf nicht leer sein.")
            return
        }
        if(email.isEmpty()) {
            toast("Feld mit Email darf nicht leer sein.")
            return
        }
        if(!isEmailValid(email)) {
            toast("Die Email muss ein gültiges Format haben.")
            return
        }

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspaceName = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        val jsonData = JSONObject()
        jsonData.put("workspace", workspaceName)
        jsonData.put("userId", mLoginData.userId)
        jsonData.put("firstname", firstName)
        jsonData.put("lastname", lastName)
        jsonData.put("email", email)
        jsonData.put("avatarUrl", mLoginData.avatarUrl)
        jsonData.put("phoneNo", phoneNo)

        disposable = apiService.sendUserData( "Bearer $token", workspaceName!!, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    myProfileEditCardViewNameOne.editSave()
                    myProfileEditCardViewNameTwo.editSave()
                    myProfileEditCardViewPhoneNo.editSave()

                    mLoginData.firstName = firstName
                    mLoginData.lastName = lastName
                    mLoginData.email = email
                    mLoginData.phoneNo = phoneNo

                    AppData().saveObjectToSharedPreference(this, PreferenceNames.LOGIN_DATA, result)

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
                    imageViewProfile.setImageResource(R.drawable.icon_placeholder)
                    imageViewProfile.setColorFilter(mAttributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)

                    mLoginData.avatarUrl = ""
                    AppData().saveObjectToSharedPreference(this, PreferenceNames.LOGIN_DATA, mLoginData)
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

    override fun scrollViewToPosition(myProfileEditCardView: MyProfileEditCardView) {
        focusOnView(myProfileEditCardView)
    }

    override fun uploadData() {
        sendUserData()
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

                        mLoginData.avatarUrl = it
                        AppData().saveObjectToSharedPreference(this, PreferenceNames.LOGIN_DATA, mLoginData)

                        imageViewProfile.colorFilter = null
                        Picasso.get()
                            .load(it)
                            .transform(CircleTransform())
                            .into(imageViewProfile)
                    }
                }
            }
        }
    }

    private fun focusOnView(view: View) {
        scrollView.post {
            scrollView.scrollTo(0, view.top)
        }
    }
}