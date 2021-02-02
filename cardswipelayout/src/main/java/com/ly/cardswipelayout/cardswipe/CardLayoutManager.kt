package com.ly.cardswipelayout.cardswipe

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.ly.cardswipelayout.cardswipe.adapter.CommonViewHolder
import kotlin.math.abs

/**
 *  create by Myking
 *  date : 2020/5/25 11:42
 *  description :
 */
class CardLayoutManager(recyclerView: RecyclerView, itemTouchHelper: ItemTouchHelper) :
    RecyclerView.LayoutManager() {

    private var dx = 0f
    private var dy = 0f

    private val viewOnTouchListener = View.OnTouchListener { v, event ->
        val childHolder = recyclerView.getChildViewHolder(v) as CommonViewHolder
        if (event.action == MotionEvent.ACTION_DOWN) {
            dx = event.x
            dy = event.y
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            if (abs(dx - event.x) >= 10f || abs(dy - event.y) > 10f) {
//                loge("itemview move")
                itemTouchHelper.startSwipe(childHolder)
            }
        } else if (event.action == MotionEvent.ACTION_UP) {
//            loge("itemview up")
//            logd("x:${event.x} y:${event.y} width:${v.width} ic_small_height:${v.height}")
            if (abs(dx - event.x) < 10f && abs(dy - event.y) < 10f) {
                if (v is CardTouchViewGroup) {
                    if (event.x < v.width / 2 && event.y < v.height - 500) {
//                        logd("onLeftViewClick")
                        v.listener?.onLeftViewClick(childHolder)
                    } else if (event.x > v.width / 2 && event.y < v.height - 500) {
//                        logd("onRightViewClick")
                        v.listener?.onRightViewClick(childHolder)
                    } else if (event.y > v.height - 500 && event.y < v.height - 51) {
//                        logd("onButtomViewClick")
                        v.listener?.onBottomViewClick(childHolder)
                    }
                }

            }
        }

        false
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        detachAndScrapAttachedViews(recycler)
//        logd("当前itemCount：${itemCount}")
        if (itemCount > CardConfig.CARD_DEFAULT_ITEM) {
            // 把数据源倒着循环，这样，第0个数据就在屏幕最上面了
            for (position in CardConfig.CARD_DEFAULT_ITEM downTo 0) {
                val view = recycler.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                // 同理
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                // 将 Item View 放入 RecyclerView 中布局
                layoutDecoratedWithMargins(
                    view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view)
                )
                // 其实屏幕上有四张卡片，但是我们把第三张和第四张卡片重叠在一起，这样看上去就只有三张
                // 第四张卡片主要是为了保持动画的连贯性
//                logd("大于默认配置 position：${position}")
                view.scaleY = 1f
                view.scaleX = 1f
                when {
                    position == CardConfig.CARD_DEFAULT_ITEM -> {
                        // 按照一定的规则缩放。
                        // CardConfig.DEFAULT_SCALE 默认为0.1f
                        view.scaleX = 1 - (position - 1) * CardConfig.CARD_DEFAULT_SCALE
                        view.scaleY = 1 - (position - 1) * CardConfig.CARD_DEFAULT_SCALE
                    }
                    position > 0 -> {
                        view.scaleX = 1 - position * CardConfig.CARD_DEFAULT_SCALE
                        view.scaleY = 1 - position * CardConfig.CARD_DEFAULT_SCALE
                    }
                    else -> {
                        // 设置 mTouchListener 的意义就在于我们想让处于顶层的卡片是可以随意滑动的
                        // 而第二层、第三层等等的卡片是禁止滑动的
                        view.setOnTouchListener(viewOnTouchListener)
                    }
                }
            }
        } else {

            for (position in itemCount - 1 downTo 0) {
                val view = recycler.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(
                    view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view)
                )
                view.scaleY = 1f
                view.scaleX = 1f
                if (position > 0) {
                    view.scaleX = 1 - position * CardConfig.CARD_DEFAULT_SCALE
                    view.scaleY = 1 - position * CardConfig.CARD_DEFAULT_SCALE
                } else {
                    view.setOnTouchListener(viewOnTouchListener)
                }
            }
        }
        recycleChildren(recycler)
    }

    /**
     * 回收需回收的Item。
     */
    private fun recycleChildren(recycler: Recycler) {
        for (holder in recycler.scrapList) {
            removeAndRecycleView(holder.itemView, recycler)
        }
    }
}