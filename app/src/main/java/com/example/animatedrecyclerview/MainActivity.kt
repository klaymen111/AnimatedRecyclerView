package com.example.animatedrecyclerview

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.example.animatedrecyclerview.model.data.local.vo.DummyItem
import com.example.animatedrecyclerview.ui.delegates.DummyDelegate
import com.example.animatedrecyclerview.ui.selector.SelectorTransformListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SelectorTransformListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomSheetBehavior()
        initSelector()
        displayItems(buildDummyItems())
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, offset: Float) {
                    productSelector.transformation(offset)
                }
            })
        }
    }

    private fun initSelector() {
        productSelector.transformListener = this
        productSelector.delegates = listOf(DummyDelegate(this))
    }

    private fun displayItems(itemList: MutableList<BaseItem>) {
        productSelector.itemList = itemList
    }

    private fun buildDummyItems(): MutableList<BaseItem> {
        return arrayListOf(
            DummyItem(title = "1", background = R.drawable.gradient_background),
            DummyItem(title = "2", background = R.drawable.gradient_background),
            DummyItem(title = "3", background = R.drawable.gradient_background),
            DummyItem(title = "4", background = R.drawable.gradient_background),
            DummyItem(title = "5", background = R.drawable.gradient_background),
            DummyItem(title = "6", background = R.drawable.gradient_background),
            DummyItem(title = "7", background = R.drawable.gradient_background),
            DummyItem(title = "8", background = R.drawable.gradient_background)
        )
    }

    override fun onTransformToHorizontalList() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
