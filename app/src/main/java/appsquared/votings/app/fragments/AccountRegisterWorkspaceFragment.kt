package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import appsquared.votings.app.AccountRegisterActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.PreferenceNames
import appsquared.votings.app.R
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_account_register_mail.*
import kotlinx.android.synthetic.main.fragment_account_register_name.*
import kotlinx.android.synthetic.main.fragment_account_register_workspace.*

class AccountRegisterWorkspaceFragment : Fragment() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mListener: FragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_register_workspace, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextAccountRegisterWorkspace.setText((activity as AccountRegisterActivity).getWorkspace())

        editTextAccountRegisterWorkspace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewAccountRegisterWorkspaceError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setWorkspace(editTextAccountRegisterWorkspace.text.toString())
            }
        })

        buttonCardViewAccountRegisterWorkspaceNext.materialCardView.setOnClickListener {
            if(editTextAccountRegisterWorkspace.text.isEmpty()) {
                textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_empty)
                return@setOnClickListener
            }
            if(editTextAccountRegisterWorkspace.text.toString().startsWith("-") || editTextAccountRegisterWorkspace.text.toString().endsWith("-")) {
                textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_pre_postfix)
                return@setOnClickListener
            }
            if(editTextAccountRegisterWorkspace.text.length < 4) {
                textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_letters)
                return@setOnClickListener
            }
            checkWorkspaceAvailable(editTextAccountRegisterWorkspace.text.toString())
        }

        buttonCardViewAccountRegisterWorkspacePrevious.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }
    }

    fun showError() {

    }

    fun onButtonPressed(action: Int) {
        if (mListener != null) {
            mListener!!.fragmentInteraction(action, this.javaClass)
        }
    }

    private fun checkWorkspaceAvailable(workspace: String) {

        disposable = apiService.checkWorkspaceAvailable(workspace)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    onButtonPressed(Constant.NEXT)
                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                            textViewAccountRegisterWorkspaceError.text = getString(R.string.workspace_taken)
                            return@subscribe
                        }
                    }
                }
            )
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
        fun newInstance() = AccountRegisterWorkspaceFragment()
    }
}