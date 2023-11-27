package appsquared.votings.app.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import app.votings.android.R
import app.votings.android.databinding.EditCardViewBinding

class EditTextCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    val PASSWORD = 0
    val MAIL = 1
    val NAME = 2
    val PHONE = 3

    var bindingEditCardView: EditCardViewBinding
    init {
        bindingEditCardView = EditCardViewBinding.inflate(LayoutInflater.from(context), this, true)

        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.EditTextCardView
        )

        bindingEditCardView.editTextView.hint = attributes.getString(R.styleable.EditTextCardView_hint)
        when(attributes.getInt(R.styleable.EditTextCardView_type, -1)) {
            PASSWORD -> {
                bindingEditCardView.editTextView.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
            MAIL -> {
                bindingEditCardView.editTextView.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            NAME -> {
                bindingEditCardView.editTextView.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }
            PHONE -> {
                bindingEditCardView.editTextView.inputType = InputType.TYPE_CLASS_PHONE
            }
        }
        attributes.recycle()
    }
    
    fun isEmpty() : Boolean {
        return bindingEditCardView.editTextView.text.toString().isEmpty()
    }

    fun getText() : String {
        return bindingEditCardView.editTextView.text.toString()
    }

    fun isEnabled(enabled: Boolean) {
        bindingEditCardView.editTextView.isEnabled = enabled
    }

    fun setText(text: String) {
        bindingEditCardView.editTextView.setText(text, TextView.BufferType.EDITABLE)
    }
}