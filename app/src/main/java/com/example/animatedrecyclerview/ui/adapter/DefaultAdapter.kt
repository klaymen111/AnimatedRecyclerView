package com.example.animatedrecyclerview.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import java.util.*

open class DefaultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onViewAttachedListener: OnViewAttachedListener? = null

    val delegatesManager: AdapterDelegatesManager<List<BaseItem>> = AdapterDelegatesManager()
    private val items: MutableList<BaseItem> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(items, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        onViewAttachedListener?.onViewAttachedToWindow(holder)
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        items.clear()
    }

    open fun addItems(items: List<BaseItem>) {
        this.items.addAll(items)
    }

    open fun getItem(index: Int): BaseItem? {
        return if (items.size > index)
            items[index]
        else
            null
    }

    fun getItems(): List<BaseItem> {
        return items
    }

    fun removeItem(position: Int) {
        if (items.size > position) {
            items.removeAt(position)
        }
    }

    open fun addDelegate(delegate: AdapterDelegate<List<BaseItem>>): DefaultAdapter {
        delegatesManager.addDelegate(delegate)
        return this
    }

    fun addDelegates(delegates: List<AdapterDelegate<List<BaseItem>>>) {
        delegates.forEach {
            addDelegate(it)
        }
    }
}

interface OnViewAttachedListener {
    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder)
}