package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.dialog_list.*
import kotlinx.android.synthetic.main.dialog_list.textViewMessage
import kotlinx.android.synthetic.main.dialog_list.textViewTitle

class ListDialog(val context: Context) {

    val dialog = Dialog(context)
    lateinit var mListener : (String) -> Unit

    fun callBack(listener: (String) -> Unit) : ListDialog {
        mListener = listener
        return this
    }

    fun generate() : ListDialog {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_list)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        return this
    }

    fun addButton(tag: String, text: String) : ListDialog {
        val button = Button(context)
        button.text = text
        button.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        button.setTextColor(Color.WHITE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.backgroundTintList = context.resources.getColorStateList(R.color.colorPrimary, null)
        } else {
            button.backgroundTintList = context.resources.getColorStateList(R.color.colorPrimary)
        }
        button.setOnClickListener {
            mListener(tag)
            dialog.dismiss()
        }

        dialog.linearLayoutButtonHolder.addView(button)
        return this
    }

    fun addButton(tag: String, text: Int) : ListDialog {
        val button = Button(context)
        button.text = context.getString(text)
        button.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        button.setTextColor(Color.WHITE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.backgroundTintList = context.resources.getColorStateList(R.color.colorPrimary, null)
        } else {
            button.backgroundTintList = context.resources.getColorStateList(R.color.colorPrimary)
        }
        button.setOnClickListener {
            mListener(tag)
            dialog.dismiss()
        }

        dialog.linearLayoutButtonHolder.addView(button)
        return this
    }

    fun addCancelButton(listener: () -> Unit) : ListDialog {
        val button = Button(context)
        button.text = context.getString(android.R.string.cancel)
        button.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setTextColor(context.resources.getColor(R.color.colorPrimary, null))
            button.backgroundTintList = context.resources.getColorStateList(R.color.white, null)
        } else {
            button.setTextColor(context.resources.getColor(R.color.colorPrimary))
            button.backgroundTintList = context.resources.getColorStateList(R.color.white)
        }
        button.setOnClickListener {
            listener()
            dialog.dismiss()
        }

        dialog.linearLayoutButtonHolder.addView(button)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : ListDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : ListDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : ListDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : ListDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = context.getString(text)
        return this
    }

    fun show() {
        dialog.show()
    }
}