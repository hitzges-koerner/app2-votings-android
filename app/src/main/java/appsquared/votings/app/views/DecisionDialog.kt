package appsquared.votings.app.views

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import appsquared.votings.app.R
import kotlinx.android.synthetic.main.dialog_decision.*
import kotlinx.android.synthetic.main.my_profile_edit_card_view.*
import kotlinx.android.synthetic.main.my_profile_edit_card_view.buttonLeft
import kotlinx.android.synthetic.main.my_profile_edit_card_view.buttonRight

class DecisionDialog(val context: Context, val listener: (Int) -> Unit) {

    val dialog = Dialog(context)

    var mDismissWhenRightButtonClicked = true
    var mDismissWhenLeftButtonClicked = true

    fun generate() : DecisionDialog {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_decision)

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.buttonLeft.setOnClickListener{
            listener(LEFT)
            if(mDismissWhenLeftButtonClicked) dialog.dismiss()
        }

        dialog.buttonRight.setOnClickListener{
            listener(RIGHT)
            if(mDismissWhenRightButtonClicked) dialog.dismiss()
        }
        return this
    }

    fun setButtonLeftName(text: String) : DecisionDialog {
        dialog.buttonLeft.visibility = View.VISIBLE
        dialog.buttonLeft.text = text
        return this
    }

    fun setButtonLeftName(text: String, dismissWhenClicked: Boolean) : DecisionDialog {
        mDismissWhenLeftButtonClicked = dismissWhenClicked
        dialog.buttonLeft.visibility = View.VISIBLE
        dialog.buttonLeft.text = text
        return this
    }

    fun setButtonRightName(text: String) : DecisionDialog {
        dialog.buttonRight.visibility = View.VISIBLE
        dialog.buttonRight.text = text
        return this
    }

    fun setButtonRightName(text: String, dismissWhenClicked: Boolean) : DecisionDialog {
        mDismissWhenRightButtonClicked = dismissWhenClicked
        dialog.buttonRight.visibility = View.VISIBLE
        dialog.buttonRight.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonLeftName(text: Int) : DecisionDialog {
        dialog.buttonLeft.visibility = View.VISIBLE
        dialog.buttonLeft.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setButtonRightName(text: Int) : DecisionDialog {
        dialog.buttonRight.visibility = View.VISIBLE
        dialog.buttonRight.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: String) : DecisionDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: String) : DecisionDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = text
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setMessage(text: Int) : DecisionDialog {
        dialog.textViewMessage.visibility = View.VISIBLE
        dialog.textViewMessage.text = context.getString(text)
        return this
    }

    /**
     * TODO
     * @param text
     */
    fun setTitle(text: Int) : DecisionDialog {
        dialog.textViewTitle.visibility = View.VISIBLE
        dialog.textViewTitle.text = context.getString(text)
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