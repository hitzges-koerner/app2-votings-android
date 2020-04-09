package appsquared.votings.app

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_card_view.view.*
import kotlinx.android.synthetic.main.item_main_tiles.view.*
import kotlinx.android.synthetic.main.item_main_tiles.view.materialCardView

class TilesAdapter(private val items: MutableList<Item>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<TilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TilesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_tiles, parent, false)
        return ViewHolder(view, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Item) {
            with(itemView) {

                textViewLabel.text = item.text
                textViewLabel.setTextColor(ResourcesCompat.getColor(resources, attributes.textColor, null))

                imageViewIcon.setImageResource(item.iconId)
                imageViewIcon.setColorFilter(ResourcesCompat.getColor(resources, attributes.highlightColor, null), PorterDuff.Mode.SRC_ATOP)

                materialCardView.setCardBackgroundColor(ResourcesCompat.getColor(resources, attributes.tilesBackgroundColor, null))
                materialCardView.strokeColor = ResourcesCompat.getColor(resources, attributes.tilesBorderColor, null)
                materialCardView.strokeWidth = dpToPx(attributes.tilesBorderWidth)

                /*
                if(item.icon.isEmpty()) {
                    textViewTile.text = item.iconText
                    textViewTile.visibility = View.VISIBLE
                    imageViewTile.visibility = View.INVISIBLE
                } else {
                    imageViewTile.setImageResource(item.iconId)
                    imageViewTile.visibility = View.VISIBLE
                    textViewTile.visibility = View.GONE
                }

                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE

                if(attributes.colorize) {
                    var backgroundColor = attributes.disabledBackgroundColor
                    if (item.viewType.isNullOrEmpty()) {
                        imageViewTile.setColorFilter(attributes.disabledTintColor)
                        textViewTile.setTextColor(attributes.disabledTintColor)
                        //constraintLayoutBackground.setBackgroundColor(attributes.disabledBackgroundColor)
                        backgroundColor = attributes.disabledBackgroundColor
                    } else {
                        imageViewTile.setColorFilter(attributes.activeTintColor)
                        textViewTile.setTextColor(attributes.activeTintColor)
                        //constraintLayoutBackground.setBackgroundColor(attributes.activeBackgroundColor)
                        backgroundColor = attributes.activeBackgroundColor
                    }
                    shape.setColor(backgroundColor)
                } else shape.setColor(ResourcesCompat.getColor(resources, R.color.transparent, null))

                shape.setStroke(attributes.borderWidth, attributes.borderColor)
                shape.cornerRadius = attributes.cornerRadius.toFloat()

                constraintLayoutBackground.background = shape


                //textViewTile.text = item.iconText
                //textViewTile.setTextColor(attributes.itemSelected)

                //linearLayoutBackground.setBackgroundColor(attributes.backgroundColor)
                 */

                setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}