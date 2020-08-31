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
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import appsquared.votings.app.*
import appsquared.votings.app.views.DecisionDialog
import appsquared.votings.app.views.InputDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_changelog.*
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.fragment_voting_create_choices.*
import kotlinx.android.synthetic.main.text_card_view.view.*
import kotlinx.android.synthetic.main.text_card_view.view.materialCardView

class VotingCreateChoicesFragment : Fragment() {

    var mChoices = mutableListOf<String>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mListener: FragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting_create_choices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mChoices = (activity as VotingCreateActivity).getChoices()

        buttonCardViewVotingCreateChoicesNew.materialCardView.setOnClickListener {
            InputDialog(requireContext()) { button: Int, text: String ->
                if(button == InputDialog.LEFT) return@InputDialog
                if(button == InputDialog.RIGHT) {
                    if(text.isNotEmpty()) {
                        mChoices.add(text)
                        recyclerViewVotingCreateChoices.adapter?.notifyItemInserted(mChoices.size - 1)
                        textViewVotingCreateChoicesError.visibility = View.GONE
                        (activity as VotingCreateActivity).setChoices(mChoices)
                    }
                }
            }.generate()
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
        recyclerViewVotingCreateChoices.setPadding(0, spacing, 0, 0)
        recyclerViewVotingCreateChoices.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                dpToPx(4),
                includeEdge
            )
        )
        recyclerViewVotingCreateChoices.layoutManager = GridLayoutManager(context, spanCount)

        recyclerViewVotingCreateChoices.adapter = ChoicesListAdapter(mChoices) { position: Int ->
            mChoices.removeAt(position)
            recyclerViewVotingCreateChoices.adapter?.notifyItemRemoved(position)
            (activity as VotingCreateActivity).setChoices(mChoices)
        }

        itemTouchHelper.attachToRecyclerView(recyclerViewVotingCreateChoices)

        buttonCardViewVotingCreateChoicesNext.materialCardView.setOnClickListener {
            //TODO show error when there are less than 2 choices added to list --> textViewVotingCreateChoicesError
            if(mChoices.size < 2) {
                textViewVotingCreateChoicesError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

        buttonCardViewVotingCreateChoicesPrevious.materialCardView.setOnClickListener {
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
            recyclerView.adapter!!.notifyItemRemoved(position)
        }

    })
}