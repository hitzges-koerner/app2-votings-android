package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.databinding.ItemCreateChoiceBinding

class ChoicesListAdapter(private val items: MutableList<String>, private val listener: (Int) -> Unit) : RecyclerView.Adapter<ChoicesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreateChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemCreateChoiceBinding, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: String) {
            with(itemView) {
                binding.textViewChoiceTitle.text = item

                binding.imageViewChoiceDelete.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}