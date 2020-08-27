package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import appsquared.votings.app.AccountRegisterActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.R
import framework.base.constant.Constant
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_account_register_info.*
import kotlinx.android.synthetic.main.fragment_account_register_mail.*
import kotlinx.android.synthetic.main.fragment_account_register_name.*

class AccountRegisterNameFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_register_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextAccountRegisterNameFirst.setText((activity as AccountRegisterActivity).getFirstName())
        editTextAccountRegisterNameLast.setText((activity as AccountRegisterActivity).getLastName())

        editTextAccountRegisterNameFirst.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewAccountRegisterNameFirstError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setFirstName(editTextAccountRegisterNameFirst.text.toString())
            }
        })

        editTextAccountRegisterNameLast.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewAccountRegisterNameLastError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setLastName(editTextAccountRegisterNameLast.text.toString())
            }
        })

        buttonCardViewAccountRegisterNameNext.materialCardView.setOnClickListener {
            if(editTextAccountRegisterNameFirst.text.isEmpty()) {
                textViewAccountRegisterNameFirstError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(editTextAccountRegisterNameLast.text.isEmpty()) {
                textViewAccountRegisterNameLastError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

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
        fun newInstance() = AccountRegisterNameFragment()
    }
}