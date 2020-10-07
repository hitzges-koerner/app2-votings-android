package appsquared.votings.app.tag.adapter

/**
 * Created by jakobkoerner on 08.02.18.
 */

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.simple_list_item_default.view.*


class TagListAdapter(private val items: MutableList<String>, val listener: (String) -> Unit) : RecyclerView.Adapter<TagListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item_default, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, val listener:  (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: String) {
            with(itemView) {
                text.text = item

                setOnClickListener {
                    listener(item)
                }
            }
        }
    }
}