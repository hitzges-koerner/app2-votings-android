package appsquared.votings.app.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import app.votings.android.R
import app.votings.android.databinding.ButtonCardViewBinding
import appsquared.votings.app.dpToPx
import com.google.android.material.card.MaterialCardView

class ButtonCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    var bindingButtonCardView: ButtonCardViewBinding
    init {
        bindingButtonCardView = ButtonCardViewBinding.inflate(LayoutInflater.from(context), this, true)

        val textView : TextView = findViewById(R.id.textView)
        val materialCardView : MaterialCardView = findViewById(R.id.materialCardView)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.ButtonCardView
        )

        textView.text = attributes.getString(R.styleable.ButtonCardView_text)
        textView.setTextColor(attributes.getColor(R.styleable.ButtonCardView_text_color, Color.WHITE))

        materialCardView.setCardBackgroundColor(attributes.getColor(R.styleable.ButtonCardView_background_color, Color.WHITE))
        materialCardView.strokeColor = attributes.getColor(R.styleable.ButtonCardView_border_color, Color.WHITE)
        materialCardView.strokeWidth = attributes.getInteger(R.styleable.ButtonCardView_border_size, 0)

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
        bindingButtonCardView.textView.text = text
    }

    fun setCardBackgroundColor(color: Int) {
        bindingButtonCardView.materialCardView.setCardBackgroundColor(color)
    }
}