package appsquared.votings.app

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_voting_select.view.*
import kotlinx.android.synthetic.main.settings_impress_card_view.*

class VotingSelectListAdapter(private val items: MutableList<Model.VotingShort>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<VotingSelectListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingSelectListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voting_select, parent, false)
        return ViewHolder(view, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.VotingShort) {
            with(itemView) {

                val white = ContextCompat.getColor(context, R.color.white)
                imageViewItemVotingSelect.setColorFilter(attributes.contentBackgroundColor, PorterDuff.Mode.SRC_ATOP)

                if(item.inRepresentationOfId.isEmpty() && item.inRepresentationOfName.isEmpty()) {
                    textViewItemVotingSelect.text = "für mich selbst"
                } else textViewItemVotingSelect.text = item.inRepresentationOfName

                textViewItemVotingSelect.setTextColor(attributes.contentBackgroundColor)

                materialCardViewItemVotingSelect.setCardBackgroundColor(white)
                materialCardViewItemVotingSelect.strokeColor = attributes.contentBackgroundColor
                materialCardViewItemVotingSelect.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}