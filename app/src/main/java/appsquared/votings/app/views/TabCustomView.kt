package appsquared.votings.app.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.votings.android.R
import app.votings.android.databinding.TabCustomBinding

class TabCustomView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    var tabBackgroundColorActive = 0
    var tabBackgroundColorInActive = 0
    var tabTextColorActive = 0
    var tabTextColorInActive = 0

    var binding: TabCustomBinding
    init {
        binding = TabCustomBinding.inflate(LayoutInflater.from(context), this, true)
        binding.textViewTabLeft.setOnClickListener {
            listener?.customOnClick(LEFT)

            binding.textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
            binding.textViewTabRight.setBackgroundColor(tabBackgroundColorInActive)
            binding.textViewTabLeft.setTextColor(tabTextColorActive)
            binding.textViewTabRight.setTextColor(tabTextColorInActive)
        }
        binding.textViewTabRight.setOnClickListener {
            listener?.customOnClick(RIGHT)

            binding.textViewTabLeft.setBackgroundColor(tabBackgroundColorInActive)
            binding.textViewTabRight.setBackgroundColor(tabBackgroundColorActive)
            binding.textViewTabLeft.setTextColor(tabTextColorInActive)
            binding.textViewTabRight.setTextColor(tabTextColorActive)
        }
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.EditTextCardView
        )
        attributes.recycle()
    }

    var listener: CustomOnClickListener? = null

    fun setCustomOnClickListener(l: CustomOnClickListener) {
        listener = l
    }

    public fun setTabBackgroundColorSelected(color: Int) {
        this.tabBackgroundColorActive = color

        binding.textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
        binding.materialCardViewTab.setStrokeColor(tabBackgroundColorActive)
    }

    fun setTabBackgroundColorUnSelected(color: Int) {
        this.tabBackgroundColorInActive = color

        binding.textViewTabRight.setBackgroundColor(tabBackgroundColorInActive)
    }

    fun setTabTextColorSelected(color: Int) {
        this.tabTextColorActive = color

        binding.textViewTabLeft.setTextColor(tabTextColorActive)
    }

    fun setTabTextColorUnSelected(color: Int) {
        this.tabTextColorInActive = color

        binding.textViewTabRight.setTextColor(tabTextColorInActive)
    }

    fun setTabTitleLeft(title: String) {
        binding.textViewTabLeft.text = title
    }

    fun setTabTitleRight(title: String) {
        binding.textViewTabRight.text = title
    }

    companion object {
        var LEFT = 0
        var RIGHT = 1
    }
}

interface CustomOnClickListener {
    fun customOnClick(button: Int)
}
