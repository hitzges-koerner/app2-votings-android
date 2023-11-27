package appsquared.votings.app.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.databinding.ItemMainTilesBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.Item
import appsquared.votings.app.dpToPx

class TilesAdapter(private val items: MutableList<Item>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<TilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainTilesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemMainTilesBinding, private val attributes: Attributes, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Item) {
            with(itemView) {

                binding.textViewLabel.text = item.text
                binding.textViewLabel.setTextColor(attributes.tilesTextColor)

                binding.imageViewIcon.setImageResource(item.iconId)
                binding.imageViewIcon.setColorFilter(attributes.tilesIconTintColor, PorterDuff.Mode.SRC_ATOP)
                binding.materialCardViewIcon.setCardBackgroundColor(attributes.tilesIconBackgroundColor)

                binding.materialCardView.setCardBackgroundColor(attributes.tilesBackgroundColor)
                binding.materialCardView.strokeColor = attributes.tilesBorderColor
                binding.materialCardView.strokeWidth = dpToPx(attributes.tilesBorderWidth)
                binding.materialCardView.radius = dpToPx(attributes.tilesCornerRadius).toFloat()

                binding.materialCardViewIcon.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.materialCardViewIcon.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        val materialCardViewIconWidth = binding.materialCardViewIcon.width //width is ready
                        //val radius = materialCardViewIconWidth.toFloat() / 100 * attributes.tilesIconCornerRadius.toFloat()
                        val radius = materialCardViewIconWidth/2

                        //materialCardViewIcon.radius = dpToPx(radius.toInt()).toFloat()
                        binding.materialCardViewIcon.radius = radius.toFloat()
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