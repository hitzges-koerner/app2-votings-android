package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.databinding.ItemNewsBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.getLocalDateStyle
import com.squareup.picasso.Picasso
import appsquared.votings.app.rest.Model

class NewsListAdapter(private val items: MutableList<Model.News>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemNewsBinding, private val attributes: Attributes, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.News) {
            with(itemView) {

                binding.textViewNewsTitle.text = item.title
                //textViewNewsTitle.setTextColor(attributes.contentAccentColor)

                binding.textViewNewsText.text = item.subTitle
                //textViewNewsText.setTextColor(attributes.contentTextColor)

                val publishFrom = getLocalDateStyle(item.publishFrom, context)
                binding.textViewNewsDateTime.text = publishFrom
                //textViewNewsDateTime.setTextColor(attributes.contentTextColor)

                binding.imageViewNewsItem.visibility = View.VISIBLE
                if(item.headerImageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(item.headerImageUrl)
                        .into(binding.imageViewNewsItem)
                } else binding.imageViewNewsItem.visibility = View.GONE

                //materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                //materialCardView.strokeColor = attributes.contentBorderColor
                //materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                //materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                binding.materialCardView.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}