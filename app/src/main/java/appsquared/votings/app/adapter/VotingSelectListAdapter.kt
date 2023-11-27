package appsquared.votings.app.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.votings.android.R
import app.votings.android.databinding.ItemVotingSelectBinding
import appsquared.votings.app.Attributes
import appsquared.votings.app.rest.Model

class VotingSelectListAdapter(
    private val items: MutableList<Model.VotingShort>,
    val attributes: Attributes,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<VotingSelectListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVotingSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, attributes, listener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(
        private val binding: ItemVotingSelectBinding,
        private val attributes: Attributes,
        val listener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Model.VotingShort) {
            with(itemView) {

                val white = ContextCompat.getColor(context, R.color.white)
                binding.imageViewItemVotingSelect.setColorFilter(
                    attributes.contentBackgroundColor,
                    PorterDuff.Mode.SRC_ATOP
                )

                if (item.inRepresentationOfId.isEmpty() && item.inRepresentationOfName.isEmpty()) {
                    binding.textViewItemVotingSelect.text = "für mich selbst"
                } else binding.textViewItemVotingSelect.text = item.inRepresentationOfName

                binding.textViewItemVotingSelect.setTextColor(attributes.contentBackgroundColor)

                binding.materialCardViewItemVotingSelect.setCardBackgroundColor(white)
                binding.materialCardViewItemVotingSelect.strokeColor =
                    attributes.contentBackgroundColor
                binding.materialCardViewItemVotingSelect.setOnClickListener {
                    listener(layoutPosition)
                }
            }
        }
    }
}