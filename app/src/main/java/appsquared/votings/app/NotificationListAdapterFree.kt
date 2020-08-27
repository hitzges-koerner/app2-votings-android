package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_notification_free.view.*

class NotificationListAdapterFree(private val items: MutableList<Model.Notification>) : RecyclerView.Adapter<NotificationListAdapterFree.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListAdapterFree.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification_free, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.Notification) {
            with(itemView) {

                textViewNotificationText.text = item.message
                textViewNotificationText.setTextColor(ContextCompat.getColor(context, R.color.black))

                textViewNotificationDateTime.text = getLocalDateStyle(item.sentDate, context)
                textViewNotificationDateTime.setTextColor(ContextCompat.getColor(context, R.color.grey_144))
            }
        }
    }
}