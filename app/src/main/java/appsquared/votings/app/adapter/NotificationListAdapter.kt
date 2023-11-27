package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.databinding.ItemNotificationBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.getLocalDateStyle
import appsquared.votings.app.rest.Model

class NotificationListAdapter(private val items: MutableList<Model.Notification>, val attributes: Attributes) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, attributes)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemNotificationBinding, private val attributes: Attributes) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.Notification) {
            with(itemView) {

                binding.textViewNotificationText.text = item.message
                //textViewNotificationText.setTextColor(attributes.contentTextColor)

                binding.textViewNotificationDateTime.text = getLocalDateStyle(item.sentDate, context)
                //textViewNotificationDateTime.setTextColor(attributes.contentTextColor)

                //materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                //materialCardView.strokeColor = attributes.contentBorderColor
                //materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                //materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()
            }
        }
    }
}