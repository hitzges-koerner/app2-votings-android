package appsquared.votings.app

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appsquared.votings.app.tag.adapter.TagListAdapter
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_user.view.*

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
                val v2 = inflater.inflate(R.layout.item_section_user, viewGroup, false)
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

                textViewLabelFirst.text = item.lastName + ", " + item.firstName
                textViewLabelFirst.setTextColor(getColor(context, R.color.black))

                textViewLabelSecond.text = item.email
                textViewLabelSecond.setTextColor(getColor(context, R.color.grey_60))

                if(item.isOnline == "1") {
                    circleImageViewStatus.visibility = View.VISIBLE
                    circleImageViewBackground.visibility = View.VISIBLE
                    circleImageViewBackground.circleBackgroundColor = getColor(context, R.color.white)
                } else {
                    circleImageViewStatus.visibility = View.GONE
                    circleImageViewBackground.visibility = View.GONE
                }

                if(item.isConfirmed == "1") {
                    imageViewConfirmedStatus.setImageResource(R.drawable.ic_baseline_how_to_reg_24)
                    imageViewConfirmedStatus.imageTintList = ColorStateList.valueOf(getColor(context, R.color.colorAccent))
                } else {
                    imageViewConfirmedStatus.setImageResource(R.drawable.ic_baseline_error_24)
                    imageViewConfirmedStatus.imageTintList = ColorStateList.valueOf(getColor(context, R.color.colorAccent))
                }



                if(item.avatarUrl.isNotEmpty() && URLUtil.isValidUrl(item.avatarUrl)) {
                    Picasso.get()
                        .load(item.avatarUrl)
                        .placeholder(R.color.grey_144)
                        .into(circleImageViewProfile)
                    circleImageViewProfile.visibility = View.VISIBLE
                } else {
                    circleImageViewProfile.visibility = View.GONE
                    circleImageView.setImageResource(R.drawable.tile_icons_profil)
                    circleImageView.setColorFilter(getColor(context, R.color.white))
                    circleImageView.circleBackgroundColor = getColor(context, R.color.colorAccent)
                }

                val tagList : MutableList<String> = mutableListOf()
                tagList.add("Admin")
                tagList.add("Benutzer")
                tagList.add("Marketing Chef")
                recyclerViewTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewTags.adapter = TagListAdapter(tagList) {
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
                textViewSection.setTextColor(getColor(context, R.color.black))

                constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)
            }
        }
    }

}