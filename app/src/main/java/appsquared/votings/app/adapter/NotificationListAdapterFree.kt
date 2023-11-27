package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemNotificationFreeBinding
import appsquared.votings.app.getLocalDateStyle
import appsquared.votings.app.rest.Model

class NotificationListAdapterFree(private val items: MutableList<Model.Notification>) : RecyclerView.Adapter<NotificationListAdapterFree.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationFreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemNotificationFreeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.Notification) {
            with(itemView) {

                binding.textViewNotificationText.text = item.message
                binding.textViewNotificationText.setTextColor(ContextCompat.getColor(context, R.color.black))

                binding.textViewNotificationDateTime.text = getLocalDateStyle(item.sentDate, context)
                binding.textViewNotificationDateTime.setTextColor(ContextCompat.getColor(context, R.color.grey_144))
            }
        }
    }
}