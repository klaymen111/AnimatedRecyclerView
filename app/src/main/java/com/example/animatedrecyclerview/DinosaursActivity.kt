package com.example.animatedrecyclerview

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.example.animatedrecyclerview.model.data.local.vo.DinosaurItem
import com.example.animatedrecyclerview.ui.delegates.DinosaurDelegate
import com.example.animatedrecyclerview.ui.selector.ItemChangeListener
import com.example.animatedrecyclerview.ui.selector.SelectorTransformListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*


class DinosaursActivity : AppCompatActivity(), SelectorTransformListener, ItemChangeListener {

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
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> imageGrip.setImageResource(R.drawable.ic_arrow_down)
                        BottomSheetBehavior.STATE_COLLAPSED -> imageGrip.setImageResource(R.drawable.ic_arrow_up)
                        else -> return
                    }
                }

                override fun onSlide(bottomSheet: View, offset: Float) {
                    selector.transformation(offset)
                }
            })
        }
    }

    private fun initSelector() {
        selector.changeListener = this
        selector.transformListener = this
        selector.delegates = listOf(DinosaurDelegate(this))
        textSwitcher.setFactory {
            val textView = TextView(this@DinosaursActivity)
            textView.textSize = 20f
            textView.typeface = Typeface.DEFAULT_BOLD
            textView
        }
        textSwitcher.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        textSwitcher.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
    }

    private fun displayItems(itemList: MutableList<BaseItem>) {
        selector.itemList = itemList
    }

    private fun buildDummyItems(): MutableList<BaseItem> {
        return arrayListOf(
            DinosaurItem(
                title = getStringFromResource(R.string.ankylosaurus),
                description = getStringFromResource(R.string.ankylosaurus_description),
                background = R.drawable.ic_ankylosaurus
            ),
            DinosaurItem(
                title = getStringFromResource(R.string.brachiosaurus),
                description = getStringFromResource(R.string.brachiosaurus_description),
                background = R.drawable.ic_brachiosaurus
            ),
            DinosaurItem(
                title = getStringFromResource(R.string.ichthyosaur),
                description = getStringFromResource(R.string.ichthyosaur_description),
                background = R.drawable.ic_ichthyosaur
            ),
            DinosaurItem(
                title = getStringFromResource(R.string.triceratops),
                description = getStringFromResource(R.string.triceratops_description),
                background = R.drawable.ic_triceratops
            ),
            DinosaurItem(
                title = getStringFromResource(R.string.tyrannosaurus),
                description = getStringFromResource(R.string.tyrannosaurus_description),
                background = R.drawable.ic_tyrannosaurus
            )
        )
    }

    private fun getStringFromResource(@StringRes stringRes: Int) = resources.getString(stringRes)

    override fun onTransformToHorizontalList() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onItemChanged(item: BaseItem) {
        textSwitcher.setText((item as DinosaurItem).description)
    }
}
