package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.activity_user_list.imageViewBackground


class UserListActivity : AppCompatActivity(), EditTextWithClear.OnEditTextWithClearClickListener,
    TextView.OnEditorActionListener {

    private lateinit var mUserListDownloaded: MutableList<Model.User>
    private lateinit var mUserList: MutableList<Model.User>

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val workspace: Model.WorkspaceResponse? = AppData().getSavedObjectFromPreference(this, "workspace", Model.WorkspaceResponse::class.java)

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(toolbarCustom) { view, insets ->
            statusBarSize = insets.systemWindowInsetTop

            val params: ViewGroup.LayoutParams = toolbarCustom.layoutParams
            val temp = dpToPx(56)
            params.height = statusBarSize + temp
            toolbarCustom.requestLayout()

            insets
        }

        editTextSearch.setOnEditWithClearClickListener(this)
        editTextSearch.setOnEditorActionListener(this)

        mUserListDownloaded = mutableListOf<Model.User>()
        mUserList = mutableListOf<Model.User>()
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
                    materialCardViewSearch.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewSearch.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewSearch.strokeWidth = dpToPx(1)
                    materialCardViewSearch.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_60))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey_144))
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
                    materialCardViewSearch.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewSearch.strokeColor = getColorTemp(R.color.white)
                    materialCardViewSearch.strokeWidth = dpToPx(1)
                    materialCardViewSearch.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_60))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey_144))
                }

                "clean" -> {
                    attributes = setAttributesDefault()

                    //search bar
                    materialCardViewSearch.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewSearch.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewSearch.strokeWidth = dpToPx(1)
                    materialCardViewSearch.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_60))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey_144))
                }
                else -> {
                    attributes = setAttributesDefault()

                    //search bar
                    materialCardViewSearch.setCardBackgroundColor(getColorTemp(R.color.grey_230))
                    materialCardViewSearch.strokeColor = getColorTemp(R.color.colorAccent)
                    materialCardViewSearch.strokeWidth = dpToPx(1)
                    materialCardViewSearch.radius = dpToPx(5).toFloat()

                    editTextSearch.setTextColor(getColorTemp(R.color.grey_60))
                    editTextSearch.setHintTextColor(getColorTemp(R.color.grey_144))
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
                        this@UserListActivity,
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

                val searchBarHeight = materialCardViewSearch.height //height is ready

                var spanCount = 2 // 2 columns for phone
                val tabletSize = resources.getBoolean(R.bool.isTablet)
                if (tabletSize) {
                    spanCount = 3 // 3 columns for tablet
                }
                val includeEdge = false

                val spacing = dpToPx(16)

                recyclerView.setPadding(0, imageViewHeaderHeight + searchBarHeight + spacing + spacing + spacing, 0, 0)
                recyclerView.addItemDecoration(MarginItemDecoration(dpToPx(16)))

                recyclerView.layoutManager = LinearLayoutManager(this@UserListActivity)

                recyclerView.adapter = UserListAdapter(mUserList, attributes) { position: Int ->

                }
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scrolling up 
                    viewFadeOut(materialCardViewSearch, 200)
                } else {
                    // Scrolling down
                    viewFadeIn(materialCardViewSearch)
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
        attributes.contentAccentColor = getColorTemp(R.color.black)
        attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
        return attributes
    }

    private fun loadUserList() {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.getUserList("Bearer $token", workspace!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mUserListDownloaded.clear()
                    mUserListDownloaded.addAll(result)
                    mUserListDownloaded.sortBy { it.lastName }
                    mUserList.clear()
                    mUserList.addAll(addSection(mUserListDownloaded))
                    recyclerView.adapter?.notifyDataSetChanged()

                    materialCardViewSearch.visibility = VISIBLE

                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
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

}
