package com.example.animatedrecyclerview.ui.delegates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

abstract class BaseAdapterDelegate<T : BaseItem, VH : RecyclerView.ViewHolder>(context: Context) :
    AdapterDelegate<List<BaseItem>>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @get:LayoutRes
    internal abstract val layoutId: Int

    override fun isForViewType(items: List<BaseItem>, position: Int): Boolean {
        return isForViewType(items[position])
    }

    internal abstract fun isForViewType(item: BaseItem): Boolean

    internal abstract fun createViewHolder(view: View): VH

    internal abstract fun onBind(item: T, holder: VH)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = inflater.inflate(layoutId, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(
        items: List<BaseItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>
    ) {
        val item = items[position] as T
        val viewHolder = holder as VH
        onBind(item, viewHolder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val viewHolder = holder as VH
        onViewDetached(viewHolder)
    }

    internal fun onViewDetached(viewHolder: VH) {}
}