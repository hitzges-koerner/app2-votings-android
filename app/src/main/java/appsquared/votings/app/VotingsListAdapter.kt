package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_voting.view.*

class VotingsListAdapter(private val items: MutableList<Model.Voting>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<VotingsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voting, parent, false)
        return ViewHolder(view, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.Voting) {
            with(itemView) {

                textViewVotingTitle.text = item.votingTitle
                textViewVotingTitle.setTextColor(attributes.contentAccentColor)

                val votingStart = getLocalDateStyle(item.votingFrom, context)
                textViewVotingStart.text = votingStart
                textViewVotingStart.setTextColor(attributes.contentTextColor)

                val votingEnd = getLocalDateStyle(item.votingTill, context)
                textViewVotingEnd.text = votingEnd
                textViewVotingEnd.setTextColor(attributes.contentTextColor)

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                buttonCardViewVoting.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}