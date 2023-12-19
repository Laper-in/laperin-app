package com.capstone.laperinapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.laperinapp.R

class EditTextEmail : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var editTextBackground: Drawable? = null
    private var editTextErrorBackground: Drawable? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hint = context.getString(R.string.username)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = editTextBackground ?: ContextCompat.getDrawable(context, R.drawable.bg_edit_text)

        if (error != null && editTextErrorBackground != null) {
            background = editTextErrorBackground
        }
    }

    private fun init() {
        editTextBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text)
        editTextErrorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_error)
    }
}
