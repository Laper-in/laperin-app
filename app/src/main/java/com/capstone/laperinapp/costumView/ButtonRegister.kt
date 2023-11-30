package com.capstone.laperinapp.costumView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.capstone.laperinapp.R

class ButtonRegister
    : AppCompatButton {

        private lateinit var background : Drawable
        constructor(context: Context) : super(context) {
            init()
        }

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            init()
        }

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
            init()
        }

        private fun init() {
            background = ContextCompat.getDrawable(context, R.drawable.rounded_button) as Drawable
        }
    }
