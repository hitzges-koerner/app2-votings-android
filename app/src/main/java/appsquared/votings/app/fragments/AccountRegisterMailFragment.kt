package appsquared.votings.app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.votings.android.databinding.FragmentAccountRegisterMailBinding
import appsquared.votings.app.AccountRegisterActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.LegalDocsActivity
import appsquared.votings.app.isEmailValid
import appsquared.votings.app.Constant
import appsquared.votings.app.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class AccountRegisterMailFragment : Fragment() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAccountRegisterMailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountRegisterMailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextAccountRegisterMail.setText((activity as AccountRegisterActivity).getEmail())

        binding.editTextAccountRegisterMail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewAccountRegisterEmailError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setEmail(binding.editTextAccountRegisterMail.text.toString())
            }
        })

        binding.buttonCardViewAccountRegisterMailNext.bindingButtonCardView.materialCardView.setOnClickListener {
            if(binding.editTextAccountRegisterMail.text.isEmpty() || !isEmailValid(binding.editTextAccountRegisterMail.text)) {
                showError()
                return@setOnClickListener
            }
            if(!binding.checkBoxAccountRegisterTerms.isChecked) {
                showErrorTerms()
                return@setOnClickListener
            }
            registerAccount()
        }

        binding.buttonCardViewAccountRegisterMailPrevious.bindingButtonCardView.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }

        binding.textViewAccountRegisterTermsClick.setOnClickListener {
            startActivity(Intent(context, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.TERMS).putExtra(LegalDocsActivity.ACCOUNT_REGISTER_TERMS, true))
        }

        binding.checkBoxAccountRegisterTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.textViewAccountRegisterEmailTermsError.visibility = View.INVISIBLE
        }
    }

    private fun showErrorTerms() {
        binding.textViewAccountRegisterEmailTermsError.visibility = View.VISIBLE
    }

    private fun registerAccount() {

        val jsonData = JSONObject()
        jsonData.put("firstname", (activity as AccountRegisterActivity).getFirstName())
        jsonData.put("lastname",  (activity as AccountRegisterActivity).getLastName())
        jsonData.put("workspace",  (activity as AccountRegisterActivity).getWorkspace())
        jsonData.put("email",  (activity as AccountRegisterActivity).getEmail())
        jsonData.put("source",  (activity as AccountRegisterActivity).getSource())

        disposable = apiService.registerAccount(jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    onButtonPressed(Constant.NEXT)
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            return@subscribe
                        }
                    }
                }
            )
    }

    fun showError() {
        binding.textViewAccountRegisterEmailError.visibility = View.VISIBLE
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
        fun newInstance() = AccountRegisterMailFragment()
    }
}