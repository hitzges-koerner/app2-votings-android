package appsquared.votings.app.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemSectionUserBinding
import app.votings.android.databinding.ItemUserBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.tag.adapter.TagListAdapter
import com.squareup.picasso.Picasso
import appsquared.votings.app.rest.Model

class UserListAdapter(private val items: MutableList<Model.User>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER = 0
    private val SECTION = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType) {
            USER -> {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderUser(binding, attributes, listener)
            }
            SECTION -> {
                val binding = ItemSectionUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderSection(binding, attributes, listener)
            }
            else -> {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                ViewHolderUser(binding, attributes, listener)
            }
        }
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

    class ViewHolderUser(private val binding: ItemUserBinding, private val attributes: Attributes, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.User) {
            with(itemView) {

                binding.textViewLabelFirst.text = item.lastName + ", " + item.firstName
                binding.textViewLabelFirst.setTextColor(getColor(context, R.color.black))

                binding.textViewLabelSecond.text = item.email
                binding.textViewLabelSecond.setTextColor(getColor(context, R.color.grey_60))

                if(item.isOnline == "1") {
                    binding.circleImageViewStatus.visibility = View.VISIBLE
                    binding.circleImageViewBackground.visibility = View.VISIBLE
                    binding.circleImageViewBackground.circleBackgroundColor = getColor(context, R.color.white)
                } else {
                    binding.circleImageViewStatus.visibility = View.GONE
                    binding.circleImageViewBackground.visibility = View.GONE
                }

                if(item.isConfirmed == "1") {
                    binding.imageViewConfirmedStatus.setImageResource(R.drawable.ic_baseline_how_to_reg_24)
                    binding.imageViewConfirmedStatus.imageTintList = ColorStateList.valueOf(getColor(context, R.color.colorAccent))
                } else {
                    binding.imageViewConfirmedStatus.setImageResource(R.drawable.ic_baseline_error_24)
                    binding.imageViewConfirmedStatus.imageTintList = ColorStateList.valueOf(getColor(context, R.color.colorAccent))
                }



                if(item.avatarUrl.isNotEmpty() && URLUtil.isValidUrl(item.avatarUrl)) {
                    Picasso.get()
                        .load(item.avatarUrl)
                        .placeholder(R.color.grey_144)
                        .into(binding.circleImageViewProfile)
                    binding.circleImageViewProfile.visibility = View.VISIBLE
                } else {
                    binding.circleImageViewProfile.visibility = View.GONE
                    binding.circleImageView.setImageResource(R.drawable.tile_icons_profil)
                    binding.circleImageView.setColorFilter(getColor(context, R.color.white))
                    binding.circleImageView.circleBackgroundColor = getColor(context, R.color.colorAccent)
                }

                val tagList : MutableList<String> = mutableListOf()
                tagList.add("Admin")
                tagList.add("Benutzer")
                tagList.add("Marketing Chef")
                binding.recyclerViewTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.recyclerViewTags.adapter = TagListAdapter(tagList) {
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

    class ViewHolderSection(private val binding: ItemSectionUserBinding, private val attributes: Attributes, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.User) {
            with(itemView) {
                binding.textViewSection.text = item.userId
                binding.textViewSection.setTextColor(getColor(context, R.color.black))

                binding.constraintLayoutRoot.setBackgroundColor(attributes.headlinesBackgroundColor)
            }
        }
    }

}