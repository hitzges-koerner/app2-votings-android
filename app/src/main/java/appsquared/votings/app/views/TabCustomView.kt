package appsquared.votings.app.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.tab_custom.view.*

class TabCustomView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    var tabBackgroundColorActive = 0
    var tabBackgroundColorInActive = 0
    var tabTextColorActive = 0
    var tabTextColorInActive = 0

    init {
        inflate(context, R.layout.tab_custom, this)
        textViewTabLeft.setOnClickListener {
            listener?.customOnClick(LEFT)

            textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
            textViewTabRight.setBackgroundColor(tabBackgroundColorInActive)
            textViewTabLeft.setTextColor(tabTextColorActive)
            textViewTabRight.setTextColor(tabTextColorInActive)
        }
        textViewTabRight.setOnClickListener {
            listener?.customOnClick(RIGHT)

            textViewTabLeft.setBackgroundColor(tabBackgroundColorInActive)
            textViewTabRight.setBackgroundColor(tabBackgroundColorActive)
            textViewTabLeft.setTextColor(tabTextColorInActive)
            textViewTabRight.setTextColor(tabTextColorActive)
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

    fun setTabBackgroundColorSelected(color: Int) {
        this.tabBackgroundColorActive = color

        textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
        materialCardViewTab.setStrokeColor(tabBackgroundColorActive)
    }

    fun setTabBackgroundColorUnSelected(color: Int) {
        this.tabBackgroundColorInActive = color

        textViewTabRight.setBackgroundColor(tabBackgroundColorInActive)
    }

    fun setTabTextColorSelected(color: Int) {
        this.tabTextColorActive = color

        textViewTabLeft.setTextColor(tabTextColorActive)
    }

    fun setTabTextColorUnSelected(color: Int) {
        this.tabTextColorInActive = color

        textViewTabRight.setTextColor(tabTextColorInActive)
    }

    fun setTabTitleLeft(title: String) {
        textViewTabLeft.text = title
    }

    fun setTabTitleRight(title: String) {
        textViewTabRight.text = title
    }

    companion object {
        var LEFT = 0
        var RIGHT = 1
    }
}

interface CustomOnClickListener {
    fun customOnClick(button: Int)
}
