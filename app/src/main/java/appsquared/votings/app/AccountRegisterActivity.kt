package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import appsquared.votings.app.fragments.*
import appsquared.votings.app.views.DecisionDialog
import framework.base.constant.Constant


class AccountRegisterActivity : BaseActivity(),
    FragmentInteractionListener {

    object WorkspaceData {
        var firstName: String = ""
        var lastName: String = ""
        var workspace: String = ""
        var email: String = ""
        var source: String = "android"
    }

    private var mFragmentId: Int = 0

    override fun fragmentInteraction(action: Int, javaClass: Class<*>) {
        when(action) {
            Constant.BACK -> {
                previousFragment()
            }
            Constant.NEXT -> {
                if(mFragmentId == 3) setCancelButtonActive(false)
                if(mFragmentId == 4) finish()
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

        val fragmentArray = arrayOf("AccountRegisterInfoFragment",
            "AccountRegisterNameFragment",
            "AccountRegisterWorkspaceFragment",
            "AccountRegisterMailFragment",
            "AccountRegisterConfirmationFragment")

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
        setContentView(R.layout.activity_account_registration)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        WorkspaceData.workspace = ""
        WorkspaceData.email = ""
        WorkspaceData.firstName = ""
        WorkspaceData.lastName = ""

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
    }

    private fun getFragmentClassName(fragmentName: String) : Class<*> {
        when(fragmentName) {
            "AccountRegisterInfoFragment" -> return AccountRegisterInfoFragment::class.java
            "AccountRegisterNameFragment" -> return AccountRegisterNameFragment::class.java
            "AccountRegisterMailFragment" -> return AccountRegisterMailFragment::class.java
            "AccountRegisterWorkspaceFragment" -> return AccountRegisterWorkspaceFragment::class.java
            "AccountRegisterConfirmationFragment" -> return AccountRegisterConfirmationFragment::class.java
        }
        return FragmentNotFoundFragment::class.java
    }

    override fun clickToolbarCancelButton() {
        super.clickToolbarCancelButton()

        if(mFragmentId == 0 || mFragmentId == 4) finish()
        else {
            DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    finish()
                }
            }.generate()
                .setButtonRightName(getString(R.string.yes))
                .setButtonLeftName(getString(R.string.no))
                .setMessage(getString(R.string.cancel_account_registration))
                .show()
        }
    }

    fun setEmail(email: String) {
        WorkspaceData.email = email
    }

    fun setWorkspace(workspace: String) {
        WorkspaceData.workspace = workspace
    }

    fun setFirstName(firstName: String) {
        WorkspaceData.firstName = firstName
    }

    fun setLastName(lastName: String) {
        WorkspaceData.lastName = lastName
    }

    fun getEmail() : String {
        return WorkspaceData.email
    }

    fun getWorkspace() : String {
        return WorkspaceData.workspace
    }

    fun getFirstName() : String {
        return WorkspaceData.firstName
    }

    fun getLastName() : String {
        return WorkspaceData.lastName
    }

    fun getSource() : String {
        return WorkspaceData.source
    }

    override fun onBackPressed() {
        clickToolbarCancelButton()
    }
}
