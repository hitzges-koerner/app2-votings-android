package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import app.votings.android.databinding.DialogInfoBinding

class InfoDialog(val context: Context, val listener: () -> Unit) {

    val dialog = Dialog(context)
    private lateinit var binding: DialogInfoBinding

    fun generate(binding: DialogInfoBinding) : InfoDialog {
        this@InfoDialog.binding = binding

        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        binding.button.setOnClickListener{
            listener()
            dialog.dismiss()
        }
        return this
    }

    fun setButtonName(text: String) : InfoDialog {
        binding.button.visibility = View.VISIBLE
        binding.button.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonName(text: Int) : InfoDialog {
        binding.button.visibility = View.VISIBLE
        binding.button.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : InfoDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : InfoDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : InfoDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : InfoDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = context.getString(text)
        return this
    }


    /**
     * TODO
     * @param cancelable
     */
    fun setCancelable(cancelable: Boolean) : InfoDialog {
        dialog.setCancelable(cancelable)
        return this
    }

    fun show() {
        dialog.show()
    }
}