package com.example.taskplanner.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class LinedEditText(context: Context?, attrs: AttributeSet?) :
    AppCompatEditText(context!!, attrs) {
    private val mRect = Rect()
    private val mPaint: Paint = Paint()
    init {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        //  mPaint.setColor(getResources().getColor()); //SET YOUR OWN COLOR HERE
    }

    override fun onDraw(canvas: Canvas) {
        //int count = getLineCount();
        val height = height
        val myLineHeight = lineHeight
        var count = height / myLineHeight
        if (lineCount > count) count = lineCount //for long text with scrolling
        val r: Rect = mRect
        val paint: Paint = mPaint
        var baseline = getLineBounds(0, r) //first line
        for (i in 0 until count) {
            canvas.drawLine(r.left.toFloat(), (baseline + 1).toFloat(),
                r.right.toFloat(), (baseline + 1).toFloat(), paint)
            baseline += myLineHeight //next line
        }
        super.onDraw(canvas)
    }
}