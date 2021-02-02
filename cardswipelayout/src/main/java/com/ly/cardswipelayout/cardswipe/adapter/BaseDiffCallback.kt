package com.ly.cardswipelayout.cardswipe.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 *  create by Myking
 *  date : 2020/10/12 11:20
 *  description :
 */
abstract class BaseDiffCallback<T>(protected val oldList: List<T>, protected val newList: List<T>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

}