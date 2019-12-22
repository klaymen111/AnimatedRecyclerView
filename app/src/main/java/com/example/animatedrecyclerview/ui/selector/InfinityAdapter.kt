package com.example.animatedrecyclerview.ui.selector

import androidx.recyclerview.widget.RecyclerView
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.example.animatedrecyclerview.ui.adapter.DefaultAdapter

/**
 * Created by Grigorii Shadrin on 30.11.2019.
 *
 * Горизинтальный зацикленный адаптер для RecyclerView
 */
open class InfinityAdapter : DefaultAdapter() {

    val realCount: Int
        get() = getItems().size

    override fun getItemCount(): Int {
        return when {
            realCount > 1 -> Integer.MAX_VALUE
            realCount == 1 -> 1
            else -> 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(getItems(), position % realCount)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position % realCount)
    }

    override fun getItem(position: Int): BaseItem? {
        return if (realCount > 0) {
            getItems()[position % realCount]
        } else {
            null
        }
    }

    override fun addDelegate(delegate: AdapterDelegate<List<BaseItem>>): InfinityAdapter {
        delegatesManager.addDelegate(delegate)
        return this
    }
}
