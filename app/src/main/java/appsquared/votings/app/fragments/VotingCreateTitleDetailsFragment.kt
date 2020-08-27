package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.R
import appsquared.votings.app.VotingCreateActivity
import appsquared.votings.app.hideKeyboard
import framework.base.constant.Constant
import kotlinx.android.synthetic.main.fragment_voting_create_title_details.*
import kotlinx.android.synthetic.main.text_card_view.view.*

class VotingCreateTitleDetailsFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting_create_title_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextVotingCreateTitle.setText((activity as VotingCreateActivity).getVotingTitle())
        editTextVotingCreateDetails.setText((activity as VotingCreateActivity).getDescription())

        editTextVotingCreateTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewVotingCreateTitleError.visibility = View.INVISIBLE
                (activity as VotingCreateActivity).setVotingTitle(editTextVotingCreateTitle.text.toString())
            }
        })

        editTextVotingCreateDetails.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (activity as VotingCreateActivity).setDescription(editTextVotingCreateDetails.text.toString())
            }
        })

        editTextVotingCreateTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        editTextVotingCreateDetails.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        buttonCardViewVotingCreateTitleDetailsNext.materialCardView.setOnClickListener {
            if(editTextVotingCreateTitle.text.isEmpty()) {
                textViewVotingCreateTitleError.visibility = View.VISIBLE
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
        fun newInstance() = VotingCreateTitleDetailsFragment()
    }
}