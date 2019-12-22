package com.example.animatedrecyclerview.ui.selector

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ProductSelectorSnapHelper(private val offset: Int = 0, private val listener: SnapListener) : LinearSnapHelper() {

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()
        val targetPosition = if (velocityX < 0) position else position + 1
        return min(linearLayoutManager.itemCount - 1, max(targetPosition, 0))
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        val linearLayoutManager = layoutManager as LinearLayoutManager
        var position = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            position = linearLayoutManager.findFirstVisibleItemPosition()
        }
        if (position != RecyclerView.NO_POSITION) {
            val view = linearLayoutManager.findViewByPosition(position)
            val offscreen = abs(linearLayoutManager.getDecoratedLeft(view!!))
            val onscreen = abs(linearLayoutManager.getDecoratedRight(view))
            val prefView = if (onscreen >= offscreen || position + 1 == linearLayoutManager.itemCount) {
                view
            } else {
                linearLayoutManager.findViewByPosition(position + 1)
            }
            listener.onSnap(prefView)
            return prefView
        }
        return null
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val helper: OrientationHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        val out = IntArray(2)
        out[0] = distanceToStart(targetView, helper)
        out[1] = 0
        return out
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding + offset
    }
}

interface SnapListener {
    fun onSnap(view: View?)
}