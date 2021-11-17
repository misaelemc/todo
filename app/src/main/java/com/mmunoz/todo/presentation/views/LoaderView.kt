package com.mmunoz.todo.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.mmunoz.todo.R

class LoaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        inflate(context, R.layout.loader_layout, this)
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_black))
        isClickable = false
        isFocusable = false
    }
}