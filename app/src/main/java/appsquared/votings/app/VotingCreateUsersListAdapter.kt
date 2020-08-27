package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_voting_create_user.view.*

class VotingCreateUsersListAdapter(private val items: MutableList<Model.User>, private val listener: (Int) -> Unit) : RecyclerView.Adapter<VotingCreateUsersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingCreateUsersListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voting_create_user, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: Model.User) {
            with(itemView) {
                textViewUserName.text = "${item.lastName}, ${item.firstName}"

                if(item.isSelected) imageViewUserChecked.setImageResource(R.drawable.ic_round_checked)
                else imageViewUserChecked.setImageResource(R.drawable.ic_round_unchecked)

                itemView.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}