package appsquared.votings.app.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import app.votings.android.R
import app.votings.android.databinding.MyProfileEditCardViewRedesignBinding


class MyProfileEditCardView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private var mTempText = ""
    private var mType = -1
    private var mTextColor: Int = 0
    private var mBackgroundColor: Int = 0

    private var onMyProfileEditButtonClickListener: OnMyProfileEditButtonClickListener? = null

    /**
     * TODO
     * @param onToolbarItemClickListener
     */
    fun setOnMyProfileEditButtonClickListener(onMyProfileEditButtonClickListener: OnMyProfileEditButtonClickListener) {
        this.onMyProfileEditButtonClickListener = onMyProfileEditButtonClickListener
    }

    fun setOnClickListener(l: (View) -> Unit) {

    }

    private val editText: EditText
    private var editMode = EDIT_MODE_OFF

    var binding: MyProfileEditCardViewRedesignBinding
    init {
        binding = MyProfileEditCardViewRedesignBinding.inflate(LayoutInflater.from(context), this, true)

        editText = findViewById(R.id.editText)
        val attributes = context.obtainStyledAttributes(attrs,
            R.styleable.MyProfileEditCardView
        )
        binding.imageViewIcon.setImageResource(attributes.getResourceId(R.styleable.MyProfileEditCardView_image, R.drawable.transparent))
        mType = attributes.getInt(R.styleable.MyProfileEditCardView_profil_type, -1)
        attributes.recycle()

        editText.isEnabled = false
        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                editSave()
                onMyProfileEditButtonClickListener?.uploadData()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.imageViewEdit.visibility = VISIBLE
        binding.imageViewCancel.visibility = GONE
        binding.imageViewSave.visibility = GONE

        binding.imageViewEdit.setOnClickListener {
            editMode = EDIT_MODE_ON
            mTempText = getText()
            binding.imageViewEdit.visibility = GONE
            binding.imageViewCancel.visibility = VISIBLE
            binding.imageViewSave.visibility = VISIBLE
            editText.isEnabled = true
            editText.requestFocus()
            editText.isFocusable = true
            editText.setSelection(editText.text.length)
            editText.showKeyboard()
            onMyProfileEditButtonClickListener?.scrollViewToPosition(this)
        }

        binding.imageViewCancel.setOnClickListener {
            editCancel()
        }

        binding.imageViewSave.setOnClickListener {
            editSave()
            onMyProfileEditButtonClickListener?.uploadData()
        }
    }

    fun disabledEdit() {
        binding.imageViewEdit.visibility = GONE
    }

    fun editCancel() {
        setText(mTempText)
        editMode = EDIT_MODE_OFF
        binding.imageViewSave.visibility = GONE
        binding.imageViewCancel.visibility = GONE
        binding.imageViewEdit.visibility = VISIBLE
        editText.isFocusable = false
        editText.clearFocus()
        editText.hideKeyboard()
    }

    fun editSave() {
        editMode = EDIT_MODE_OFF
        binding.imageViewSave.visibility = GONE
        binding.imageViewCancel.visibility = GONE
        binding.imageViewEdit.visibility = VISIBLE
        editText.isEnabled = false
        editText.clearFocus()
        editText.hideKeyboard()
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

    fun setTextColor(color: Int) {
        editText.setTextColor(color)
    }

    fun setPlaceholderColor(color: Int) {
        editText.setHintTextColor(color)
    }

    fun setIconTintColor(color: Int) {
        binding.imageViewIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
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

    fun setCursorColor(color: Int) {
        setCursorDrawableColor(editText, color)
    }

    fun setCursorDrawableColor(editText: TextView, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(color, color))
            gradientDrawable.setSize(2.spToPx(editText.context).toInt(), editText.textSize.toInt())
            editText.textCursorDrawable = gradientDrawable
            return
        }

        try {
            val editorField = try {
                TextView::class.java.getDeclaredField("mEditor").apply { isAccessible = true }
            } catch (t: Throwable) {
                null
            }
            val editor = editorField?.get(editText) ?: editText
            val editorClass: Class<*> = if (editorField == null) TextView::class.java else editor.javaClass

            val tintedCursorDrawable = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                .apply { isAccessible = true }
                .getInt(editText)
                .let { ContextCompat.getDrawable(editText.context, it) ?: return }
                .let { tintDrawable(it, color) }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                editorClass
                    .getDeclaredField("mDrawableForCursor")
                    .apply { isAccessible = true }
                    .run { set(editor, tintedCursorDrawable) }
            } else {
                editorClass
                    .getDeclaredField("mCursorDrawable")
                    .apply { isAccessible = true }
                    .run { set(editor, arrayOf(tintedCursorDrawable, tintedCursorDrawable)) }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun Number.spToPx(context: Context? = null): Float {
        val res = context?.resources ?: android.content.res.Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), res.displayMetrics)
    }

    fun tintDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
        (drawable as? VectorDrawableCompat)
            ?.apply { setTintList(ColorStateList.valueOf(color)) }
            ?.let { return it }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (drawable as? VectorDrawable)
                ?.apply { setTintList(ColorStateList.valueOf(color)) }
                ?.let { return it }
        }

        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return DrawableCompat.unwrap(wrappedDrawable)
    }

    /**
     *
     */
    interface OnMyProfileEditButtonClickListener {
        fun scrollViewToPosition(myProfileEditCardView: MyProfileEditCardView)
        fun uploadData()
    }

    companion object {
        var LEFT = 1
        var RIGHT = 2
        val EDIT_MODE_ON = 1
        val EDIT_MODE_OFF = 2
    }
}