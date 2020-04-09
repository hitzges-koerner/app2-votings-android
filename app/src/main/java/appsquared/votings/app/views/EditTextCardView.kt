package appsquared.votings.app.views

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import appsquared.votings.app.R

class EditTextCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private val editText: EditText

    init {
        inflate(context, R.layout.edit_card_view, this)

        editText = findViewById(R.id.editTextView)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.EditTextCardView
        )

        editText.hint = attributes.getString(R.styleable.EditTextCardView_hint)
        attributes.recycle()
    }
    
    fun isEmpty() : Boolean {
        return editText.text.toString().isEmpty()
    }

    fun getText() : String {
        return editText.text.toString()
    }

    fun isEnabled(enabled: Boolean) {
        editText.isEnabled = enabled
    }

    fun setText(text: String) {
        editText.setText(text, TextView.BufferType.EDITABLE)
    }
}