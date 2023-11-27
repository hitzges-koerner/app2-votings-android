package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.votings.android.R
import app.votings.android.databinding.FragmentVotingCreateTypeBinding
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.VotingCreateActivity
import appsquared.votings.app.Constant

class VotingCreateTypeFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentVotingCreateTypeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVotingCreateTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.SINGLE) setCheckedVotingTypeSingle()
        if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.MULTI) setCheckedVotingTypeMulti()

        binding.materialCardViewChoiceSingle.setOnClickListener {
            setCheckedVotingTypeSingle()
        }

        binding.materialCardViewChoiceMulti.setOnClickListener {
            setCheckedVotingTypeMulti()
        }

        binding.buttonCardViewVotingCreateTypePrevious.bindingButtonCardView.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }

        binding.buttonCardViewVotingCreateTypeNext.bindingButtonCardView.materialCardView.setOnClickListener {
            if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.NONE) {
                binding.textViewVotingCreateTypeError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

    }

    fun setCheckedVotingTypeSingle() {
        binding.textViewVotingCreateTypeError.visibility = View.GONE
        (activity as VotingCreateActivity).setChoiceType(VotingCreateActivity.ChoiceType.SINGLE)
        binding.imageViewChoiceSingleChecked.setImageResource(R.drawable.ic_round_checked)
        binding.imageViewChoiceMultiChecked.setImageResource(R.drawable.ic_round_unchecked)
    }

    fun setCheckedVotingTypeMulti() {
        binding.textViewVotingCreateTypeError.visibility = View.GONE
        (activity as VotingCreateActivity).setChoiceType(VotingCreateActivity.ChoiceType.MULTI)
        binding.imageViewChoiceSingleChecked.setImageResource(R.drawable.ic_round_unchecked)
        binding.imageViewChoiceMultiChecked.setImageResource(R.drawable.ic_round_checked)
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
        fun newInstance() = VotingCreateTypeFragment()
    }
}