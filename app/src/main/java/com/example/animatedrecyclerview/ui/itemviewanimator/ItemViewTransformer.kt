package com.example.animatedrecyclerview.ui.itemviewanimator

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Grigorii Shadrin on 11.12.2019.
 *
 * Абстрактный класс имплементирующий возможности аналогичные интерфейсу ViewPager PageTransformer.
 * В теории, код анимации можно просто копипастить из существующих имплементаций PageTransformer
 */
abstract class ItemViewTransformer {

    init {
        currentItemViewOffset = 0
        isCentered = true
    }

    constructor(currentItemViewOffset: Int) {
        this.currentItemViewOffset = currentItemViewOffset
        this.isCentered = false
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var attachedAdapter: RecyclerView.Adapter<*>
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var dataObserver: RecyclerView.AdapterDataObserver
    private var isCentered: Boolean
    private var currentItemViewOffset: Int
    private var measuredChildWidth: Int = 0

    private val currentFrameLeft: Float by lazy {
        if (isCentered) {
            (recyclerView.measuredWidth - childWidth) / 2
        } else {
            currentItemViewOffset.toFloat()
        }
    }

    private val childWidth: Float by lazy {
        if (measuredChildWidth == 0) {
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                if (child.measuredWidth != 0) {
                    measuredChildWidth = child.measuredWidth
                    measuredChildWidth.toFloat()
                }
            }
        }
        measuredChildWidth.toFloat()
    }

    fun attachToRecycler(recycler: RecyclerView) {
        check(recycler.layoutManager is LinearLayoutManager) { "Only LinearLayoutManager is supported" }
        this.layoutManager = recycler.layoutManager as LinearLayoutManager
        this.recyclerView = recycler
        this.attachedAdapter = recycler.adapter as RecyclerView.Adapter<*>

        dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                updatePositions()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }
        }
        attachedAdapter.registerAdapterDataObserver(dataObserver)
        updatePositions()

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updatePositions()
            }
        }

        recyclerView.addOnScrollListener(scrollListener)
    }

    fun detachFromRecycler() {
        attachedAdapter.unregisterAdapterDataObserver(dataObserver)
        recyclerView.removeOnScrollListener(scrollListener)
        measuredChildWidth = 0
    }

    private fun updatePositions() {
        val views = findVisibleViews()
        for (view in views) {
            val position: Float = if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                (view.left - currentFrameLeft) / (view.measuredWidth + view.marginLeft + view.marginRight)
            } else {
                (view.top - currentFrameLeft) / (view.measuredHeight + view.marginTop + view.marginBottom)
            }
            transformItemView(view, position)
        }
    }

    private fun findVisibleViews(): List<View> {
        val views: ArrayList<View> = ArrayList()
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return views
        }
        for (i in 0 until childCount) {
            views.add(layoutManager.getChildAt(i) ?: break)
        }
        return views
    }

    abstract fun transformItemView(view: View, position: Float)
}