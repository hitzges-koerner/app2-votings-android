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
import app.votings.android.R
import app.votings.android.databinding.DialogInputBinding
import appsquared.votings.app.hideKeyboard

class InputDialog(val context: Context, val listener: (Int, String) -> Unit) {

    val dialog = Dialog(context)
    private lateinit var binding: DialogInputBinding

    fun generate(binding: DialogInputBinding) : InputDialog {
        dialog.setCancelable(true)
        this@InputDialog.binding = binding
        dialog.setContentView(binding.root)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count <= 0) {
                    binding.buttonRight.isEnabled = false
                    binding.buttonRight.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccentDisabled))
                } else {
                    binding.buttonRight.isEnabled = true
                    binding.buttonRight.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent))
                }
            }
        })

        binding.buttonLeft.setOnClickListener{
            listener(LEFT, binding.editText.text.toString())
            context.hideKeyboard(binding.editText)
            dialog.dismiss()
        }

        binding.buttonRight.setOnClickListener{
            listener(RIGHT, binding.editText.text.toString())
            context.hideKeyboard(binding.editText)
            dialog.dismiss()
        }
        return this
    }

    fun setButtonLeftName(text: String) : InputDialog {
        binding.buttonLeft.visibility = View.VISIBLE
        binding.buttonLeft.text = text
        return this
    }

    fun setButtonRightName(text: String) : InputDialog {
        binding.buttonRight.visibility = View.VISIBLE
        binding.buttonRight.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonLeftName(text: Int) : InputDialog {
        binding.buttonLeft.visibility = View.VISIBLE
        binding.buttonLeft.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonRightName(text: Int) : InputDialog {
        binding.buttonRight.visibility = View.VISIBLE
        binding.buttonRight.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : InputDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : InputDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : InputDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : InputDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setHint(text: String) : InputDialog {
        binding.editText.hint = text
        return this
    }

    fun show() {
        dialog.show()
        binding.editText.setSelection(binding.editText.text.toString().length)

        binding.editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    companion object {
        val LEFT = 1
        val RIGHT = 2
    }
}