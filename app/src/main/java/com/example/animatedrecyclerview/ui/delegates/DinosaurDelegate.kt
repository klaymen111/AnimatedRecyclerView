package com.example.animatedrecyclerview.ui.delegates

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.animatedrecyclerview.R
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.example.animatedrecyclerview.model.data.local.vo.DinosaurItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_dummy.view.*

/**
 * Created by Grigorii Shadrin on 28.10.2019.
 */

class DinosaurDelegate(context: Context) :
    BaseAdapterDelegate<DinosaurItem, DinosaurDelegate.DummyHolder>(context) {
    override val layoutId: Int
        get() = R.layout.layout_dummy

    override fun isForViewType(item: BaseItem): Boolean {
        return item is DinosaurItem
    }

    override fun createViewHolder(view: View): DummyHolder {
        return DummyHolder(view)
    }

    override fun onBind(item: DinosaurItem, holder: DummyHolder) {
        holder.bind(item)
    }

    inner class DummyHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: DinosaurItem) {
            with(containerView) {
                itemView.tag = item
                textName.text = item.title
                imageBackground.setImageResource(item.background)
            }
        }
    }
}