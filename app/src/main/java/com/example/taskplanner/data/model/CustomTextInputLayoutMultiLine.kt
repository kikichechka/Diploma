package com.example.taskplanner.data.model

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.taskplanner.R
import com.example.taskplanner.databinding.CustomTextInputLayoutMultiLineBinding

class CustomTextInputLayoutMultiLine(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    val binding: CustomTextInputLayoutMultiLineBinding

    init {
        val inflatedView = inflate(context, R.layout.custom_text_input_layout_multi_line, this)
        binding = CustomTextInputLayoutMultiLineBinding.bind(inflatedView)
    }
}
