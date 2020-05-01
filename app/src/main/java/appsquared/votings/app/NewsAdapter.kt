package appsquared.votings.app

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(private val items: MutableList<Model.News>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
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

                textViewNewsHeadline.text = item.title
                textViewNewsHeadline.setTextColor(attributes.contentAccentColor)

                textViewNewsText.text = item.subTitle
                textViewNewsText.setTextColor(attributes.contentTextColor)

                textViewNewsDateTime.text = item.publishFrom
                textViewNewsDateTime.setTextColor(attributes.contentTextColor)

                imageViewNewsItem.visibility = View.VISIBLE
                if(item.headerImageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(item.headerImageUrl)
                        .into(imageViewNewsItem)
                } else imageViewNewsItem.visibility = View.GONE

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.tilesBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.tilesBorderWidth)
                materialCardView.radius = dpToPx(attributes.tilesCornerRadius).toFloat()

                setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}