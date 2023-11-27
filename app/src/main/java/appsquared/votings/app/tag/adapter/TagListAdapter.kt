package appsquared.votings.app.tag.adapter

/**
 * Created by jakobkoerner on 08.02.18.
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.databinding.SimpleListItemDefaultBinding


class TagListAdapter(private val items: MutableList<String>, val listener: (String) -> Unit) : RecyclerView.Adapter<TagListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SimpleListItemDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: SimpleListItemDefaultBinding, val listener:  (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: String) {
            with(itemView) {
                binding.text.text = item

                setOnClickListener {
                    listener(item)
                }
            }
        }
    }
}