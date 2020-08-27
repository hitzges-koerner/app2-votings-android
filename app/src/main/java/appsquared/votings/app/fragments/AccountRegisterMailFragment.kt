package appsquared.votings.app.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appsquared.votings.app.*
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_account_register_mail.*
import kotlinx.android.synthetic.main.fragment_account_register_name.*
import kotlinx.android.synthetic.main.fragment_account_register_workspace.*
import kotlinx.android.synthetic.main.fragment_account_register_workspace.buttonCardViewAccountRegisterWorkspacePrevious
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_register_mail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextAccountRegisterMail.setText((activity as AccountRegisterActivity).getEmail())

        editTextAccountRegisterMail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewAccountRegisterEmailError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setEmail(editTextAccountRegisterMail.text.toString())
            }
        })

        buttonCardViewAccountRegisterMailNext.materialCardView.setOnClickListener {
            if(editTextAccountRegisterMail.text.isEmpty() || !isEmailValid(editTextAccountRegisterMail.text)) {
                showError()
                return@setOnClickListener
            }
            if(!checkBoxAccountRegisterTerms.isChecked) {
                showErrorTerms()
                return@setOnClickListener
            }
            registerAccount()
        }

        buttonCardViewAccountRegisterMailPrevious.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }

        textViewAccountRegisterTermsClick.setOnClickListener {
            startActivity(Intent(context, LegalDocsActivity::class.java).putExtra(LegalDocsActivity.LEGAL_DOC_TYPE, LegalDocsActivity.TERMS).putExtra(LegalDocsActivity.ACCOUNT_REGISTER_TERMS, true))
        }

        checkBoxAccountRegisterTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            textViewAccountRegisterEmailTermsError.visibility = View.INVISIBLE
        }
    }

    private fun showErrorTerms() {
        textViewAccountRegisterEmailTermsError.visibility = View.VISIBLE
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
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            return@subscribe
                        }
                    }
                }
            )
    }

    fun showError() {
        textViewAccountRegisterEmailError.visibility = View.VISIBLE
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