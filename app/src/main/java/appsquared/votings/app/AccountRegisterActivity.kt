package appsquared.votings.app

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import app.votings.android.R
import app.votings.android.databinding.ActivityAccountRegistrationBinding
import app.votings.android.databinding.DialogDecisionBinding
import appsquared.votings.app.fragments.AccountRegisterConfirmationFragment
import appsquared.votings.app.fragments.AccountRegisterInfoFragment
import appsquared.votings.app.fragments.AccountRegisterMailFragment
import appsquared.votings.app.fragments.AccountRegisterNameFragment
import appsquared.votings.app.fragments.AccountRegisterWorkspaceFragment
import appsquared.votings.app.fragments.FragmentNotFoundFragment
import appsquared.votings.app.views.DecisionDialog


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
        when (action) {
            Constant.BACK -> {
                previousFragment()
            }

            Constant.NEXT -> {
                if (mFragmentId == 3) setCancelButtonActive(false)
                if (mFragmentId == 4) finish()
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

        val fragmentArray = arrayOf(
            "AccountRegisterInfoFragment",
            "AccountRegisterNameFragment",
            "AccountRegisterWorkspaceFragment",
            "AccountRegisterMailFragment",
            "AccountRegisterConfirmationFragment"
        )

        val fragmentName = fragmentArray[mFragmentId]

        val fragmentClass = getFragmentClassName(fragmentName)
        replaceFragment(fragmentClass)
    }

    private fun replaceFragment(fragmentClass: Class<*>) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameLayout.id, fragmentClass.newInstance() as Fragment)
            .commit()
    }

    private lateinit var binding: ActivityAccountRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment()
    }

    fun getColorTemp(color: Int): Int {
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
        /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, AccountRegisterInfoFragment.newInstance())
        transaction.addToBackStack(null)
        transaction.commit()
         */
    }

    private fun getFragmentClassName(fragmentName: String): Class<*> {
        when (fragmentName) {
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

        if (mFragmentId == 0 || mFragmentId == 4) finish()
        else {
            DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    finish()
                }
            }.generate(DialogDecisionBinding.inflate(layoutInflater))
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

    fun getEmail(): String {
        return WorkspaceData.email
    }

    fun getWorkspace(): String {
        return WorkspaceData.workspace
    }

    fun getFirstName(): String {
        return WorkspaceData.firstName
    }

    fun getLastName(): String {
        return WorkspaceData.lastName
    }

    fun getSource(): String {
        return WorkspaceData.source
    }

    override fun onBackPressed() {
        super.onBackPressed()
        clickToolbarCancelButton()
    }
}
