package appsquared.votings.app

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_voting.view.*


class VotingsListAdapter(private val items: MutableList<Model.VotingShort>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SECTION = 0
    val VOTING = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        viewHolder = when (viewType) {
            VOTING -> {
                val v1 = inflater.inflate(R.layout.item_voting, viewGroup, false)
                ViewHolder(v1, attributes, listener)
            }
            SECTION -> {
                val v2 = inflater.inflate(R.layout.item_section_big, viewGroup, false)
                ViewHolderSection(v2, attributes, listener)
            }
            else -> {
                val v1 = inflater.inflate(R.layout.item_voting, viewGroup, false)
                ViewHolder(v1, attributes, listener)
            }
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        if(items[position].votingId.isNotEmpty()) return VOTING
        return SECTION
    }
    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            VOTING -> {
                val vh2 = viewHolder as ViewHolder
                vh2.bindItems(items[position])
            }
            SECTION -> {
                val vh2 = viewHolder as ViewHolderSection
                vh2.bindItems(items[position])
            }
        }
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.VotingShort) {
            with(itemView) {

                textViewInRepresentation.visibility = View.GONE
                if(item.inRepresentationOfId.isNotEmpty()) {
                    textViewInRepresentation.visibility = View.VISIBLE
                    textViewInRepresentation.setTextColor(ContextCompat.getColor(context, R.color.black))
                    textViewInRepresentation.text = "In Vertretung für: ${item.inRepresentationOfName}"
                }

                when(item.votingType.toUpperCase()) {
                    "OPEN" -> {
                        materialButtonVotingType.text = context.getString(R.string.open)
                        materialButtonVotingType.setTextColor(ContextCompat.getColor(context, R.color.white))
                        materialButtonVotingType.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    }

                    "SECRET" -> {
                        materialButtonVotingType.text = context.getString(R.string.secret)
                        materialButtonVotingType.setTextColor(ContextCompat.getColor(context, R.color.white))
                        materialButtonVotingType.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_190))
                    }
                }

                when(item.votingStatus) {
                    VotingsListActivity.FUTURE -> {
                        textViewButtonVote.text = context.getString(R.string.show_information)
                    }
                    VotingsListActivity.CURRENT -> {
                        if(item.isVoted == "1") textViewButtonVote.text = context.getString(R.string.show_preliminary_result)
                        else textViewButtonVote.text = context.getString(R.string.vote_now)
                    }
                    VotingsListActivity.PAST -> {
                        textViewButtonVote.text = context.getString(R.string.show_result)
                    }
                    VotingsListActivity.UNDEFINED -> {
                        textViewButtonVote.text = context.getString(R.string.error_general)
                    }
                }

                textViewButtonVote.setTextColor(attributes.contentBackgroundColor)
                materialCardViewButtonVote.setCardBackgroundColor(attributes.contentTextColor)
                materialCardViewButtonVote.strokeColor = attributes.contentTextColor
                materialCardViewButtonVote.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardViewButtonVote.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                textViewVotingTitle.text = item.votingTitle
                textViewVotingTitle.setTextColor(attributes.contentTextColor)

                val votingStart = getLocalDateStyle(item.votingFrom, context)
                textViewVotingStartTime.text = votingStart
                textViewVotingStartTime.setTextColor(attributes.contentTextColor)

                val votingEnd = getLocalDateStyle(item.votingTill, context)
                textViewVotingEndTime.text = votingEnd
                textViewVotingEndTime.setTextColor(attributes.contentTextColor)

                textViewVotingStart.text = context.getString(R.string.start)
                textViewVotingStart.setTextColor(attributes.contentTextColor)

                textViewVotingEnd.text = context.getString(R.string.end)
                textViewVotingEnd.setTextColor(attributes.contentTextColor)

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                materialCardViewButtonVote.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }

        private fun generateStrokeColor(context: Context, color: Int): ColorStateList {
            return ColorStateList.valueOf(color)
        }
    }

    class ViewHolderSection(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.VotingShort) {
            with(itemView) {
                textViewSection.text = item.votingTitle
                textViewSection.setTextColor(ContextCompat.getColor(context, R.color.black))

                constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)
            }
        }
    }
}