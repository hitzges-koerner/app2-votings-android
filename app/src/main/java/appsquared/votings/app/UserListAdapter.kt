package appsquared.votings.app

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.item_user.view.materialCardView

class UserListAdapter(private val items: MutableList<Model.User>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER = 0
    private val SECTION = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        viewHolder = when (viewType) {
            USER -> {
                val v1 = inflater.inflate(R.layout.item_user, viewGroup, false)
                ViewHolderUser(v1, attributes, listener)
            }
            SECTION -> {
                val v2 = inflater.inflate(R.layout.item_section, viewGroup, false)
                ViewHolderSection(v2, attributes, listener)
            }
            else -> {
                val v1 = inflater.inflate(R.layout.item_user, viewGroup, false)
                ViewHolderUser(v1, attributes, listener)
            }
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].userId.length <= 1) {
            return SECTION
        }
        return USER
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            USER -> {
                val vh1 = viewHolder as ViewHolderUser
                vh1.bindItems(items[position])
            }
            SECTION -> {
                val vh2 = viewHolder as ViewHolderSection
                vh2.bindItems(items[position])
            }
        }
    }

    class ViewHolderUser(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.User) {
            with(itemView) {

                textViewLabelFirst.text = item.lastName
                textViewLabelFirst.setTextColor(attributes.contentTextColor)

                textViewLabelSecond.text = item.firstName
                textViewLabelSecond.setTextColor(attributes.contentTextColor)

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()

                if(item.isOnline.equals("1")) {
                    circleImageViewStatus.visibility = View.VISIBLE
                    circleImageViewBackground.visibility = View.VISIBLE
                    circleImageViewBackground.circleBackgroundColor = attributes.contentBackgroundColor
                } else {
                    circleImageViewStatus.visibility = View.GONE
                    circleImageViewBackground.visibility = View.GONE
                }

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

    class ViewHolderSection(itemView: View, private val attributes: Attributes, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.User) {
            with(itemView) {
                textViewSection.text = item.userId
                textViewSection.setTextColor(attributes.contentAccentColor)

                constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)
            }
        }
    }

}