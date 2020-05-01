package appsquared.votings.app.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import appsquared.votings.app.NewsListActivity
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.tab_custom.view.*

class TabCustomView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    var tabBackgroundColorActive = 0
    var tabBackgroundColor = 0
    var tabTextColorActive = 0
    var tabTextColor = 0

    init {
        inflate(context, R.layout.tab_custom, this)
        textViewTabLeft.setOnClickListener {
            listener?.customOnClick(LEFT)

            textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
            textViewTabRight.setBackgroundColor(tabBackgroundColor)
            textViewTabLeft.setTextColor(tabTextColorActive)
            textViewTabRight.setTextColor(tabTextColor)
        }
        textViewTabRight.setOnClickListener {
            listener?.customOnClick(RIGHT)

            textViewTabLeft.setBackgroundColor(tabBackgroundColor)
            textViewTabRight.setBackgroundColor(tabBackgroundColorActive)
            textViewTabLeft.setTextColor(tabTextColor)
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

    fun setTabBackgroundColor(colorActive: Int, color: Int) {
        this.tabBackgroundColorActive = colorActive
        this.tabBackgroundColor = color

        textViewTabLeft.setBackgroundColor(tabBackgroundColorActive)
        textViewTabRight.setBackgroundColor(tabBackgroundColor)
    }

    fun setTabTextColor(colorActive: Int, color: Int) {
        this.tabTextColorActive = colorActive
        this.tabTextColor = color

        textViewTabLeft.setTextColor(tabTextColorActive)
        textViewTabRight.setTextColor(tabTextColor)
    }

    companion object {
        var LEFT = 1
        var RIGHT = 2
    }
}

interface CustomOnClickListener {
    fun customOnClick(button: Int)
}
