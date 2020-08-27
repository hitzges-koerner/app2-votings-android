package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import appsquared.votings.app.R
import appsquared.votings.app.hideKeyboard
import kotlinx.android.synthetic.main.activity_invite_user.*
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class InputDialog(val context: Context, val listener: (Int, String) -> Unit) {

    val dialog = Dialog(context)

    fun generate() : InputDialog {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_input)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count <= 0) {
                    dialog.buttonRight.isEnabled = false
                    dialog.buttonRight.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccentDisabled))
                } else {
                    dialog.buttonRight.isEnabled = true
                    dialog.buttonRight.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent))
                }
            }
        })

        dialog.buttonLeft.setOnClickListener{
            listener(LEFT, dialog.editText.text.toString())
            context.hideKeyboard(dialog.editText)
            dialog.dismiss()
        }

        dialog.buttonRight.setOnClickListener{
            listener(RIGHT, dialog.editText.text.toString())
            context.hideKeyboard(dialog.editText)
            dialog.dismiss()
        }
        return this
    }

    fun setButtonLeftName(text: String) : InputDialog {
        dialog.buttonLeft.visibility = View.VISIBLE
        dialog.buttonLeft.text = text
        return this
    }

    fun setButtonRightName(text: String) : InputDialog {
        dialog.buttonRight.visibility = View.VISIBLE
        dialog.buttonRight.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonLeftName(text: Int) : InputDialog {
        dialog.buttonLeft.visibility = View.VISIBLE
        dialog.buttonLeft.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonRightName(text: Int) : InputDialog {
        dialog.buttonRight.visibility = View.VISIBLE
        dialog.buttonRight.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : InputDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : InputDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : InputDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : InputDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setHint(text: String) : InputDialog {
        dialog.editText.hint = text
        return this
    }

    fun show() {
        dialog.show()
        dialog.editText.setSelection(dialog.editText.text.toString().length)

        dialog.editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    companion object {
        val LEFT = 1
        val RIGHT = 2
    }
}