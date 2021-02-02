package com.ly.cardswipelayout.cardswipe

import androidx.recyclerview.widget.RecyclerView


/**
 *  create by Myking
 *  date : 2020/5/25 14:33
 *  description :
 */
interface OnSwipeListener<T> {

    /**
     * 卡片还在滑动时回调
     *
     * @param viewHolder 该滑动卡片的viewHolder
     * @param ratio      滑动进度的比例
     * @param direction  卡片滑动的方向，SWIPING_LEFT 为向左滑，SWIPING_RIGHT 为向右滑，
     * SWIPING_NONE 为不偏左也不偏右
     */
    fun onSwiping(
        viewHolder: RecyclerView.ViewHolder,
        ratio: Float,
        direction: Int
    )

    /**
     * 卡片完全滑出时回调
     *
     * @param viewHolder 该滑出卡片的viewHolder
     * @param t          该滑出卡片的数据
     * @param direction  卡片滑出的方向，SWIPED_LEFT 为左边滑出；SWIPED_RIGHT 为右边滑出
     * @param restCount  剩余卡片的数量
     * @param isForceSwipe 是否强制执行的滑动
     */
    fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        t: T,
        direction: Int,
        restCount: Int,
        extraFlag: Int
    )

    /**
     * 所有的卡片全部滑出时回调
     */
    fun onSwipedClear()

    /**
     * 是否可以划走的条件
     * @param viewHolder 将要滑出卡片的viewHolder
     * @param t          将要滑出卡片的数据
     * @param direction  卡片滑出的方向，SWIPED_LEFT 为左边；SWIPED_RIGHT 为右边
     * @param extraFlag  额外信息
     */
    fun canSwipeAway(
        viewHolder: RecyclerView.ViewHolder,
        t: T,
        direction: Int,
        extraFlag: Int
    ): Boolean

    /**
     * 滑动被拦截回调
     * @param viewHolder 将要滑出卡片的viewHolder
     * @param direction  卡片滑出的方向，[CardConfig.SWIPED_LEFT] 为左边；[CardConfig.SWIPED_RIGHT] 为右边 [CardConfig.SWIPED_DOWN] 为下边
     */
    fun onSwipeBlock(viewHolder: RecyclerView.ViewHolder, direction: Int)
}