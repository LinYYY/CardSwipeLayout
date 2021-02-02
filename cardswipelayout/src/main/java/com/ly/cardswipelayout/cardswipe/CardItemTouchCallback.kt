package com.ly.cardswipelayout.cardswipe

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ly.cardswipelayout.cardswipe.CardConfig.SWIPED_DOWN
import com.ly.cardswipelayout.cardswipe.CardConfig.SWIPED_LEFT
import com.ly.cardswipelayout.cardswipe.CardConfig.SWIPED_RIGHT
import com.ly.cardswipelayout.cardswipe.adapter.BaseAdapter
import kotlin.math.abs


/**
 *  create by Myking
 *  date : 2020/5/25 14:21
 *  description :
 */
class CardItemTouchCallback<T>(
    private val adapter: BaseAdapter<T>,
    private val cardRecyclerView: CardRecyclerView
) :
    ItemTouchHelper.Callback() {

    var listener: OnSwipeListener<T>? = null
    var swipeAnimator: ValueAnimator? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        var swipeFlags = 0
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is CardLayoutManager) {
            swipeFlags =
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    private var ratio = 0.0f

    private fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
        extraFlag: Int = 0
    ) {
        val adapterPosition = viewHolder.adapterPosition

        if (adapterPosition == RecyclerView.NO_POSITION) {
            //当获取不到ID时，直接重新刷新页面
            adapter.notifyDataSetChanged()
            return
        }

        val swipeDirection = when (direction) {
            ItemTouchHelper.LEFT -> SWIPED_LEFT
            ItemTouchHelper.RIGHT -> SWIPED_RIGHT
            ItemTouchHelper.DOWN -> SWIPED_DOWN
            else -> SWIPED_LEFT
        }

        val item: T = adapter.getItem(adapterPosition)!!
//        logd("旋转角度:${ratio}")
        if ((listener?.canSwipeAway(
                viewHolder,
                item,
                swipeDirection, extraFlag
            ) == true && swipeDirection != SWIPED_DOWN)
        ) {
            // 删除相对应的数据
            val remove: T = adapter.remove(adapterPosition, false)
//            adapter.notifyDataSetChanged()
            // 卡片滑出后回调 OnSwipeListener 监听器
            listener?.onSwiped(
                viewHolder,
                remove,
                swipeDirection,
                adapter.itemCount,
                extraFlag
            )
        } else {
            if (swipeDirection == SWIPED_DOWN) {
                revertDownCardSwipe()
            } else {
                revertLRCardSwipe(swipeDirection)
            }
        }


        // 当没有数据时回调 OnSwipeListener 监听器
        if (adapter.itemCount == 0) {
            listener?.onSwipedClear()
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder, direction, 0)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 得到滑动的阀值
            ratio = dX / getThreshold(recyclerView, viewHolder)
            // ratio 最大为 1 或 -1
            if (ratio > 1) {
                ratio = 1f
            } else if (ratio < -1) {
                ratio = -1f
            }
            // 默认最大的旋转角度为 15 度
            itemView.rotation = ratio * CardConfig.DEFAULT_ROTATE_DEGREE
            val childCount = recyclerView.childCount
            // 当数据源个数大于最大显示数时
            if (childCount > CardConfig.CARD_DEFAULT_ITEM) {
                for (position in 1 until childCount - 1) {
                    val index = childCount - position - 1
                    val view: View = recyclerView.getChildAt(position)
                    // 和 onLayoutChildren 是一个意思，不过是做相反的动画
                    view.scaleX =
                        1 - index * CardConfig.CARD_DEFAULT_SCALE + abs(ratio) * CardConfig.CARD_DEFAULT_SCALE
                    view.scaleY =
                        1 - index * CardConfig.CARD_DEFAULT_SCALE + abs(ratio) * CardConfig.CARD_DEFAULT_SCALE
                }
            } else {
                // 当数据源个数小于或等于最大显示数时
                for (position in 0 until childCount - 1) {
                    val index = childCount - position - 1
                    val view: View = recyclerView.getChildAt(position)
                    view.scaleX =
                        1 - index * CardConfig.CARD_DEFAULT_SCALE + abs(ratio) * CardConfig.CARD_DEFAULT_SCALE
                    view.scaleY =
                        1 - index * CardConfig.CARD_DEFAULT_SCALE + abs(ratio) * CardConfig.CARD_DEFAULT_SCALE
                }
            }
            // 回调监听器
            if (ratio != 0f) {
                listener?.onSwiping(
                    viewHolder,
                    ratio,
                    if (ratio < 0) CardConfig.SWIPING_LEFT else CardConfig.SWIPING_RIGHT
                )
            } else {
                listener?.onSwiping(viewHolder, ratio, CardConfig.SWIPING_NONE)
            }

        }
    }

    private fun getThreshold(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Float {
        return recyclerView.width * getSwipeThreshold(viewHolder)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return .25f
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    fun handleCardSwipe(flag: Int, duration: Long, extraFlag: Int) {
        handleCardSwipe(flag, duration, LinearInterpolator(), extraFlag)
    }

    /**
     * 重置左右划动画
     */
    private fun revertLRCardSwipe(flag: Int) {
        if (swipeAnimator != null && swipeAnimator!!.isStarted) {
            return
        }
        val canvas: Canvas = cardRecyclerView.getCanvas() ?: return
        val viewHolder: RecyclerView.ViewHolder =
            cardRecyclerView.findViewHolderForAdapterPosition(0)
                ?: return
        swipeAnimator = when (flag) {
            CardConfig.SWIPED_LEFT -> {
                ValueAnimator.ofFloat((-cardRecyclerView.width).toFloat(), 0f)
            }
            CardConfig.SWIPED_RIGHT -> {
                ValueAnimator.ofFloat((cardRecyclerView.width).toFloat(), 0f)
            }
            else -> {
                throw IllegalStateException("flag must be one of SWIPING_LEFT or SWIPING_RIGHT")
            }
        }
        swipeAnimator!!.duration = 300
        swipeAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            onChildDraw(
                canvas,
                cardRecyclerView,
                viewHolder,
                value,
                0f,
                ItemTouchHelper.ACTION_STATE_SWIPE,
                true
            )
        }
        swipeAnimator!!.interpolator = AccelerateDecelerateInterpolator()
        swipeAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                adapter.notifyDataSetChanged()
                listener?.onSwipeBlock(viewHolder, flag)
                clearView(cardRecyclerView, viewHolder)
            }
        })
        swipeAnimator!!.start()
    }

    /**
     * 重置下滑划动画
     */
    private fun revertDownCardSwipe() {
        if (swipeAnimator != null && swipeAnimator!!.isStarted) {
            return
        }
        val canvas: Canvas = cardRecyclerView.getCanvas() ?: return
        val viewHolder: RecyclerView.ViewHolder =
            cardRecyclerView.findViewHolderForAdapterPosition(0)
                ?: return
        swipeAnimator = ValueAnimator.ofFloat((cardRecyclerView.height).toFloat(), 0f)
        swipeAnimator!!.interpolator = OvershootInterpolator()
        swipeAnimator!!.duration = 300
        swipeAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            onChildDraw(
                canvas,
                cardRecyclerView,
                viewHolder,
                0f,
                value,
                ItemTouchHelper.ACTION_STATE_SWIPE,
                true
            )
        }
        swipeAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                listener?.onSwipeBlock(viewHolder, CardConfig.SWIPED_DOWN)
                adapter.notifyDataSetChanged()
                clearView(cardRecyclerView, viewHolder)
            }
        })
        swipeAnimator!!.start()
    }


    fun handleCardSwipe(
        flag: Int,
        duration: Long,
        interpolator: TimeInterpolator?,
        extraFlag: Int
    ) {
        if (swipeAnimator != null && swipeAnimator!!.isStarted) {
            return
        }
        val canvas: Canvas = cardRecyclerView.getCanvas() ?: return
        val viewHolder: RecyclerView.ViewHolder =
            cardRecyclerView.findViewHolderForAdapterPosition(0)
                ?: return
        swipeAnimator = when (flag) {
            CardConfig.SWIPING_LEFT -> {
                ValueAnimator.ofFloat(0f, (-cardRecyclerView.width).toFloat())
            }
            CardConfig.SWIPING_RIGHT -> {
                ValueAnimator.ofFloat(0f, (cardRecyclerView.width).toFloat())
            }
            else -> {
                throw IllegalStateException("flag must be one of SWIPING_LEFT or SWIPING_RIGHT")
            }
        }
        swipeAnimator!!.duration = duration
        interpolator?.let { swipeAnimator!!.interpolator = it }
        swipeAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            onChildDraw(
                canvas,
                cardRecyclerView,
                viewHolder,
                value,
                0f,
                ItemTouchHelper.ACTION_STATE_SWIPE,
                true
            )
        }
        swipeAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                onSwiped(
                    viewHolder,
                    if (flag == CardConfig.SWIPING_LEFT) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT,
                    extraFlag
                )
                clearView(cardRecyclerView, viewHolder)
            }
        })
        swipeAnimator!!.start()
    }

    fun guideAnimate(callback: (() -> Unit)?) {
        if (swipeAnimator != null && swipeAnimator!!.isStarted) {
            return
        }
        val canvas: Canvas = cardRecyclerView.getCanvas() ?: return
        val viewHolder: RecyclerView.ViewHolder =
            cardRecyclerView.findViewHolderForAdapterPosition(0)
                ?: return

        swipeAnimator = ValueAnimator.ofFloat(
            0f,
            (-cardRecyclerView.width / 4).toFloat(),
            0f,
            (cardRecyclerView.width / 4).toFloat(),
            0f
        )

        swipeAnimator!!.duration = 3000
        swipeAnimator!!.interpolator = LinearInterpolator()
        swipeAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            onChildDraw(
                canvas,
                cardRecyclerView,
                viewHolder,
                value,
                0f,
                ItemTouchHelper.ACTION_STATE_SWIPE,
                true
            )
        }
        swipeAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                callback?.invoke()
            }
        })
        swipeAnimator!!.start()
    }
}