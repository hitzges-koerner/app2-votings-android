package appsquared.votings.app.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import appsquared.votings.app.R
import com.google.android.material.card.MaterialCardView

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

        attributes.recycle()
    }
}