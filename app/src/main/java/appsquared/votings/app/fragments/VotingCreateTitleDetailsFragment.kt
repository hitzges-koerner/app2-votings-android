package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.votings.android.databinding.FragmentVotingCreateTitleDetailsBinding
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.VotingCreateActivity
import appsquared.votings.app.hideKeyboard
import appsquared.votings.app.Constant

class VotingCreateTitleDetailsFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentVotingCreateTitleDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVotingCreateTitleDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextVotingCreateTitle.setText((activity as VotingCreateActivity).getVotingTitle())
        binding.editTextVotingCreateDetails.setText((activity as VotingCreateActivity).getDescription())

        binding.editTextVotingCreateTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewVotingCreateTitleError.visibility = View.INVISIBLE
                (activity as VotingCreateActivity).setVotingTitle(binding.editTextVotingCreateTitle.text.toString())
            }
        })

        binding.editTextVotingCreateDetails.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (activity as VotingCreateActivity).setDescription(binding.editTextVotingCreateDetails.text.toString())
            }
        })

        binding.editTextVotingCreateTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        binding.editTextVotingCreateDetails.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        binding.buttonCardViewVotingCreateTitleDetailsNext.bindingButtonCardView.materialCardView.setOnClickListener {
            if(binding.editTextVotingCreateTitle.text.isEmpty()) {
                binding.textViewVotingCreateTitleError.visibility = View.VISIBLE
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