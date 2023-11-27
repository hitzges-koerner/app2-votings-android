package appsquared.votings.app.tag.adapter

/**
 * Created by jakobkoerner on 08.02.18.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.SimpleListItemBinding


class TagDisplayAdapter(private val type: Int, private val items: MutableList<String>, private val compare: MutableList<String>, private val listener: (String) -> Unit) : RecyclerView.Adapter<TagDisplayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SimpleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, type, compare, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: SimpleListItemBinding, private val type: Int, private val compare: MutableList<String>, private val listener: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: String) {
            with(itemView) {
                if(type == 0) {
                    binding.imageViewCancel.visibility =  View.GONE
                } else {
                    binding.imageViewCancel.visibility =  View.VISIBLE
                }
                binding.textViewTag.text = item

                val inList = compare.indexOf(item)
                if(inList != -1) binding.textViewTag.setBackgroundResource(R.drawable.round_corners_green)

                if(type != 0) {
                    setOnClickListener {
                        listener(item)
                    }
                }
            }
        }
    }
}