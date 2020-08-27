package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.R
import framework.base.constant.Constant
import kotlinx.android.synthetic.main.fragment_account_register_cornfirmation.*
import kotlinx.android.synthetic.main.fragment_account_register_info.*
import kotlinx.android.synthetic.main.text_card_view.view.*

class AccountRegisterConfirmationFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_register_cornfirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCardViewAccountRegisterConfirmation.materialCardView.setOnClickListener {
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
        fun newInstance() = AccountRegisterConfirmationFragment()
    }
}