package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemFaqContentBinding
import app.votings.android.databinding.ItemFaqSectionBinding
import appsquared.votings.app.HeaderItemDecoration
import appsquared.votings.app.rest.Model


class FaqListAdapter(private val items: MutableList<Model.Faq>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    HeaderItemDecoration.StickyHeaderInterface {

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPositionTemp = itemPosition
        var headerPosition = 0
        do {
            if (this.isHeader(itemPositionTemp)) {
                headerPosition = itemPositionTemp
                break
            }
            itemPositionTemp -= 1
        } while (itemPositionTemp >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.item_faq_section
    }

    override fun bindHeaderData(header: ItemFaqSectionBinding, headerPosition: Int) {
        header.textViewFaqSection.text = items[headerPosition].topic
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return items[itemPosition].section
    }

    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_content, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.Faq) {
            with(itemView) {
                textViewFaqContentHeader.text = item.question
                textViewFaqContentText.text = item.answer

                itemView.setOnClickListener {
                    if(textViewFaqContentText.visibility == View.GONE) textViewFaqContentText.visibility = View.VISIBLE
                    else textViewFaqContentText.visibility = View.GONE
                }
            }
        }
    }
     */

    private val SECTION = 0
    private val CONTENT = 1

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        if (items[position].section) {
            return SECTION
        }
        return CONTENT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SECTION -> {
                val binding = ItemFaqSectionBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderSection(binding)
            }
            CONTENT -> {
                val binding = ItemFaqContentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderContent(binding)
            }
            else -> {
                val binding = ItemFaqContentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderContent(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            SECTION -> {
                val vh1 = viewHolder as ViewHolderSection
                vh1.bindItems(items[position])
            }
            CONTENT -> {
                val vh2 = viewHolder as ViewHolderContent
                vh2.bindItems(items[position])
            }
        }
    }

    class ViewHolderSection(private val binding: ItemFaqSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.Faq) {
            with(itemView) {
                binding.textViewFaqSection.text = item.topic
            }
        }
    }

    class ViewHolderContent(private val binding: ItemFaqContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.Faq) {
            with(itemView) {
                binding.textViewFaqContentHeader.text = item.question
                binding.textViewFaqContentText.text = item.answer

                itemView.setOnClickListener {
                    if(binding.textViewFaqContentText.visibility == View.GONE) expand(binding.textViewFaqContentText)
                    else collapse(binding.textViewFaqContentText)
                    //if(textViewFaqContentText.visibility == View.GONE) textViewFaqContentText.visibility = View.VISIBLE
                    //else textViewFaqContentText.visibility = View.GONE
                }
            }
        }

        fun expand(v: View) {
            val matchParentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight = v.measuredHeight

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.layoutParams.height = 1
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Expansion speed of 1dp/ms
            //a.duration = ((targetHeight / v.context.resources.displayMetrics.density).toLong())
            a.duration = 100
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Collapse speed of 1dp/ms
            //a.duration = ((initialHeight / v.context.resources.displayMetrics.density).toLong())
            a.duration = 100
            v.startAnimation(a)
        }
    }
}