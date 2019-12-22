package com.example.animatedrecyclerview.ui.selector

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Grigorii Shadrin on 06.12.2019.
 *
 * LinearLayoutManager с возможностью отключения скроллирования
 */
class ScrollDisablingLayoutManager @JvmOverloads constructor(context: Context,
                                                             @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
                                                             reverseLayout: Boolean = false) : LinearLayoutManager(context, orientation, reverseLayout) {
    var isScrollEnabled = true

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled && super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}
