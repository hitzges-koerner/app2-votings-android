package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import app.votings.android.databinding.DialogDecisionBinding

class DecisionDialog(val context: Context, val listener: (Int) -> Unit) {

    val dialog = Dialog(context)
    private lateinit var binding: DialogDecisionBinding

    var mDismissWhenRightButtonClicked = true
    var mDismissWhenLeftButtonClicked = true

    fun generate(binding: DialogDecisionBinding) : DecisionDialog {
        this@DecisionDialog.binding = binding

        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        binding.buttonLeft.setOnClickListener{
            listener(LEFT)
            if(mDismissWhenLeftButtonClicked) dialog.dismiss()
        }

        binding.buttonRight.setOnClickListener{
            listener(RIGHT)
            if(mDismissWhenRightButtonClicked) dialog.dismiss()
        }
        return this
    }

    fun setButtonLeftName(text: String) : DecisionDialog {
        binding.buttonLeft.visibility = View.VISIBLE
        binding.buttonLeft.text = text
        return this
    }

    fun setButtonLeftName(text: String, dismissWhenClicked: Boolean) : DecisionDialog {
        mDismissWhenLeftButtonClicked = dismissWhenClicked
        binding.buttonLeft.visibility = View.VISIBLE
        binding.buttonLeft.text = text
        return this
    }

    fun setButtonRightName(text: String) : DecisionDialog {
        binding.buttonRight.visibility = View.VISIBLE
        binding.buttonRight.text = text
        return this
    }

    fun setButtonRightName(text: String, dismissWhenClicked: Boolean) : DecisionDialog {
        mDismissWhenRightButtonClicked = dismissWhenClicked
        binding.buttonRight.visibility = View.VISIBLE
        binding.buttonRight.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonLeftName(text: Int) : DecisionDialog {
        binding.buttonLeft.visibility = View.VISIBLE
        binding.buttonLeft.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonRightName(text: Int) : DecisionDialog {
        binding.buttonRight.visibility = View.VISIBLE
        binding.buttonRight.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : DecisionDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : DecisionDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : DecisionDialog {
        binding.textViewMessage.visibility = View.VISIBLE
        binding.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : DecisionDialog {
        binding.textViewTitle.visibility = View.VISIBLE
        binding.textViewTitle.text = context.getString(text)
        return this
    }


    /**
     * TODO
     * @param cancelable
     */
    fun setCancelable(cancelable: Boolean) : DecisionDialog {
        dialog.setCancelable(cancelable)
        return this
    }

    fun show() {
        dialog.show()
    }

    companion object {
        val LEFT = 1
        val RIGHT = 2
    }
}