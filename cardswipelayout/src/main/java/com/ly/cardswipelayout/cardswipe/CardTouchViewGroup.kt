package com.ly.cardswipelayout.cardswipe

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ly.cardswipelayout.cardswipe.adapter.CommonViewHolder

/**
 *  create by Myking
 *  date : 2020/5/27 10:33
 *  description :
 */
class CardTouchViewGroup(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    FrameLayout(context, attributeSet, defStyle) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)


    var listener: OnViewClickListener? = null


    interface OnViewClickListener {
        fun onLeftViewClick(viewHolder: CommonViewHolder)
        fun onRightViewClick(viewHolder: CommonViewHolder)
        fun onBottomViewClick(viewHolder: CommonViewHolder)
    }

}