package appsquared.votings.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import framework.base.rest.Model
import kotlinx.android.synthetic.main.item_create_choice.view.*

class ChoicesListAdapter(private val items: MutableList<String>, private val listener: (Int) -> Unit) : RecyclerView.Adapter<ChoicesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicesListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_create_choice, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(itemView: View, val listener : (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: String) {
            with(itemView) {
                textViewChoiceTitle.text = item

                imageViewChoiceDelete.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}