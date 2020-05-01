package appsquared.votings.app.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.my_profile_edit_card_view.view.*


class MyProfilEditCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private var onMyProfileEditBttonClickListener: OnMyProfileEditBttonClickListener? = null

    /**
     * TODO
     * @param onToolbarItemClickListener
     */
    fun setOnMyProfileEditButtonClickListener(onMyProfileEditBttonClickListener: OnMyProfileEditBttonClickListener) {
        this.onMyProfileEditBttonClickListener = onMyProfileEditBttonClickListener
    }

    fun setOnClickListener(l: (View) -> Unit) {

    }

    private val editText: EditText
    private var editMode = EDIT_MODE_OFF

    init {
        inflate(context, R.layout.my_profile_edit_card_view, this)

        editText = findViewById(R.id.editTextView)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.MyProfilEditCardView
        )
        imageViewIcon.setImageResource(attributes.getResourceId(R.styleable.MyProfilEditCardView_image, R.drawable.transparent))
        attributes.recycle()

        editText.isEnabled = false
        buttonLeft.visibility = GONE

        buttonLeft.setOnClickListener {
            editMode = EDIT_MODE_OFF
            buttonLeft.visibility = View.GONE
            buttonRight.text = "EDIT"
            editText.isFocusable = false
            editText.clearFocus()
            editText.hideKeyboard()
        }

        buttonRight.setOnClickListener {
            if(editMode == EDIT_MODE_OFF) {
                editMode = EDIT_MODE_ON
                buttonLeft.visibility = View.VISIBLE
                buttonRight.text = "SAVE"
                editText.isEnabled = true
                editText.requestFocus()
                editText.showKeyboard()
            } else {
                editMode = EDIT_MODE_OFF
                buttonLeft.visibility = View.GONE
                buttonRight.text = "EDIT"
                editText.isEnabled = false
                editText.clearFocus()
                editText.hideKeyboard()
            }
        }
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

    fun setPlaceHolderText(text: String) {
        editText.hint = text
    }

    fun setButtonsBackgroundColor(color: Int) {
        buttonLeft.setBackgroundColor(color)
        buttonRight.setBackgroundColor(color)
    }

    fun setButtonsTextColor(color: Int) {
        buttonLeft.setTextColor(color)
        buttonRight.setTextColor(color)
    }

    fun setTextButtonLeft(text: String) {
        buttonLeft.text = text
    }

    fun setTextButtonRight(text: String) {
        buttonRight.text = text
    }

    override fun setBackgroundColor(color: Int) {
        materialCardViewMyProfile.setCardBackgroundColor(color)
    }

    fun setTextColor(color: Int) {
        editText.setTextColor(color)
    }

    fun setPlaceholderColor(color: Int) {
        editText.setHintTextColor(color)
    }

    fun setIconTintColor(color: Int) {
        imageViewIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     *
     */
    interface OnMyProfileEditBttonClickListener {
        fun onClick()
    }

    companion object {
        var LEFT = 1
        var RIGHT = 2
        val EDIT_MODE_ON = 1
        val EDIT_MODE_OFF = 2
    }
}