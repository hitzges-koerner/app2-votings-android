package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListAdapter(private val items: MutableList<Model.News>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.News) {
            with(itemView) {

                textViewNewsTitle.text = item.title
                textViewNewsTitle.setTextColor(attributes.contentAccentColor)

                textViewNewsText.text = item.subTitle
                textViewNewsText.setTextColor(attributes.contentTextColor)

                val publishFrom = getLocalDateStyle(item.publishFrom, context)
                textViewNewsDateTime.text = publishFrom
                textViewNewsDateTime.setTextColor(attributes.contentTextColor)

                imageViewNewsItem.visibility = View.VISIBLE
                if(item.headerImageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(item.headerImageUrl)
                        .into(imageViewNewsItem)
                } else imageViewNewsItem.visibility = View.GONE

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                materialCardView.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}