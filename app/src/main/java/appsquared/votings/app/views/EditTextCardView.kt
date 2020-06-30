package appsquared.votings.app.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import appsquared.votings.app.R

class EditTextCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    val PASSWORD = 0
    val MAIL = 1
    val NAME = 2
    val PHONE = 3


    private val editText: EditText

    init {
        inflate(context, R.layout.edit_card_view, this)

        editText = findViewById(R.id.editTextView)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.EditTextCardView
        )

        editText.hint = attributes.getString(R.styleable.EditTextCardView_hint)
        when(attributes.getInt(R.styleable.EditTextCardView_type, -1)) {
            PASSWORD -> {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
            MAIL -> {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            NAME -> {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }
            PHONE -> {
                editText.inputType = InputType.TYPE_CLASS_PHONE
            }
        }
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