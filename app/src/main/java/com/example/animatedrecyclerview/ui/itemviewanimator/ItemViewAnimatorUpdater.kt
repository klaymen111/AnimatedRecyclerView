package com.example.animatedrecyclerview.ui.itemviewanimator

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * Created by Grigorii Shadrin on 11.12.2019.
 *
 * Абстрактный класс для имплементации метода onUpdate обновления сосотяния каждого ItemView
 * в зависимости от прогресса анимации
 */
abstract class ItemViewAnimatorUpdater {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    fun attachToRecycler(recycler: RecyclerView): ItemViewAnimatorUpdater {
        check(recycler.layoutManager is LinearLayoutManager) { "Incorrect LayoutManager Type" }
        this.layoutManager = recycler.layoutManager as LinearLayoutManager
        this.recyclerView = recycler
        return this
    }

    fun update(progress: Float = 0f) {
        val childCount = layoutManager.childCount
        for (i in 0 until childCount) {
            val view = layoutManager.getChildAt(i)
            view?.let {
                val visiblePosition = if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                    view.right.toFloat() / (view.measuredWidth + view.marginLeft + view.marginRight)
                } else {
                    view.bottom.toFloat() / (view.measuredHeight + view.marginTop + view.marginBottom)
                }
                val adapterPosition = recyclerView.getChildAdapterPosition(view)
                onUpdate(view, ceil(visiblePosition).toInt(), adapterPosition, progress)
            }
        }
    }

    abstract fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float)
}