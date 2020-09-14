package appsquared.votings.app

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import appsquared.votings.app.VotingCustomItem.Companion.BUTTON
import appsquared.votings.app.VotingCustomItem.Companion.CHOICE
import appsquared.votings.app.VotingCustomItem.Companion.DOCUMENT
import appsquared.votings.app.VotingCustomItem.Companion.INFO
import appsquared.votings.app.VotingCustomItem.Companion.RESULT
import appsquared.votings.app.VotingCustomItem.Companion.SECTION
import appsquared.votings.app.VotingCustomItem.Companion.STREAM
import appsquared.votings.app.VotingCustomItem.Companion.USER
import kotlinx.android.synthetic.main.item_button.view.*
import kotlinx.android.synthetic.main.item_choice.view.*
import kotlinx.android.synthetic.main.item_document.view.*
import kotlinx.android.synthetic.main.item_info.view.*
import kotlinx.android.synthetic.main.item_result.view.*
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_stream.view.*
import kotlinx.android.synthetic.main.item_user_small.view.*
import java.text.DecimalFormat
import kotlin.math.roundToInt


class VotingsAdapter(private val items: MutableList<VotingCustomItem>, val attributes: Attributes, private val status: Int, private val voted: Boolean, private val listener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val PAYLOAD_NAME = "PAYLOAD_SELECTED"

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        viewHolder = when (viewType) {
            STREAM -> {
                val v1 = inflater.inflate(R.layout.item_stream, viewGroup, false)
                ViewHolderStream(v1, attributes, listener)
            }
            SECTION -> {
                val v2 = inflater.inflate(R.layout.item_section, viewGroup, false)
                ViewHolderSection(v2, attributes, listener)
            }
            DOCUMENT -> {
                val v2 = inflater.inflate(R.layout.item_document, viewGroup, false)
                ViewHolderDocument(v2, attributes, listener)
            }
            USER -> {
                val v2 = inflater.inflate(R.layout.item_user_small, viewGroup, false)
                ViewHolderUser(v2, attributes, listener)
            }
            CHOICE -> {
                val v2 = inflater.inflate(R.layout.item_choice, viewGroup, false)
                ViewHolderChoice(v2, attributes, status, voted, listener)
            }
            RESULT -> {
                val v2 = inflater.inflate(R.layout.item_result, viewGroup, false)
                ViewHolderResult(v2, attributes, listener)
            }
            INFO -> {
                val v2 = inflater.inflate(R.layout.item_info, viewGroup, false)
                ViewHolderInfo(v2, attributes, listener)
            }
            BUTTON -> {
                val v2 = inflater.inflate(R.layout.item_button, viewGroup, false)
                ViewHolderButton(v2, attributes, listener)
            }
            else -> {
                val v1 = inflater.inflate(R.layout.item_user_small, viewGroup, false)
                ViewHolderUser(v1, attributes, listener)
            }
        }
        return viewHolder
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

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
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

    class ViewHolderUser(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                materialCardViewUser.setBackgroundColor(attributes.contentBackgroundColor)
                constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
                // first user in list
                if(item.tag == 0) {
                    //materialCardViewUser.setBackgroundResource(R.drawable.background_round_corners_top)
                    customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "top")
                    constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(4))
                }
                // last user in list
                if(item.tag == 1) {
                    //materialCardViewUser.setBackgroundResource(R.drawable.background_round_corners_bottom)
                    customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "bottom")
                    constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(8))
                }
                // only one in list
                if(item.tag == 2) {
                    customView(materialCardViewUser, attributes.contentBackgroundColor, dpToPxFloat(10), "one")
                    constraintLayoutUserSmall.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
                }
                if(item.choiceId.isEmpty()) {
                    textViewUserVotedIndicator.text = "\u25EF"
                    textViewChoiceName.visibility = GONE
                } else {
                    textViewUserVotedIndicator.text = "\u29BF"
                    textViewChoiceName.visibility = VISIBLE
                }
                textViewUser.text = "${item.nameLast}, ${item.nameFirst}"
                textViewChoiceName.text = item.choiceName

                textViewUser.setTextColor(attributes.contentTextColor)
                textViewUserVotedIndicator.setTextColor(attributes.contentTextColor)
                textViewChoiceName.setTextColor(attributes.contentTextColor)

                if(item.visible)  constraintLayoutUserSmall.visibility = VISIBLE
                    else constraintLayoutUserSmall.visibility = GONE
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
                    shape.cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f)
                }
                "bottom" -> {
                    shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
                }
                "one" -> {
                    shape.cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
                }
            }
            shape.setColor(backgroundColor)
            v.background = shape
        }
    }

    class ViewHolderSection(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewSection.text = item.title
                textViewSection.setTextColor(ContextCompat.getColor(context, R.color.black))

                constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)

                if(item.visible) imageViewExpand.setImageResource(R.drawable.doppel_caret_down)
                else imageViewExpand.setImageResource(R.drawable.doppel_caret_up)

                itemView.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

    class ViewHolderStream(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewStream.setTextColor(attributes.contentTextColor)
                imageViewCaretStream.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                imageViewIconStream.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                materialCardViewStream.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

    class ViewHolderDocument(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewDocument.text = item.title
                textViewDocument.setTextColor(attributes.contentTextColor)
                imageViewCaretDocument.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                imageViewIconDocument.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                materialCardViewDocument.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

    class ViewHolderChoice(itemView: View, private val attributes: Attributes, val status: Int, val voted: Boolean, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                val df = DecimalFormat("#.#")
                textViewChoiceTitle.text = item.title
                textViewChoiceTitle.setTextColor(attributes.contentTextColor)
                if(item.count == 0) {
                    textViewChoicePercent.text = "0 %"
                    progressBarChoice.progress = 0
                } else {
                    textViewChoicePercent.text = "${df.format((item.count.toFloat() / item.total.toFloat()) * 100)} %"
                    progressBarChoice.progress = ((item.count.toFloat() / item.total.toFloat()) * 100).roundToInt()
                }

                imageViewChoiceChecked.setColorFilter(attributes.contentTextColor, PorterDuff.Mode.SRC_ATOP)
                if(item.selected) imageViewChoiceChecked.setImageResource(R.drawable.ic_round_checked)
                else imageViewChoiceChecked.setImageResource(R.drawable.ic_round_unchecked)

                when(status) {
                    VotingsListActivity.CURRENT -> {
                        if(!voted) {
                            materialCardViewChoice.setOnClickListener {
                                listener(layoutPosition)
                            }
                        } else {
                            materialCardViewChoice.isClickable = false
                            materialCardViewChoice.isFocusable = false
                        }
                    }
                    VotingsListActivity.PAST -> {
                        materialCardViewChoice.isClickable = false
                        materialCardViewChoice.isFocusable = false
                    }
                    VotingsListActivity.FUTURE -> {
                        materialCardViewChoice.isClickable = false
                        materialCardViewChoice.isFocusable = false

                        textViewChoicePercent.visibility = GONE
                        progressBarChoice.visibility = GONE
                        imageViewChoiceChecked.visibility = GONE
                    }
                }

                if(item.visible)  materialCardViewChoice.visibility = VISIBLE
                    else materialCardViewChoice.visibility = GONE
            }
        }
        fun bindItemsPayload(item: VotingCustomItem) {
            with(itemView) {
                if(item.selected) imageViewChoiceChecked.setImageResource(R.drawable.ic_round_checked)
                else imageViewChoiceChecked.setImageResource(R.drawable.ic_round_unchecked)
            }
        }
    }

    class ViewHolderResult(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewResultTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                textViewResultPercent.setTextColor(ContextCompat.getColor(context, R.color.black))
                textViewResultTitle.text = item.title
                if(item.count == 0) {
                    textViewResultPercent.text = "0,0% (0)"
                    //guidelineResult.setGuidelinePercent(0.5f)
                    textViewResultPercent.background.setLevel(0)
                } else {
                    val df = DecimalFormat("#.#")
                    textViewResultPercent.text = "${df.format((item.count.toFloat() / item.total.toFloat()) * 100)} % (${item.count})"
                    //guidelineResult.setGuidelinePercent((item.count.toFloat() / item.total.toFloat()))
                    guidelineResult.setGuidelinePercent(0.5f)

                    textViewResultPercent.background.setLevel((item.count.toFloat() / item.total.toFloat() * 10000).toInt())
                }
                if(item.visible)  linearLayoutResult.visibility = VISIBLE
                    else linearLayoutResult.visibility = GONE
            }
        }
    }

    class ViewHolderInfo(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewInfo.text = item.title
            }
        }
    }

    class ViewHolderButton(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: VotingCustomItem) {
            with(itemView) {
                textViewButton.text = context.getString(R.string.vote_now)
                textViewButton.setTextColor(attributes.contentTextColor)
                materialCardViewButton.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardViewButton.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }

}