package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import app.votings.android.R
import app.votings.android.databinding.DialogListBinding

class ListDialog(val context: Context) {

    val dialog = Dialog(context)
    private lateinit var binding: DialogListBinding
    lateinit var mListener : (String) -> Unit

    fun callBack(listener: (String) -> Unit) : ListDialog {
        mListener = listener
        return this
    }

    fun generate(binding: DialogListBinding) : ListDialog {
        this@ListDialog.binding = binding
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

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

        binding.linearLayoutButtonHolder.addView(button)
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

        binding.linearLayoutButtonHolder.addView(button)
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

        binding.linearLayoutButtonHolder.addView(button)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : ListDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : ListDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : ListDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : ListDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = context.getString(text)
        return this
    }

    fun show() {
        dialog.show()
    }
}