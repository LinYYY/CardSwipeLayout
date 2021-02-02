package com.ly.cardswipelayout.cardswipe.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import kotlin.collections.ArrayList

/**
 *  create by Myking
 *  date : 2020/5/15 10:42
 *  description : recyclerView单样式基类
 */
abstract class BaseAdapter<T>(
    protected val context: Context,
    @LayoutRes private val layoutId: Int = -1,
    var dataList: MutableList<T> = ArrayList(),
    var onItemClickListener: OnItemClickListener<T>? = null,
    var onItemLongClickListener: OnItemLongClickListener<T>? = null
) :
    RecyclerView.Adapter<CommonViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    protected var recyclerView: RecyclerView? = null

    abstract fun convert(holder: CommonViewHolder, bean: T, position: Int)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        if (layoutId == -1) {
            throw RuntimeException("layoutId should not be -1")
        }
        return CommonViewHolder.createViewHolder(context, parent, layoutId)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val bean: T = dataList[position]

        holder.convertView.setOnClickListener(this)

        holder.convertView.setOnLongClickListener(this)

        convert(holder, bean, position)
    }

    @CallSuper
    override fun onLongClick(v: View?): Boolean {
        recyclerView?.let {
            if (v!!.parent is RecyclerView) {
                val position = it.getChildAdapterPosition(v!!)
                val holder = it.getChildViewHolder(v) as CommonViewHolder
                onItemLongClickListener?.onItemLongClick(
                    this@BaseAdapter,
                    holder,
                    dataList[position],
                    holder.adapterPosition
                )
            }
        }
        return true
    }

    @CallSuper
    override fun onClick(v: View?) {
        recyclerView?.let {

            if (v!!.parent is RecyclerView) {
                val position = it.getChildAdapterPosition(v!!)
                val holder = it.getChildViewHolder(v) as CommonViewHolder
                onItemClickListener?.onItemClick(
                    this@BaseAdapter,
                    holder,
                    dataList[position],
                    position
                )
            }

        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun add(bean: T, animate: Boolean = true) {
        dataList.add(bean)
        if (animate) {
            notifyItemInserted(dataList.size - 1)
        } else {
            notifyDataSetChanged()
        }
    }

    fun add(index: Int, bean: T, animate: Boolean = true) {
        dataList.add(index, bean)
        if (animate) {
            notifyItemInserted(index)
        } else {
            notifyDataSetChanged()
        }
    }

    fun addAll(subList: List<T>, animate: Boolean = true) {
        dataList.addAll(subList)
        if (animate)
            notifyItemRangeInserted(dataList.size - 1, subList.size)
        else
            notifyDataSetChanged()
    }

    fun addAll(index: Int, subList: List<T>, animate: Boolean = true) {
        dataList.addAll(index, subList)
        if (animate) {
            notifyItemRangeInserted(index, subList.size)
            notifyItemRangeChanged(index + subList.size, dataList.size - index - subList.size)
        } else {
            notifyDataSetChanged()
        }
    }

    fun replace(index: Int, newObject: T) {
        dataList.removeAt(index)
        dataList.add(index, newObject)
        notifyDataSetChanged()
    }

    fun swap(oldIndex: Int, newIndex: Int) {
        val bean = dataList.removeAt(oldIndex)
        dataList.add(newIndex, bean)
        notifyItemMoved(oldIndex, newIndex)
    }

    fun getItem(position: Int): T? {
        return if (position < dataList.size) {
            dataList[position]
        } else null
    }

    fun remove(bean: T, animate: Boolean = true) {
        val position: Int = dataList.indexOf(bean)
        dataList.remove(bean)
        if (animate) {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
        } else {
            notifyDataSetChanged()
        }
    }

    fun remove(index: Int, animate: Boolean = true): T {
        val bean = dataList.removeAt(index)
        if (animate) {
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, dataList.size - index)
        } else {
            notifyDataSetChanged()
        }
        return bean
    }

    fun removeAll(subList: List<T>) {
        dataList.removeAll(subList)
        notifyDataSetChanged()
    }

    fun clear() {
        dataList.clear()
    }

    fun setData(newData: List<T>) {
        dataList = ArrayList(newData)
    }

    interface OnItemClickListener<T> {
        fun onItemClick(
            adapter: BaseAdapter<T>,
            holder: CommonViewHolder,
            bean: T,
            position: Int
        )
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(
            adapter: BaseAdapter<T>,
            holder: CommonViewHolder,
            bean: T,
            position: Int
        )
    }

}