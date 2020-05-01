package appsquared.votings.app.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import appsquared.votings.app.R
import appsquared.votings.app.dpToPx
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.button_card_view.view.*

class ButtonCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.button_card_view, this)

        val textView : TextView = findViewById(R.id.textView)
        val materialCardView : MaterialCardView = findViewById(R.id.materialCardView)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.ButtonCardView
        )

        textView.text = attributes.getString(R.styleable.ButtonCardView_text)
        textView.setTextColor(attributes.getColor(R.styleable.ButtonCardView_text_color, Color.WHITE))

        materialCardView.setCardBackgroundColor(attributes.getColor(R.styleable.ButtonCardView_background_color, Color.WHITE))
        materialCardView.strokeColor = attributes.getColor(R.styleable.ButtonCardView_border_color, Color.WHITE)

        if(attributes.getString(R.styleable.ButtonCardView_button_type).equals("small")) {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            lp.setMargins(dpToPx(8), dpToPx(4), dpToPx(4),dpToPx(8))
            textView.layoutParams = lp

            textView.textSize = 14F
        }

        attributes.recycle()
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setCardBackgroundColor(color: Int) {
        materialCardView.setCardBackgroundColor(color)
    }
}