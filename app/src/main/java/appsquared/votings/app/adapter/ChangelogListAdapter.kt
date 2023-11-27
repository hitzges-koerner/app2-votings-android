package appsquared.votings.app.adapter

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemChangelogBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.dpToPx
import appsquared.votings.app.getLocalDateStyle
import appsquared.votings.app.rest.Model


class ChangelogListAdapter(private val items: MutableList<Model.Changelog>, val attributes: Attributes, private val listener: (Int) -> Unit) : RecyclerView.Adapter<ChangelogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChangelogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemChangelogBinding, private val attributes: Attributes, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.Changelog) {
            with(itemView) {

                binding.textViewChangelogTitle.text = item.version
                //textViewChangelogTitle.setTextColor(attributes.contentAccentColor)

                val string = SpannableString("Text with\nBullet point")
                string.setSpan(BulletSpan(), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val apiString = item.releaseNotes
                //val string = SpannableString(apiString)
                //string.setSpan(BulletSpan(dpToPx(8),attributes.contentTextColor), 0, apiString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.textViewChangelogText.text = addBullets(context, apiString)
                //textViewChangelogText.setTextColor(attributes.contentTextColor)

                val releaseDate = getLocalDateStyle(item.releaseDate, context)
                binding.textViewChangelogDateTime.text = releaseDate
                //textViewChangelogDateTime.setTextColor(attributes.contentTextColor)

                //materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                //materialCardView.strokeColor = attributes.contentBorderColor
                //materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                //materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()
            }
        }

        fun addBullets(context: Context, text: String) : SpannableString {
            val parts = text.split("\n")
            val string = SpannableString(text)
            for (part in parts) {
                val index: Int = text.indexOf(part)
                string.setSpan(BulletSpan(dpToPx(8),ContextCompat.getColor(context, R.color.black)), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return string
        }
    }
}