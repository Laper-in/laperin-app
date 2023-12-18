package com.capstone.laperinapp.ui.customView

import android.content.Context
import android.util.AttributeSet
import com.capstone.laperinapp.R
import com.google.android.material.chip.Chip

class ChipCategory: Chip {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {

    }
}