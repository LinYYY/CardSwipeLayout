package com.ly.cardswipelayout.cardswipe

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

/**
 *  create by Myking
 *  date : 2020/5/26 17:10
 *  description :
 */
class CardRecyclerView(
    context: Context,
    @Nullable attrs: AttributeSet?,
    defStyle: Int
) : RecyclerView(context, attrs, defStyle) {

    private var canvas: Canvas? = null


    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?
    ) : this(context, attrs, 0) {
    }

    constructor(
        context: Context
    ) : this(context, null, 0) {

    }

    override fun onDraw(c: Canvas?) {
        canvas = c
        super.onDraw(c)
    }

    fun getCanvas(): Canvas? {
        return canvas
    }

}