package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import appsquared.votings.app.fragments.*
import appsquared.votings.app.views.DecisionDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class VotingCreateActivity : BaseActivity(),
    FragmentInteractionListener {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    val mUserList = mutableListOf<Model.User>()

    object VotingCreateData {
        var title: String = ""
        var description: String = ""
        var choiceType = ChoiceType.NONE
        var choices: MutableList<String> = mutableListOf()
        var userType = UserType.NONE
        var users = mutableListOf<Model.User>()
    }

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting_create)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        VotingCreateData.title = ""
        VotingCreateData.description = ""
        VotingCreateData.title = ""
        VotingCreateData.description = ""
        VotingCreateData.choiceType = ChoiceType.NONE
        VotingCreateData.choices = mutableListOf()
        VotingCreateData.userType = UserType.NONE
        VotingCreateData.users = mutableListOf()

        setScreenTitle(getString(R.string.register_workspace))
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

        if(mFragmentId == 0) finish()
        else {
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
        }
    }

    fun setVotingTitle(title: String) {
        VotingCreateData.title = title
    }

    fun setDescription(description: String) {
        VotingCreateData.description = description
    }

    fun getVotingTitle() : String {
        return VotingCreateData.title
    }

    fun getDescription() : String {
        return VotingCreateData.description
    }

    fun setChoiceType(choiceType: ChoiceType) {
        VotingCreateData.choiceType = choiceType
    }

    fun getChoiceType(): ChoiceType {
        return VotingCreateData.choiceType
    }

    fun setUserType(userType: UserType) {
        VotingCreateData.userType = userType
    }

    fun getUserType(): UserType {
        return VotingCreateData.userType
    }

    fun setUsers(users: MutableList<Model.User>) {
        VotingCreateData.users = users
    }

    fun getUsers(): MutableList<Model.User> {
        return VotingCreateData.users
    }

    fun setChoices(choices: MutableList<String>) {
        VotingCreateData.choices = choices
    }

    fun getChoices(): MutableList<String> {
        return VotingCreateData.choices
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
