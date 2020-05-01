package appsquared.votings.app

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceManager
import appsquared.votings.app.views.MyProfilEditCardView
import com.squareup.picasso.Picasso
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.constraintLayoutRoot
import kotlinx.android.synthetic.main.activity_main.imageViewHeader
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.toolbarCustom
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_user_list.imageViewBackground
import kotlinx.android.synthetic.main.activity_welcome.scrollView
import kotlinx.android.synthetic.main.toolbar_custom.*
import org.json.JSONObject


class MyProfileActivity : AppCompatActivity(), EditTextWithClear.OnEditTextWithClearClickListener,
    TextView.OnEditorActionListener, MyProfilEditCardView.OnMyProfileEditBttonClickListener {

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

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

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

        val mWorkspace: Model.WorkspaceResponse? = AppData().getSavedObjectFromPreference(this, appsquared.votings.app.PreferenceNames.Companion.WORKSPACE, Model.WorkspaceResponse::class.java)
        val mLoginData: Model.LoginResponse? = AppData().getSavedObjectFromPreference(this, appsquared.votings.app.PreferenceNames.Companion.LOGIN_DATA, Model.LoginResponse::class.java)

        if (mWorkspace != null && mLoginData != null) {
            imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (mWorkspace.main.headerImageUrl.isNotEmpty()) {
                        imageViewHeader.visibility = VISIBLE

                        imageViewHeader.layoutParams.height = calculateViewHeight(
                            this@MyProfileActivity,
                            mWorkspace.main.headerImageSize.split(":")[0].toInt(),
                            mWorkspace.main.headerImageSize.split(":")[1].toInt()
                        )
                        imageViewHeader.requestLayout()

                        Picasso.get()
                            .load(mWorkspace.main.headerImageUrl)
                            .into(imageViewHeader)
                    } else if (mWorkspace.settings.logoImageUrl.isNotEmpty()) {
                        imageViewHeader.visibility = VISIBLE
                        Picasso.get()
                            .load(mWorkspace.settings.logoImageUrl)
                            .into(imageViewHeader)
                    } else imageViewHeader.visibility = GONE

                    var imageViewHeaderHeight = imageViewHeader.height //height is ready

                    if (imageViewHeader.visibility == GONE) imageViewHeaderHeight = 0

                    val spacing = dpToPx(16)
                    scrollView.setPadding(
                        spacing,
                        spacing + imageViewHeaderHeight,
                        spacing,
                        spacing
                    )
                }
            })
            var attributes = Attributes()

            if(mWorkspace.settings.style.isNotEmpty()) {
                when(mWorkspace.settings.style.toLowerCase()) {
                    "rich" -> {
                        attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                        attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                        attributes.contentBorderWidth = 0
                        attributes.contentCornerRadius = 10

                        attributes.contentTextColor = getColorTemp(R.color.black)
                        attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                        attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                        attributes.contentAccentContrastColor = getColorTemp(R.color.white)
                        attributes.contentPlaceholderColor = getColorTemp(R.color.grey_144)
                    }

                    "minimal" -> {
                        attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                        attributes.contentBorderColor = getColorTemp(R.color.transparent)
                        attributes.contentBorderWidth = 0
                        attributes.contentCornerRadius = 0

                        attributes.contentTextColor = getColorTemp(R.color.black)
                        attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                        attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                        attributes.contentAccentContrastColor = getColorTemp(R.color.white)
                        attributes.contentPlaceholderColor = getColorTemp(R.color.grey_144)
                    }

                    "clean" -> {
                        attributes = setAttributesDefault()
                    }
                    else -> {
                        attributes = setAttributesDefault()
                    }
                }
            }

            if(mWorkspace.settings.backgroundColor.isNotEmpty()) constraintLayoutRoot.setBackgroundColor(convertStringToColor(mWorkspace.settings.backgroundColor))
            if(mWorkspace.settings.backgroundImageUrl.isNotEmpty()) {
                imageViewBackground.visibility = VISIBLE
                Picasso.get()
                    .load(mWorkspace.settings.backgroundImageUrl)
                    .into(imageViewBackground)
            } else if(mWorkspace.settings.style.equals("rich", true)) {
                imageViewBackground.visibility = VISIBLE
                imageViewBackground.setImageResource(R.drawable.image)
            }

            val workspaceSettings = mWorkspace.settings
            if(workspaceSettings.contentBackgroundColor.isNotEmpty())  attributes.contentBackgroundColor = convertStringToColor(workspaceSettings.contentBackgroundColor)
            if(workspaceSettings.contentBorderColor.isNotEmpty()) attributes.contentBorderColor = convertStringToColor(workspaceSettings.contentBorderColor)
            if(workspaceSettings.contentBorderWidth.isNotEmpty()) attributes.contentBorderWidth = workspaceSettings.contentBorderWidth.toInt()
            if(workspaceSettings.contentCornerRadius.isNotEmpty()) attributes.contentCornerRadius = workspaceSettings.contentCornerRadius.toInt()

            if(workspaceSettings.contentTextColor.isNotEmpty()) attributes.contentTextColor = convertStringToColor(workspaceSettings.contentTextColor)
            if(workspaceSettings.contentAccentColor.isNotEmpty()) attributes.contentAccentColor = convertStringToColor(workspaceSettings.contentAccentColor)
            if(workspaceSettings.contentPlaceholderColor.isNotEmpty()) attributes.contentPlaceholderColor = convertStringToColor(workspaceSettings.contentPlaceholderColor)
            if(workspaceSettings.contentAccentContrastColor.isNotEmpty()) attributes.contentAccentContrastColor = convertStringToColor(workspaceSettings.contentAccentContrastColor)

            myProfileEditCardViewNameOne.setBackgroundColor(attributes.contentBackgroundColor)
            myProfileEditCardViewNameOne.setTextButtonLeft("cancel")
            myProfileEditCardViewNameOne.setButtonsBackgroundColor(attributes.contentAccentColor)
            myProfileEditCardViewNameOne.setButtonsTextColor(attributes.contentAccentContrastColor)
            myProfileEditCardViewNameOne.setIconTintColor(attributes.contentTextColor)
            myProfileEditCardViewNameOne.setTextColor(attributes.contentTextColor)
            myProfileEditCardViewNameOne.setPlaceholderColor(attributes.contentPlaceholderColor)
            if(mLoginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(mLoginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
                            R.string.placeholder_name_first))
            myProfileEditCardViewNameOne.setOnMyProfileEditButtonClickListener(this)

            myProfileEditCardViewNameTwo.setBackgroundColor(attributes.contentBackgroundColor)
            myProfileEditCardViewNameTwo.setTextButtonLeft("cancel")
            myProfileEditCardViewNameTwo.setButtonsBackgroundColor(attributes.contentAccentColor)
            myProfileEditCardViewNameTwo.setButtonsTextColor(attributes.contentAccentContrastColor)
            myProfileEditCardViewNameTwo.setIconTintColor(attributes.contentTextColor)
            myProfileEditCardViewNameTwo.setTextColor(attributes.contentTextColor)
            myProfileEditCardViewNameTwo.setPlaceholderColor(attributes.contentPlaceholderColor)
            if(mLoginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(mLoginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
                            R.string.placeholder_name_last))
            myProfileEditCardViewNameTwo.setOnMyProfileEditButtonClickListener(this)

            myProfileEditCardViewMail.setBackgroundColor(attributes.contentBackgroundColor)
            myProfileEditCardViewMail.setTextButtonLeft("cancel")
            myProfileEditCardViewMail.setButtonsBackgroundColor(attributes.contentAccentColor)
            myProfileEditCardViewMail.setButtonsTextColor(attributes.contentAccentContrastColor)
            myProfileEditCardViewMail.setIconTintColor(attributes.contentTextColor)
            myProfileEditCardViewMail.setTextColor(attributes.contentTextColor)
            myProfileEditCardViewMail.setPlaceholderColor(attributes.contentPlaceholderColor)
            if(mLoginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(mLoginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
                            R.string.placeholder_email))
            myProfileEditCardViewMail.setOnMyProfileEditButtonClickListener(this)

            myProfileEditCardViewPhoneNo.setBackgroundColor(attributes.contentBackgroundColor)
            myProfileEditCardViewPhoneNo.setTextButtonLeft("cancel")
            myProfileEditCardViewPhoneNo.setButtonsBackgroundColor(attributes.contentAccentColor)
            myProfileEditCardViewPhoneNo.setButtonsTextColor(attributes.contentAccentContrastColor)
            myProfileEditCardViewPhoneNo.setIconTintColor(attributes.contentTextColor)
            myProfileEditCardViewPhoneNo.setTextColor(attributes.contentTextColor)
            myProfileEditCardViewPhoneNo.setPlaceholderColor(attributes.contentPlaceholderColor)
            if(mLoginData.firstName.isNotEmpty()) myProfileEditCardViewNameOne.setText(mLoginData.firstName) else myProfileEditCardViewNameOne.setPlaceHolderText(getString(
                            R.string.placeholder_phone_number))
            myProfileEditCardViewPhoneNo.setOnMyProfileEditButtonClickListener(this)

            // profile image
            imageViewProfile.setBackgroundColor(attributes.contentBackgroundColor)
            if(mLoginData.avatarUrl.isNotEmpty() && URLUtil.isValidUrl(mLoginData.avatarUrl)) {
                Picasso.get()
                    .load(mLoginData.avatarUrl)
                    .into(imageViewProfile)
            } else {
                imageViewProfile.setImageResource(R.drawable.icon_placeholder)
                imageViewProfile.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
            }

            textViewImageEdit.setBackgroundColor(attributes.contentAccentColor)
            textViewImageEdit.setTextColor(attributes.contentAccentContrastColor)

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
                            //TODO DELETE AVATAR API CALL
                        }
                    }
                }
                    .generate()
                    .setTitle("Bitte Aktion Auswählen:")
                    .addButton("camera", R.string.camera)
                    .addButton("photo", R.string.gallery)
                    .addButton("delete", R.string.delete_avatar)
                    .addCancelButton()
                    .show()

            }

        }

        /*
        var attributes = Attributes()

        if(workspace!!.settings.style.isNotEmpty()) {
            when(workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)

                    //search bar
                    materialCardViewMyProfile.setCardBackgroundColor(getColorTemp(R.color.grey_light))
                    materialCardViewMyProfile.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewMyProfile.strokeWidth = dpToPx(1)
                    materialCardViewMyProfile.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_dark))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey))
                }

                "minimal" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 0

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)

                    //search bar
                    materialCardViewMyProfile.setCardBackgroundColor(getColorTemp(R.color.grey_light))
                    materialCardViewMyProfile.strokeColor = getColorTemp(R.color.white)
                    materialCardViewMyProfile.strokeWidth = dpToPx(1)
                    materialCardViewMyProfile.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_dark))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey))
                }

                "clean" -> {
                    attributes = setAttributesDefault()

                    //search bar
                    materialCardViewMyProfile.setCardBackgroundColor(getColorTemp(R.color.grey_light))
                    materialCardViewMyProfile.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewMyProfile.strokeWidth = dpToPx(1)
                    materialCardViewMyProfile.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_dark))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey))
                }
                else -> {
                    attributes = setAttributesDefault()

                    //search bar
                    materialCardViewMyProfile.setCardBackgroundColor(getColorTemp(R.color.grey_light))
                    materialCardViewMyProfile.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewMyProfile.strokeWidth = dpToPx(1)
                    materialCardViewMyProfile.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_dark))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey))
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
        if(workspaceSettings.contentBackgroundColor.isNotEmpty())  attributes.contentBackgroundColor = convertStringToColor(workspaceSettings.contentBackgroundColor)
        if(workspaceSettings.contentBorderColor.isNotEmpty()) attributes.contentBorderColor = convertStringToColor(workspaceSettings.contentBorderColor)
        if(workspaceSettings.contentBorderWidth.isNotEmpty()) attributes.contentBorderWidth = workspaceSettings.contentBorderWidth.toInt()
        if(workspaceSettings.contentCornerRadius.isNotEmpty()) attributes.contentCornerRadius = workspaceSettings.contentCornerRadius.toInt()

        if(workspaceSettings.contentTextColor.isNotEmpty()) attributes.contentTextColor = convertStringToColor(workspaceSettings.contentTextColor)
        if(workspaceSettings.contentAccentColor.isNotEmpty()) attributes.contentAccentColor = convertStringToColor(workspaceSettings.contentAccentColor)

        imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if (workspace.main.headerImageUrl.isNotEmpty()) {
                    imageViewHeader.visibility = VISIBLE

                    imageViewHeader.layoutParams.height = calculateViewHeight(
                        this@MyProfileActivity,
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

                val searchBarHeight = materialCardViewMyProfile.height //height is ready

                var spanCount = 2 // 2 columns for phone
                val tabletSize = resources.getBoolean(R.bool.isTablet)
                if (tabletSize) {
                    spanCount = 3 // 3 columns for tablet
                }
                val includeEdge = false

                val spacing = dpToPx(16)

                recyclerView.setPadding(0, imageViewHeaderHeight + searchBarHeight + spacing + spacing + spacing, 0, 0)
                recyclerView.addItemDecoration(MarginItemDecoration(dpToPx(16)))

                recyclerView.layoutManager = LinearLayoutManager(this@MyProfileActivity)

                recyclerView.adapter = UserListAdapter(mUserList, attributes) { position: Int ->

                }
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scrolling up 
                    viewFadeOut(materialCardViewMyProfile, 200)
                } else {
                    // Scrolling down
                    viewFadeIn(materialCardViewMyProfile)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        })

        loadUserList()
         */

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

    override fun onItemClick(search: String) {
        val userListTemp : MutableList<Model.User> = mutableListOf()
        userListTemp.addAll(mUserListDownloaded)
        val userList = userListTemp.filter { user -> user.firstName.contains(search, true) ||
                user.lastName.contains(search, true)
        }
        mUserList.clear()
        mUserList.addAll(addSection(userList.toMutableList()))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onClick() {
        TODO("Not yet implemented")
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


}
