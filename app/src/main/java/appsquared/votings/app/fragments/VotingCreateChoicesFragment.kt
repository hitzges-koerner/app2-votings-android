package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.DialogInputBinding
import app.votings.android.databinding.FragmentVotingCreateChoicesBinding
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.GridSpacingItemDecoration
import appsquared.votings.app.VotingCreateActivity
import appsquared.votings.app.adapter.ChoicesListAdapter
import appsquared.votings.app.dpToPx
import appsquared.votings.app.views.InputDialog
import appsquared.votings.app.Constant
import appsquared.votings.app.rest.ApiService
import io.reactivex.disposables.Disposable

class VotingCreateChoicesFragment : Fragment() {

    var mChoices = mutableListOf<String>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mListener: FragmentInteractionListener? = null

    private lateinit var binding: FragmentVotingCreateChoicesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVotingCreateChoicesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mChoices = (activity as VotingCreateActivity).getChoices()

        binding.buttonCardViewVotingCreateChoicesNew.bindingButtonCardView.materialCardView.setOnClickListener {
            InputDialog(requireContext()) { button: Int, text: String ->
                if(button == InputDialog.LEFT) return@InputDialog
                if(button == InputDialog.RIGHT) {
                    if(text.isNotEmpty()) {
                        mChoices.add(text)
                        binding.recyclerViewVotingCreateChoices.adapter?.notifyItemInserted(mChoices.size - 1)
                        binding.textViewVotingCreateChoicesError.visibility = View.GONE
                        (activity as VotingCreateActivity).setChoices(mChoices)
                    }
                }
            }.generate(DialogInputBinding.inflate(layoutInflater))
                .setTitle(getString(R.string.voting_create_choice_dialog_title))
                .setMessage(getString(R.string.voting_create_choice_dialog_text))
                .setHint(getString(R.string.voting_create_choice_dialog_hint))
                .setButtonLeftName(getString(R.string.cancel)).setButtonRightName(getString(R.string.add))
                .show()
        }

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)
        binding.recyclerViewVotingCreateChoices.setPadding(0, spacing, 0, 0)
        binding.recyclerViewVotingCreateChoices.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                dpToPx(4),
                includeEdge
            )
        )
        binding.recyclerViewVotingCreateChoices.layoutManager = GridLayoutManager(context, spanCount)

        binding.recyclerViewVotingCreateChoices.adapter = ChoicesListAdapter(mChoices) { position: Int ->
            mChoices.removeAt(position)
            binding.recyclerViewVotingCreateChoices.adapter?.notifyItemRemoved(position)
            (activity as VotingCreateActivity).setChoices(mChoices)
        }

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewVotingCreateChoices)

        binding.buttonCardViewVotingCreateChoicesNext.bindingButtonCardView.materialCardView.setOnClickListener {
            //TODO show error when there are less than 2 choices added to list --> textViewVotingCreateChoicesError
            if(mChoices.size < 2) {
                binding.textViewVotingCreateChoicesError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

        binding.buttonCardViewVotingCreateChoicesPrevious.bindingButtonCardView.materialCardView.setOnClickListener {
            onButtonPressed(Constant.BACK)
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
        fun newInstance() = VotingCreateChoicesFragment()
    }

    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled() = true
        override fun isItemViewSwipeEnabled() = false

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = if (isItemViewSwipeEnabled) ItemTouchHelper.START or ItemTouchHelper.END else 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (viewHolder.itemViewType != target.itemViewType)
                return false
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            val item = mChoices.removeAt(fromPosition)
            mChoices.add(toPosition, item)
            (activity as VotingCreateActivity).setChoices(mChoices)
            recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            mChoices.removeAt(position)
            binding.recyclerViewVotingCreateChoices.adapter!!.notifyItemRemoved(position)
        }

    })
}