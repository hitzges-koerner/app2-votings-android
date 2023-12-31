package appsquared.votings.app

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
                textViewLabel.setTextColor(attributes.tilesTextColor)

                imageViewIcon.setImageResource(item.iconId)
                imageViewIcon.setColorFilter(attributes.tilesIconTintColor, PorterDuff.Mode.SRC_ATOP)
                materialCardViewIcon.setCardBackgroundColor(attributes.tilesIconBackgroundColor)

                materialCardView.setCardBackgroundColor(attributes.tilesBackgroundColor)
                materialCardView.strokeColor = attributes.tilesBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.tilesBorderWidth)
                materialCardView.radius = dpToPx(attributes.tilesCornerRadius).toFloat()

                materialCardViewIcon.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        materialCardViewIcon.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        val materialCardViewIconWidth = materialCardViewIcon.width //width is ready
                        //val radius = materialCardViewIconWidth.toFloat() / 100 * attributes.tilesIconCornerRadius.toFloat()
                        val radius = materialCardViewIconWidth/2

                        //materialCardViewIcon.radius = dpToPx(radius.toInt()).toFloat()
                        materialCardViewIcon.radius = radius.toFloat()
                    }
                })

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