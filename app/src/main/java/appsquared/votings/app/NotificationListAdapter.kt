package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationListAdapter(private val items: MutableList<Model.Notification>, val attributes: Attributes) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view, attributes)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val attributes: Attributes) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.Notification) {
            with(itemView) {

                textViewNotificationText.text = item.message
                textViewNotificationText.setTextColor(attributes.contentTextColor)

                textViewNotificationDateTime.text = getLocalDateStyle(item.sentDate, context)
                textViewNotificationDateTime.setTextColor(attributes.contentTextColor)

                materialCardView.setCardBackgroundColor(attributes.contentBackgroundColor)
                materialCardView.strokeColor = attributes.contentBorderColor
                materialCardView.strokeWidth = dpToPx(attributes.contentBorderWidth)
                materialCardView.radius = dpToPx(attributes.contentCornerRadius).toFloat()
            }
        }
    }
}