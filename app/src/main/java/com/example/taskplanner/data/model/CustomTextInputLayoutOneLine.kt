package com.example.taskplanner.data.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.taskplanner.R
import com.example.taskplanner.databinding.CustomTextInputLayoutOneLineBinding
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("ViewConstructor")
class CustomTextInputLayoutOneLine(var index: Int, context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    val binding: CustomTextInputLayoutOneLineBinding

    init {
        val inflatedView = inflate(context, R.layout.custom_text_input_layout_one_line, this)
        binding = CustomTextInputLayoutOneLineBinding.bind(inflatedView)
    }

    fun setHint(text: String) {
        binding.editTitleTask.hint = text
    }
}