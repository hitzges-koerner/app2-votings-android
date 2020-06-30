package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import appsquared.votings.app.*
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_votings.*
import kotlinx.android.synthetic.main.dialog_list.*
import kotlinx.android.synthetic.main.dialog_voting_select.*
import kotlinx.android.synthetic.main.fragment_notificationlist.*
import kotlinx.android.synthetic.main.layout_toolbar_dialog_voting_select.*
import org.w3c.dom.Attr

class VotingSelectDialog(val context: Context, val attributes: Attributes, val listener: (Model.VotingShort) -> Unit) {

    val dialog = Dialog(context)

    fun generate() : VotingSelectDialog {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_voting_select)

        dialog.imageDialogClose.setOnClickListener {
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

        dialog.recyclerViewDialogVotingSelect.setPadding(spacing, spacing, spacing, spacing)
        dialog.recyclerViewDialogVotingSelect.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        dialog.recyclerViewDialogVotingSelect.layoutManager = GridLayoutManager(context, spanCount)
        dialog.recyclerViewDialogVotingSelect.adapter = VotingSelectListAdapter(list, attributes) { position: Int ->
            listener(list[position])
            dialog.dismiss()
        }
        return this
    }

    fun show() {
        dialog.show()
    }
}