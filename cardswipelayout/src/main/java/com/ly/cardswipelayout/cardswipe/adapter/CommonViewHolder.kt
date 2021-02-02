package com.ly.cardswipelayout.cardswipe.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

/**
 *  create by Myking
 *  date : 2020/5/15 11:43
 *  description : 通用viewholder封装
 */
class CommonViewHolder private constructor(val context: Context, val convertView: View) :
    RecyclerView.ViewHolder(convertView) {

    private val views: SparseArray<View> = SparseArray()
    var extra: String = ""

    companion object {
        @JvmStatic
        fun createViewHolder(
            context: Context,
            viewGroup: ViewGroup?,
            @LayoutRes layoutId: Int
        ): CommonViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false)
            return CommonViewHolder(context, itemView)
        }
    }

    fun <T : View> getView(@IdRes viewId: Int): T {
        var view = views.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

    fun setText(@IdRes viewId: Int, text: String?): CommonViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes id: Int): CommonViewHolder {
        val tv = getView<TextView>(viewId)
        tv.setText(id)
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int): CommonViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setVisibility(@IdRes viewId: Int, visibility: Int): CommonViewHolder {
        val view = getView<View>(viewId)
        view.visibility = visibility
        return this
    }

    fun setAlpha(@IdRes viewId: Int, alpha: Float): CommonViewHolder {
        val view = getView<View>(viewId)
        view.alpha = alpha
        return this
    }

    fun setOnClickListener(
        @IdRes viewId: Int,
        listener: View.OnClickListener?
    ): CommonViewHolder {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }
}