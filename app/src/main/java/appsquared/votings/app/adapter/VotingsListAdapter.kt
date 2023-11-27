package appsquared.votings.app.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemSectionBinding
import app.votings.android.databinding.ItemVotingBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.PreferenceNames
import appsquared.votings.app.VotingsListActivity
import appsquared.votings.app.getLocalDateStyle
import appsquared.votings.app.rest.Model


class VotingsListAdapter(
    private val items: MutableList<Model.VotingShort>,
    val attributes: Attributes,
    private val listener: (Int, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SECTION = 0
    val VOTING = 1

    var mEditMode = false

    fun setEditMode(editMode: Boolean) {
        mEditMode = editMode
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType) {
            VOTING -> {
                val binding = ItemVotingBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                ViewHolder(binding, attributes, listener)
            }

            SECTION -> {
                val binding = ItemSectionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                ViewHolderSection(binding, attributes, listener)
            }

            else -> {
                val binding = ItemVotingBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
                ViewHolder(binding, attributes, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].votingId.isNotEmpty()) return VOTING
        return SECTION
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            VOTING -> {
                val vh2 = viewHolder as ViewHolder
                vh2.bindItems(items[position], mEditMode)
            }

            SECTION -> {
                val vh2 = viewHolder as ViewHolderSection
                vh2.bindItems(items[position])
            }
        }
    }

    class ViewHolder(
        private val binding: ItemVotingBinding,
        private val attributes: Attributes,
        val listener: (Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(item: Model.VotingShort, editMode: Boolean) {
            with(itemView) {
                if (editMode) binding.imageViewEditVotingList.visibility = VISIBLE
                else binding.imageViewEditVotingList.visibility = GONE

                val pref = PreferenceManager.getDefaultSharedPreferences(context)
                if ((item.isQuickVoting == "1" && item.ownerId == pref.getString(
                        PreferenceNames.USERID,
                        ""
                    )) || item.votingStatus == VotingsListActivity.PAST
                ) {
                    binding.imageViewEditVotingList.setBackgroundResource(R.drawable.background_round)
                    binding.imageViewEditVotingList.setOnClickListener {
                        listener(EDIT_BUTTON, layoutPosition)
                    }
                } else {
                    binding.imageViewEditVotingList.setBackgroundResource(R.drawable.background_round_grey)
                    binding.imageViewEditVotingList.setOnClickListener {
                        Toast.makeText(context, "no permissions to edit voting", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                binding.textViewInRepresentation.visibility = GONE
                if (item.inRepresentationOfId.isNotEmpty()) {
                    binding.textViewInRepresentation.visibility = VISIBLE
                    binding.textViewInRepresentation.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                    binding.textViewInRepresentation.text =
                        context.getString(R.string.in_representation_for) + item.inRepresentationOfName
                }

                when (item.votingType.toUpperCase()) {
                    "OPEN" -> {
                        binding.materialButtonVotingType.text = context.getString(R.string.open)
                        binding.materialButtonVotingType.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        binding.materialButtonVotingType.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            )
                        )
                    }

                    "SECRET" -> {
                        binding.materialButtonVotingType.text = context.getString(R.string.secret)
                        binding.materialButtonVotingType.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        binding.materialButtonVotingType.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.grey_190
                            )
                        )
                    }
                }

                when (item.votingStatus) {
                    VotingsListActivity.FUTURE -> {
                        binding.textViewButtonVote.text =
                            context.getString(R.string.show_information)
                    }

                    VotingsListActivity.CURRENT -> {
                        if (item.isVoted == "1") binding.textViewButtonVote.text =
                            context.getString(R.string.show_preliminary_result)
                        else binding.textViewButtonVote.text = context.getString(R.string.vote_now)
                    }

                    VotingsListActivity.PAST -> {
                        binding.textViewButtonVote.text = context.getString(R.string.show_result)
                    }

                    VotingsListActivity.UNDEFINED -> {
                        binding.textViewButtonVote.text = context.getString(R.string.error_general)
                    }
                }

                //textViewButtonVote.setTextColor(attributes.contentBackgroundColor)
                //materialCardViewButtonVote.setCardBackgroundColor(attributes.contentTextColor)
                //materialCardViewButtonVote.strokeColor = attributes.contentTextColor
                //materialCardViewButtonVote.strokeWidth = dpToPx(attributes.contentBorderWidth)
                //materialCardViewButtonVote.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                binding.textViewVotingTitle.text = item.votingTitle
                //textViewVotingTitle.setTextColor(attributes.contentTextColor)

                val votingStart = getLocalDateStyle(item.votingFrom, context)
                binding.textViewVotingStartTime.text = votingStart
                //textViewVotingStartTime.setTextColor(attributes.contentTextColor)

                val votingEnd = getLocalDateStyle(item.votingTill, context)
                binding.textViewVotingEndTime.text = " - $votingEnd"
                //textViewVotingEndTime.setTextColor(attributes.contentTextColor)

                binding.textViewVotingStart.text = context.getString(R.string.start)
                //textViewVotingStart.setTextColor(attributes.contentTextColor)

                binding.textViewVotingEnd.text = context.getString(R.string.end)
                //textViewVotingEnd.setTextColor(attributes.contentTextColor)

                //materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                //materialCardView.strokeColor = attributes.contentBorderColor
                //materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                //materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                binding.materialCardViewButtonVote.setOnClickListener {
                    listener(VOTING_BUTTON, layoutPosition)
                }
            }
        }

        private fun generateStrokeColor(context: Context, color: Int): ColorStateList {
            return ColorStateList.valueOf(color)
        }
    }

    class ViewHolderSection(
        private val binding: ItemSectionBinding,
        private val attributes: Attributes,
        val listener: (Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.VotingShort) {
            with(itemView) {
                binding.textViewSection.text = item.votingTitle
                binding.textViewSection.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }


    companion object {
        val VOTING_BUTTON = 0
        val EDIT_BUTTON = 1
    }
}