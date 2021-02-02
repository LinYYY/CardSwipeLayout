package com.ly.cardswipelayout.cardswipe

import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.BaseItemAnimator

/**
 *  create by Myking
 *  date : 2020/5/26 18:03
 *  description :撤回动画
 */
class CardItemAnimator : BaseItemAnimator {
    private val mTension: Float

    constructor() {
        mTension = 2.0f
        moveDuration = 300
        addDuration = 300
        removeDuration = 300
        changeDuration = 300
    }

    constructor(mTension: Float) {
        this.mTension = mTension
    }

    override fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        ViewCompat.animate(holder.itemView)
//            .translationX(-holder.itemView.rootView.width.toFloat())
            .setDuration(removeDuration)
            .setListener(DefaultRemoveVpaListener(holder))
            .setStartDelay(getRemoveDelay(holder))
            .start()
    }

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition == 0) {
            ViewCompat.setTranslationX(
                holder.itemView,
                -holder.itemView.rootView.width.toFloat()
            )
        }
    }

    override fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition == 0) {
            ViewCompat.animate(holder.itemView)
                .translationX(0f)
                .setDuration(addDuration)
                .setListener(DefaultAddVpaListener(holder))
                .setInterpolator(OvershootInterpolator(mTension))
                .setStartDelay(getAddDelay(holder))
                .start()
        } else {
            ViewCompat.animate(holder.itemView)
                .setDuration(addDuration)
                .setListener(DefaultAddVpaListener(holder))
                .setInterpolator(OvershootInterpolator(mTension))
                .setStartDelay(getAddDelay(holder))
                .start()
        }

    }
}