package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import appsquared.votings.app.*
import appsquared.votings.app.views.DecisionDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_voting_create_choices.*
import kotlinx.android.synthetic.main.fragment_voting_create_users.*
import org.json.JSONArray
import org.json.JSONObject

class VotingCreateUsersFragment : Fragment() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mUsers: MutableList<Model.User> = mutableListOf()
    private var mListener: FragmentInteractionListener? = null
    val mUserList = mutableListOf<Model.User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting_create_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if((activity as VotingCreateActivity).getUserType() == VotingCreateActivity.UserType.ALL) setCheckedUserTypeAll()
        if((activity as VotingCreateActivity).getUserType() == VotingCreateActivity.UserType.SELECTED) setCheckedUserTypeSelected()
        mUsers = (activity as VotingCreateActivity).getUsers()

        materialCardViewUserAll.setOnClickListener {
            setCheckedUserTypeAll()
        }

        materialCardViewUserSelected.setOnClickListener {
            setCheckedUserTypeSelected()
        }

        buttonCardViewVotingCreateUsersPrevious.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }

        buttonCardViewVotingCreateUsersNext.materialCardView.setOnClickListener {
            if((activity as VotingCreateActivity).getUserType() == VotingCreateActivity.UserType.NONE) {
                textViewVotingCreateUsersError.text = getString(R.string.voting_create_error_at_least_two_participants)
                textViewVotingCreateUsersError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            val selectedUsers = mUsers.filter { it.isSelected == true }
            if(selectedUsers.size >= 2 || isAllUsers()) {
                DecisionDialog(requireContext()) {
                    if(it == DecisionDialog.RIGHT) createVoting()
                }.generate()
                    .setCancelable(true)
                    .setTitle(getString(R.string.voting_create) + "?")
                    .setMessage(getString(R.string.voting_create_send_dialog_text))
                    .setButtonLeftName(getString(R.string.no))
                    .setButtonRightName(getString(R.string.yes))
                    .show()
            } else {
                textViewVotingCreateUsersError.text = getString(R.string.voting_create_error_at_least_two_participants)
                textViewVotingCreateUsersError.visibility = View.VISIBLE
                return@setOnClickListener
            }
        }

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)
        recyclerViewVotingCreateUsers.setPadding(0, spacing, 0, 0)
        recyclerViewVotingCreateUsers.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                dpToPx(4),
                includeEdge
            )
        )
        recyclerViewVotingCreateUsers.layoutManager = GridLayoutManager(context, spanCount)

        recyclerViewVotingCreateUsers.adapter = VotingCreateUsersListAdapter(mUsers) { position: Int ->
            textViewVotingCreateUsersError.visibility = View.GONE
            mUsers[position].isSelected = !mUsers[position].isSelected
            recyclerViewVotingCreateUsers.adapter?.notifyItemChanged(position)
        }
    }

    fun setCheckedUserTypeAll() {
        recyclerViewVotingCreateUsers.visibility = View.GONE
        textViewVotingCreateUsersError.visibility = View.GONE
        (activity as VotingCreateActivity).setUserType(VotingCreateActivity.UserType.ALL)
        imageViewUserAllChecked.setImageResource(R.drawable.ic_round_checked)
        imageViewUserSelectedChecked.setImageResource(R.drawable.ic_round_unchecked)
    }

    fun setCheckedUserTypeSelected() {
        recyclerViewVotingCreateUsers.visibility = View.VISIBLE
        textViewVotingCreateUsersError.visibility = View.GONE
        (activity as VotingCreateActivity).setUserType(VotingCreateActivity.UserType.SELECTED)
        imageViewUserAllChecked.setImageResource(R.drawable.ic_round_unchecked)
        imageViewUserSelectedChecked.setImageResource(R.drawable.ic_round_checked)
    }

    fun isSingleChoice() : String {
        return if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.SINGLE) "1"
        else "0"
    }

    fun isAllUsers() : Boolean {
        return (activity as VotingCreateActivity).getUserType() == VotingCreateActivity.UserType.ALL
    }

    fun getUsers() : JSONArray {
        val jsonArrayUser: JSONArray = JSONArray()
        if((activity as VotingCreateActivity).getUserType() == VotingCreateActivity.UserType.ALL) {
            (activity as VotingCreateActivity).getUsers().forEach { jsonArrayUser.put(it.userId) }
            return jsonArrayUser
        }
        else {
            (activity as VotingCreateActivity).getUsers().filter { it.isSelected }.toMutableList().forEach { jsonArrayUser.put(it.userId) }
            return jsonArrayUser
        }
    }

    fun getChoices() : JSONArray {
        val jsonArrayChoices: JSONArray = JSONArray()
        (activity as VotingCreateActivity).getChoices().forEach { jsonArrayChoices.put(it) }
        return jsonArrayChoices
    }

    private fun createVoting() {

        textViewVotingCreateUsersError.visibility = View.GONE

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "") ?: ""
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "") ?: ""

        val jsonData = JSONObject()
        jsonData.put("title", (activity as VotingCreateActivity).getVotingTitle())
        jsonData.put("description",  (activity as VotingCreateActivity).getDescription())
        jsonData.put("choices", getChoices() )
        jsonData.put("isSingleChoice",  isSingleChoice())
        jsonData.put("isPublicVoting",  "1")
        jsonData.put("users",  getUsers())
        jsonData.put("isAllUsers",  isAllUsers())

        disposable = apiService.createVoting("Bearer $token", workspace, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    onButtonPressed(Constant.NEXT)
                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 424 ) {
                            textViewVotingCreateUsersError.text = getString(R.string.voting_create_error_too_much_user_selected)
                            textViewVotingCreateUsersError.visibility = View.VISIBLE
                            return@subscribe
                        }
                        if(error.code() == 409 ) {
                            return@subscribe
                        }
                    }
                }
            )
    }

    fun onButtonPressed(action: Int) {
        if (mListener != null) {
            mListener!!.fragmentInteraction(action, this.javaClass)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = VotingCreateUsersFragment()
    }
}