package appsquared.votings.app.tag.adapter

/**
 * Created by jakobkoerner on 08.02.18.
 */

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.simple_list_item.view.*


class TagDisplayAdapter(private val type: Int, private val items: MutableList<String>, private val compare: MutableList<String>, private val listener: (String) -> Unit) : RecyclerView.Adapter<TagDisplayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item, parent, false)
        return ViewHolder(view, type, compare, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, private val type: Int, private val compare: MutableList<String>, private val listener: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: String) {
            with(itemView) {
                if(type == 0) {
                    imageViewCancel.visibility =  View.GONE
                } else {
                    imageViewCancel.visibility =  View.VISIBLE
                }
                textViewTag.text = item

                val inList = compare.indexOf(item)
                if(inList != -1) textViewTag.setBackgroundResource(R.drawable.round_corners_green)

                if(type != 0) {
                    setOnClickListener {
                        listener(item)
                    }
                }
            }
        }
    }
}