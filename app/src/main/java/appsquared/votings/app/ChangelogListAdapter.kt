package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_changelog.view.*

class ChangelogListAdapter(private val items: MutableList<Model.Changelog>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<ChangelogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangelogListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_changelog, parent, false)
        return ViewHolder(view, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.Changelog) {
            with(itemView) {

                textViewChangelogTitle.text = item.version
                textViewChangelogTitle.setTextColor(attributes.contentAccentColor)

                textViewChangelogText.text = item.releaseNotes
                textViewChangelogText.setTextColor(attributes.contentTextColor)

                val releaseDate = getLocalDateStyle(item.releaseDate, context)
                textViewChangelogDateTime.text = releaseDate
                textViewChangelogDateTime.setTextColor(attributes.contentTextColor)

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()
            }
        }
    }
}