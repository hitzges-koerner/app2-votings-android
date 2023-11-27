package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import app.votings.android.databinding.DialogVotingSelectBinding
import app.votings.android.databinding.LayoutToolbarDialogVotingSelectBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.GridSpacingItemDecoration
import appsquared.votings.app.adapter.VotingSelectListAdapter
import appsquared.votings.app.dpToPx
import appsquared.votings.app.rest.Model

class VotingSelectDialog(val context: Context, val attributes: Attributes, val listener: (Model.VotingShort) -> Unit) {

    val dialog = Dialog(context)
    private lateinit var binding: DialogVotingSelectBinding
    private lateinit var bindingToolbar: LayoutToolbarDialogVotingSelectBinding

    fun generate(binding: DialogVotingSelectBinding) : VotingSelectDialog {
        this@VotingSelectDialog.binding = binding
        bindingToolbar = binding.layoutToolbarDialogVotingSelect
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        bindingToolbar.imageDialogClose.setOnClickListener {
            dialog.dismiss()
        }

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        return this
    }

    fun setItems(list: MutableList<Model.VotingShort>) : VotingSelectDialog {
        var spanCount = 1
        val includeEdge = false
        val spacing = dpToPx(16)

        binding.recyclerViewDialogVotingSelect.setPadding(spacing, spacing, spacing, spacing)
        binding.recyclerViewDialogVotingSelect.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerViewDialogVotingSelect.layoutManager = GridLayoutManager(context, spanCount)
        binding.recyclerViewDialogVotingSelect.adapter = VotingSelectListAdapter(list, attributes) { position: Int ->
            listener(list[position])
            dialog.dismiss()
        }
        return this
    }

    fun show() {
        dialog.show()
    }
}