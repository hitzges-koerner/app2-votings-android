package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import appsquared.votings.app.fragments.*
import appsquared.votings.app.views.ListDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class VotingCreateActivity : BaseActivity(),
    FragmentInteractionListener {

    private var mVotingCreateData: VotingCreateData = VotingCreateData()
    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    val mUserList = mutableListOf<Model.User>()

    data class VotingCreateData(
        var title: String = "",
        var description: String = "",
        var choiceType: ChoiceType = ChoiceType.NONE,
        var choices: MutableList<String> = mutableListOf(),
        var userType: UserType = UserType.NONE,
        var users: MutableList<Model.User> = mutableListOf<Model.User>()
    )

    private var mFragmentId: Int = 0

    override fun fragmentInteraction(action: Int, javaClass: Class<*>) {
        when(action) {
            Constant.BACK -> {
                previousFragment()
            }
            Constant.NEXT -> {
                if(mFragmentId == 4) setCancelButtonActive(false)
                if(mFragmentId == 5) finish()
                else {
                    setCancelButtonActive(true)
                    nextFragment()
                }
            }
        }
    }

    private fun nextFragment() {
        mFragmentId++
        loadFragment()
    }

    private fun previousFragment() {
        mFragmentId--
        loadFragment()
    }

    fun loadFragment() {

        val fragmentArray = arrayOf("VotingCreateInfoFragment",
            "VotingCreateTitleDetailsFragment",
            "VotingCreateChoicesFragment",
            "VotingCreateTypeFragment",
            "VotingCreateUsersFragment",
            "VotingCreateConfirmationFragment")

        val fragmentName = fragmentArray[mFragmentId]

        val fragmentClass = getFragmentClassName(fragmentName)
        replaceFragment(fragmentClass)
    }

    private fun replaceFragment(fragmentClass: Class<*>) {

        val fragment = fragmentClass.newInstance() as Fragment

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()

        if(fragmentClass == VotingCreateInfoFragment::class.java) {
            val votingAvailable = AppData().isSavedObjectFromPreferenceAvailable(this, PreferenceNames.VOTING_CREATE_DATA)
            if(votingAvailable) {
                ListDialog(this) { tag: String ->
                    when (tag) {
                        "open" -> {
                            mVotingCreateData = AppData().getSavedObjectFromPreference(this, PreferenceNames.VOTING_CREATE_DATA, VotingCreateData::class.java)
                                ?: VotingCreateData()
                            nextFragment()
                        }
                        "discard" -> {
                            AppData().deleteSavedObjectFromPreference(this, PreferenceNames.VOTING_CREATE_DATA)
                        }
                    }
                }
                    .generate()
                    .addButton("open", R.string.voting_dialog_create_button_open)
                    .addButton("discard", R.string.voting_dialog_create_button_discard)
                    .addCancelButton()
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting_create)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.voting_create))
        setCancelButtonActive(true)
        removeImageHeader()

        val workspace = mWorkspace

        loadFragment()
        /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, AccountRegisterInfoFragment.newInstance())
        transaction.addToBackStack(null)
        transaction.commit()
         */

        loadUserList()
    }

    private fun getFragmentClassName(fragmentName: String) : Class<*> {
        when(fragmentName) {
            "VotingCreateInfoFragment" -> return VotingCreateInfoFragment::class.java
            "VotingCreateTitleDetailsFragment" -> return VotingCreateTitleDetailsFragment::class.java
            "VotingCreateChoicesFragment" -> return VotingCreateChoicesFragment::class.java
            "VotingCreateTypeFragment" -> return VotingCreateTypeFragment::class.java
            "VotingCreateUsersFragment" -> return VotingCreateUsersFragment::class.java
            "VotingCreateConfirmationFragment" -> return VotingCreateConfirmationFragment::class.java
        }
        return FragmentNotFoundFragment::class.java
    }

    override fun clickToolbarCancelButton() {
        super.clickToolbarCancelButton()

        if(mFragmentId == 0 || votingsDataIsEmpty()) finish()
        else {
            /*
            DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    finish()
                }
            }.generate()
                .setButtonRightName(getString(R.string.yes))
                .setButtonLeftName(getString(R.string.no))
                .setMessage("Do you want to cancel creating a new voting?")
                .show()
             */

            ListDialog(this) { tag: String ->
                when (tag) {
                    "discard" -> {
                        AppData().deleteSavedObjectFromPreference(this, PreferenceNames.VOTING_CREATE_DATA)
                        finish()
                    }
                    "save" -> {
                        AppData().saveObjectToSharedPreference(this, PreferenceNames.VOTING_CREATE_DATA, mVotingCreateData)
                        finish()
                    }
                }
            }
                .generate()
                .setTitle(R.string.voting_dialog_cancel_title)
                .addButton("discard", R.string.voting_dialog_cancel_button_discard)
                .addButton("save", R.string.voting_dialog_cancel_button_save)
                .addCancelButton()
                .show()
        }
    }

    private fun votingsDataIsEmpty(): Boolean {
        if(mVotingCreateData.title.isNotEmpty()) return false
        if(mVotingCreateData.description.isNotEmpty()) return false
        return true
    }

    fun setVotingTitle(title: String) {
        mVotingCreateData.title = title
    }

    fun setDescription(description: String) {
        mVotingCreateData.description = description
    }

    fun getVotingTitle() : String {
        return mVotingCreateData.title
    }

    fun getDescription() : String {
        return mVotingCreateData.description
    }

    fun setChoiceType(choiceType: ChoiceType) {
        mVotingCreateData.choiceType = choiceType
    }

    fun getChoiceType(): ChoiceType {
        return mVotingCreateData.choiceType
    }

    fun setUserType(userType: UserType) {
        mVotingCreateData.userType = userType
    }

    fun getUserType(): UserType {
        return mVotingCreateData.userType
    }

    fun setUsers(users: MutableList<Model.User>) {
        mVotingCreateData.users = users
    }

    fun getUsers(): MutableList<Model.User> {
        return mVotingCreateData.users
    }

    fun setChoices(choices: MutableList<String>) {
        mVotingCreateData.choices = choices
    }

    fun getChoices(): MutableList<String> {
        return mVotingCreateData.choices
    }

    enum class ChoiceType {
        NONE, SINGLE, MULTI
    }

    enum class UserType {
        NONE, ALL, SELECTED
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
                    mUserList.addAll(result)
                    setUsers(result)
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

    override fun onBackPressed() {
        clickToolbarCancelButton()
    }
}
