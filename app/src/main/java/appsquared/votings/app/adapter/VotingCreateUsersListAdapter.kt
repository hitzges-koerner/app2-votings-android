package appsquared.votings.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemVotingCreateUserBinding
import appsquared.votings.app.rest.Model

class VotingCreateUsersListAdapter(private val items: MutableList<Model.User>, private val listener: (Int) -> Unit) : RecyclerView.Adapter<VotingCreateUsersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVotingCreateUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(private val binding: ItemVotingCreateUserBinding, val listener: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.User) {
            with(itemView) {
                binding.textViewUserName.text = "${item.lastName}, ${item.firstName}"

                if(item.isSelected) binding.imageViewUserChecked.setImageResource(R.drawable.ic_round_checked)
                else binding.imageViewUserChecked.setImageResource(R.drawable.ic_round_unchecked)

                itemView.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}