package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.dialog_info.*

class InfoDialog(val context: Context, val listener: () -> Unit) {

    val dialog = Dialog(context)

    fun generate() : InfoDialog {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_info)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.button.setOnClickListener{
            listener()
            dialog.dismiss()
        }
        return this
    }

    fun setButtonName(text: String) : InfoDialog {
        dialog.button.visibility = View.VISIBLE
        dialog.button.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonName(text: Int) : InfoDialog {
        dialog.button.visibility = View.VISIBLE
        dialog.button.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : InfoDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : InfoDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : InfoDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : InfoDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = context.getString(text)
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