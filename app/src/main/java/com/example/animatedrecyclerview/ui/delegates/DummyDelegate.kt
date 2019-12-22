package com.example.animatedrecyclerview.ui.delegates

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.animatedrecyclerview.R
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.example.animatedrecyclerview.model.data.local.vo.DummyItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_dummy.view.*

/**
 * Created by Grigorii Shadrin on 28.10.2019.
 */

class DummyDelegate(context: Context) :
    BaseAdapterDelegate<DummyItem, DummyDelegate.DummyHolder>(context) {
    override val layoutId: Int
        get() = R.layout.layout_dummy

    private val screenWidth: Int = context.resources.displayMetrics.widthPixels

    override fun isForViewType(item: BaseItem): Boolean {
        return item is DummyItem
    }

    override fun createViewHolder(view: View): DummyHolder {
        return DummyHolder(view)
    }

    override fun onBind(item: DummyItem, holder: DummyHolder) {
        holder.bind(item)
    }

    inner class DummyHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            initViewWidth()
        }

        private fun initViewWidth() {
            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.width = screenWidth
            itemView.layoutParams = params
        }

        fun bind(item: DummyItem) {
            with(containerView) {
                textName.text = item.title
                imageBackground.setImageResource(item.background)
            }
        }
    }
}