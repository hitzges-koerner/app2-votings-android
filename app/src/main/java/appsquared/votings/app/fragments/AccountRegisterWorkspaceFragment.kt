package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.votings.android.R
import app.votings.android.databinding.FragmentAccountRegisterWorkspaceBinding
import appsquared.votings.app.AccountRegisterActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.Constant
import appsquared.votings.app.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AccountRegisterWorkspaceFragment : Fragment() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mListener: FragmentInteractionListener? = null

    private lateinit var binding: FragmentAccountRegisterWorkspaceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountRegisterWorkspaceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextAccountRegisterWorkspace.setText((activity as AccountRegisterActivity).getWorkspace())

        binding.editTextAccountRegisterWorkspace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewAccountRegisterWorkspaceError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setWorkspace(binding.editTextAccountRegisterWorkspace.text.toString())
            }
        })

        binding.buttonCardViewAccountRegisterWorkspaceNext.bindingButtonCardView.materialCardView.setOnClickListener {
            if(binding.editTextAccountRegisterWorkspace.text.isEmpty()) {
                binding.textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                binding.textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_empty)
                return@setOnClickListener
            }
            if(binding.editTextAccountRegisterWorkspace.text.toString().startsWith("-") || binding.editTextAccountRegisterWorkspace.text.toString().endsWith("-")) {
                binding.textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                binding.textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_pre_postfix)
                return@setOnClickListener
            }
            if(binding.editTextAccountRegisterWorkspace.text.length < 4) {
                binding.textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                binding.textViewAccountRegisterWorkspaceError.text = getString(R.string.error_workspace_letters)
                return@setOnClickListener
            }
            checkWorkspaceAvailable(binding.editTextAccountRegisterWorkspace.text.toString())
        }

        binding.buttonCardViewAccountRegisterWorkspacePrevious.bindingButtonCardView.materialCardView.setOnClickListener {
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
                    Log.d("LOGIN", error.message ?: "")

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            binding.textViewAccountRegisterWorkspaceError.visibility = View.VISIBLE
                            binding.textViewAccountRegisterWorkspaceError.text = getString(R.string.workspace_taken)
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