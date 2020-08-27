package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appsquared.votings.app.VotingCreateActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.R
import framework.base.constant.Constant
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_voting_create_type.*

class VotingCreateTypeFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting_create_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.SINGLE) setCheckedVotingTypeSingle()
        if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.MULTI) setCheckedVotingTypeMulti()

        materialCardViewChoiceSingle.setOnClickListener {
            setCheckedVotingTypeSingle()
        }

        materialCardViewChoiceMulti.setOnClickListener {
            setCheckedVotingTypeMulti()
        }

        buttonCardViewVotingCreateTypePrevious.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
        }

        buttonCardViewVotingCreateTypeNext.materialCardView.setOnClickListener {
            if((activity as VotingCreateActivity).getChoiceType() == VotingCreateActivity.ChoiceType.NONE) {
                textViewVotingCreateTypeError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

    }

    fun setCheckedVotingTypeSingle() {
        textViewVotingCreateTypeError.visibility = View.GONE
        (activity as VotingCreateActivity).setChoiceType(VotingCreateActivity.ChoiceType.SINGLE)
        imageViewChoiceSingleChecked.setImageResource(R.drawable.ic_round_checked)
        imageViewChoiceMultiChecked.setImageResource(R.drawable.ic_round_unchecked)
    }

    fun setCheckedVotingTypeMulti() {
        textViewVotingCreateTypeError.visibility = View.GONE
        (activity as VotingCreateActivity).setChoiceType(VotingCreateActivity.ChoiceType.MULTI)
        imageViewChoiceSingleChecked.setImageResource(R.drawable.ic_round_unchecked)
        imageViewChoiceMultiChecked.setImageResource(R.drawable.ic_round_checked)
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