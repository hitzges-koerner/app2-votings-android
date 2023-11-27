package appsquared.votings.app.adapter

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemButtonBinding
import app.votings.android.databinding.ItemChoiceBinding
import app.votings.android.databinding.ItemDocumentBinding
import app.votings.android.databinding.ItemInfoBinding
import app.votings.android.databinding.ItemResultBinding
import app.votings.android.databinding.ItemSectionBinding
import app.votings.android.databinding.ItemStreamBinding
import app.votings.android.databinding.ItemUserSmallBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.VotingCustomItem
import appsquared.votings.app.VotingCustomItem.Companion.BUTTON
import appsquared.votings.app.VotingCustomItem.Companion.CHOICE
import appsquared.votings.app.VotingCustomItem.Companion.DOCUMENT
import appsquared.votings.app.VotingCustomItem.Companion.INFO
import appsquared.votings.app.VotingCustomItem.Companion.RESULT
import appsquared.votings.app.VotingCustomItem.Companion.SECTION
import appsquared.votings.app.VotingCustomItem.Companion.STREAM
import appsquared.votings.app.VotingCustomItem.Companion.USER
import appsquared.votings.app.VotingsListActivity
import appsquared.votings.app.dpToPx
import java.text.DecimalFormat
import kotlin.math.roundToInt


class VotingsAdapter(
    private val items: MutableList<VotingCustomItem>,
    val attributes: Attributes,
    private val status: Int,
    private val voted: Boolean,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val PAYLOAD_NAME = "PAYLOAD_SELECTED"

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            STREAM -> {
                val binding = ItemStreamBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderStream(binding, attributes, listener)
            }
            SECTION -> {
                val binding = ItemSectionBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderSection(binding, attributes, listener)
            }
            DOCUMENT -> {
                val binding = ItemDocumentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderDocument(binding, attributes, listener)
            }
            USER -> {
                val binding = ItemUserSmallBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderUser(binding, attributes, listener)
            }
            CHOICE -> {
                val binding = ItemChoiceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderChoice(binding, attributes, status, voted, listener)
            }
            RESULT -> {
                val binding = ItemResultBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderResult(binding, attributes, listener)
            }
            INFO -> {
                val binding = ItemInfoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderInfo(binding, attributes, listener)
            }
            BUTTON -> {
                val binding = ItemButtonBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderButton(binding, attributes, listener)
            }
            else -> {
                val binding = ItemUserSmallBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderUser(binding, attributes, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            STREAM -> {
                val vh1 = viewHolder as ViewHolderStream
                vh1.bindItems(items[position])
            }
            DOCUMENT -> {
                val vh1 = viewHolder as ViewHolderDocument
                vh1.bindItems(items[position])
            }
            SECTION -> {
                val vh2 = viewHolder as ViewHolderSection
                vh2.bindItems(items[position])
            }
            USER -> {
                val vh2 = viewHolder as ViewHolderUser
                vh2.bindItems(items[position])
            }
            CHOICE -> {
                val vh2 = viewHolder as ViewHolderChoice
                vh2.bindItems(items[position])
            }
            RESULT -> {
                val vh2 = viewHolder as ViewHolderResult
                vh2.bindItems(items[position])
            }
            INFO -> {
                val vh2 = viewHolder as ViewHolderInfo
                vh2.bindItems(items[position])
            }
            BUTTON -> {
                val vh2 = viewHolder as ViewHolderButton
                vh2.bindItems(items[position])
            }
        }
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if(payloads.isEmpty()) onBindViewHolder(viewHolder, position)
        else {
            when (viewHolder.itemViewType) {
                CHOICE -> {
                    val vh2 = viewHolder as ViewHolderChoice
                    vh2.bindItemsPayload(items[position])
                }
            }
        }
    }

    class ViewHolderUser(
        private val binding: ItemUserSmallBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                //materialCardViewUser.setBackgroundColor(attributes.contentBackgroundColor)
                binding.constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
                // first user in list
                if(item.tag == 0) {
                    //materialCardViewUser.setBackgroundResource(R.drawable.background_round_corners_top)
                    //customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "top")
                    binding.constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(4))
                }
                // last user in list
                if(item.tag == 1) {
                    //materialCardViewUser.setBackgroundResource(R.drawable.background_round_corners_bottom)
                    //customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "bottom")
                    binding.constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(8))
                }
                // only one in list
                if(item.tag == 2) {
                    //customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "one")
                    binding.constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
                }
                if(item.choiceId.isEmpty()) {
                    binding.textViewUserVotedIndicator.text = "\u25EF"
                    binding.textViewChoiceName.visibility = GONE
                } else {
                    binding.textViewUserVotedIndicator.text = "\u29BF"
                    binding.textViewChoiceName.visibility = VISIBLE
                }
                binding.textViewUser.text = "${item.nameLast}, ${item.nameFirst}"
                binding.textViewChoiceName.text = item.choiceName

                //textViewUser.setTextColor(attributes.contentTextColor)
                //textViewUserVotedIndicator.setTextColor(attributes.contentTextColor)
                binding.textViewUserVotedIndicator.setTextColor(ContextCompat.getColor(context,
                    R.color.black))
                //textViewChoiceName.setTextColor(attributes.contentTextColor)

                if(item.visible)  binding.constraintLayoutUserSmall.visibility = VISIBLE
                    else binding.constraintLayoutUserSmall.visibility = GONE
            }
        }

        fun customView(
            v: View,
            backgroundColor: Int,
            cornerRadius: Float,
            side: String
        ) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            when(side) {
                "top" -> {
                    shape.cornerRadii = floatArrayOf(cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        0f,
                        0f,
                        0f,
                        0f)
                }
                "bottom" -> {
                    shape.cornerRadii = floatArrayOf(0f,
                        0f,
                        0f,
                        0f,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius)
                }
                "one" -> {
                    shape.cornerRadii = floatArrayOf(cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius,
                        cornerRadius)
                }
            }
            shape.setColor(backgroundColor)
            v.background = shape
        }
    }

    class ViewHolderSection(
        private val binding: ItemSectionBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewSection.text = item.title
                binding.textViewSection.setTextColor(ContextCompat.getColor(context, R.color.black))

                //constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)

                //if(item.visible) imageViewExpand.setImageResource(R.drawable.doppel_caret_down)
                //else imageViewExpand.setImageResource(R.drawable.doppel_caret_up)

                //itemView.setOnClickListener {
                //    listener(layoutPosition)
                //}
            }
        }
    }

    class ViewHolderStream(
        private val binding: ItemStreamBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewStream.setTextColor(attributes.contentTextColor)
                binding.imageViewCaretStream.setColorFilter(attributes.contentTextColor,
                    PorterDuff.Mode.SRC_ATOP)
                binding.imageViewIconStream.setColorFilter(attributes.contentTextColor,
                    PorterDuff.Mode.SRC_ATOP)
                binding.materialCardViewStream.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

    class ViewHolderDocument(
        private val binding: ItemDocumentBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewDocument.text = item.title
                binding.textViewDocument.setTextColor(attributes.contentTextColor)
                binding.imageViewCaretDocument.setColorFilter(attributes.contentTextColor,
                    PorterDuff.Mode.SRC_ATOP)
                binding.imageViewIconDocument.setColorFilter(attributes.contentTextColor,
                    PorterDuff.Mode.SRC_ATOP)
                binding.materialCardViewDocument.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

    class ViewHolderChoice(
        private val binding: ItemChoiceBinding,
        private val attributes: Attributes,
        val status: Int,
        val voted: Boolean,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                val df = DecimalFormat("#.#")
                binding.textViewChoiceTitle.text = item.title
                //textViewChoiceTitle.setTextColor(attributes.contentTextColor)
                if(item.count == 0) {
                    binding.textViewChoicePercent.text = "0 %"
                    binding.progressBarChoice.progress = 0
                } else {
                    binding.textViewChoicePercent.text = "${df.format((item.count.toFloat() / item.total.toFloat()) * 100)} %"
                    binding.progressBarChoice.progress = ((item.count.toFloat() / item.total.toFloat()) * 100).roundToInt()
                }

                //imageViewChoiceChecked.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                if(item.selected) binding.imageViewChoiceChecked.setImageResource(R.drawable.ic_round_checked)
                else binding.imageViewChoiceChecked.setImageResource(R.drawable.ic_round_unchecked)

                when(status) {
                    VotingsListActivity.CURRENT -> {
                        if (!voted) {
                            binding.materialCardViewChoice.setOnClickListener {
                                listener(layoutPosition)
                            }
                        } else {
                            binding.materialCardViewChoice.isClickable = false
                            binding.materialCardViewChoice.isFocusable = false
                        }
                    }
                    VotingsListActivity.PAST -> {
                        binding.materialCardViewChoice.isClickable = false
                        binding.materialCardViewChoice.isFocusable = false
                    }
                    VotingsListActivity.FUTURE -> {
                        binding.materialCardViewChoice.isClickable = false
                        binding.materialCardViewChoice.isFocusable = false

                        binding.textViewChoicePercent.visibility = GONE
                        binding.progressBarChoice.visibility = GONE
                        binding.imageViewChoiceChecked.visibility = GONE
                    }
                }

                if(item.visible)  binding.materialCardViewChoice.visibility = VISIBLE
                    else binding.materialCardViewChoice.visibility = GONE
            }
        }
        fun bindItemsPayload(item: VotingCustomItem) {
            with(binding.root) {
                if(item.selected) binding.imageViewChoiceChecked.setImageResource(R.drawable.ic_round_checked)
                else binding.imageViewChoiceChecked.setImageResource(R.drawable.ic_round_unchecked)
            }
        }
    }

    class ViewHolderResult(
        private val binding: ItemResultBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewResultTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                binding.textViewResultPercent.setTextColor(ContextCompat.getColor(context, R.color.black))
                binding.textViewResultTitle.text = item.title

                if(item.best) {
                    binding.textViewResultPercent.background = ContextCompat.getDrawable(context,
                        R.drawable.bkgd_level)
                    binding.materialCardViewItemVotingSelect.strokeColor = ContextCompat.getColor(context,
                        R.color.colorPrimary)
                } else {
                    //textViewResultPercent.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    binding.textViewResultPercent.background = ContextCompat.getDrawable(context,
                        R.drawable.bkgd_level_grey)
                    binding.materialCardViewItemVotingSelect.strokeColor = ContextCompat.getColor(context,
                        R.color.grey_144)
                }

                if(item.count == 0) {
                    binding.textViewResultPercent.text = "0,0% (0)"
                    //guidelineResult.setGuidelinePercent(0.5f)
                    binding.textViewResultPercent.background.level = 0
                } else {
                    val df = DecimalFormat("#.#")
                    binding.textViewResultPercent.text = "${df.format((item.count.toFloat() / item.total.toFloat()) * 100)} % (${item.count})"
                    //guidelineResult.setGuidelinePercent((item.count.toFloat() / item.total.toFloat()))
                    binding.guidelineResult.setGuidelinePercent(0.5f)

                    binding.textViewResultPercent.background.level = (item.count.toFloat() / item.total.toFloat() * 10000).toInt()
                }
                if(item.visible)  binding.linearLayoutResult.visibility = VISIBLE
                    else binding.linearLayoutResult.visibility = GONE
            }
        }
    }

    class ViewHolderInfo(
        private val binding: ItemInfoBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        var infoTextExpanded = false

        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewInfo.text = item.title

                binding.textViewInfo.post {
                    val lineCount: Int = binding.textViewInfo.lineCount
                    if (lineCount > 3) {
                        binding.textViewInfoMore.visibility = View.VISIBLE
                        if (infoTextExpanded) {
                            binding.textViewInfo.maxLines = Integer.MAX_VALUE
                            binding.textViewInfoMore.setText(context.getString(R.string.show_less))
                        } else {
                            binding.textViewInfo.maxLines = 3
                            binding.textViewInfo.ellipsize = TextUtils.TruncateAt.END
                            binding.textViewInfoMore.setText(context.getString(R.string.show_more))
                        }

                        binding.textViewInfoMore.setOnClickListener {
                            infoTextExpanded = !infoTextExpanded
                            if (infoTextExpanded) {
                                binding.textViewInfo.maxLines = Integer.MAX_VALUE
                                binding.textViewInfoMore.setText(context.getString(R.string.show_less))
                            } else {
                                binding.textViewInfo.maxLines = 3
                                binding.textViewInfoMore.setText(context.getString(R.string.show_more))
                            }
                        }
                    } else binding.textViewInfoMore.visibility = GONE
                }
            }
        }
    }

    class ViewHolderButton(
        private val binding: ItemButtonBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: VotingCustomItem) {
            with(binding.root) {
                binding.textViewButton.text = context.getString(R.string.vote_now)
                //textViewButton.setTextColor(attributes.contentTextColor)
                //materialCardViewButton.setCardBackgroundColor(attributes.contentBackgroundColor)
                binding.materialCardViewButton.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

}