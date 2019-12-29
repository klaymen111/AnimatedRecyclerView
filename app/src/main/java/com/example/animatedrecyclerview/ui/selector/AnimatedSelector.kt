package com.example.animatedrecyclerview.ui.selector

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.animatedrecyclerview.R
import com.example.animatedrecyclerview.model.data.local.vo.BaseItem
import com.example.animatedrecyclerview.ui.adapter.DefaultAdapter
import com.example.animatedrecyclerview.ui.adapter.OnViewAttachedListener
import com.example.animatedrecyclerview.ui.itemviewanimator.ItemViewTransformer
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.layout_animated_selector.view.*
import kotlin.math.roundToInt

/**
 * Created by Grigorii Shadrin on 06.12.2019.
 */
private const val TRANSFORM_NOTIFY_DELAY = 200L
private const val ANIMATION_DELAY = 300L

class AnimatedSelector(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs), SnapListener {

    constructor(context: Context) : this(context, null)

    private var verticalProgress: Float = 1f
    private var bouncingTransformer: ItemViewTransformer
    private var emergingUpdater: EmergingUpdater
    private var scatterUpdater: ScatterUpdater
    private var slideRightUpdater: SlideRightUpdater

    var changeListener: ItemChangeListener? = null
    var transformListener: SelectorTransformListener? = null

    private var offset: Int
    private var horizontalAdapter: InfinityAdapter
    private var verticalAdapter: DefaultAdapter
    private var snapHelper: SelectorSnapHelper
    private var selectedItemInt: BaseItem? = null
    private var isTransformToSingleItem: Boolean = false
    private var isTransformToHorizontalList: Boolean = false
    private val cardShift = 23.px
    private val heightCard = 155.px
    private val screenWidth: Int = context.resources.displayMetrics.widthPixels

    var delegates = emptyList<AdapterDelegate<List<BaseItem>>>()
        set(list) {
            field = list
            horizontalAdapter.addDelegates(list)
            verticalAdapter.addDelegates(list)
            if (itemList.isNotEmpty()) {
                invalidateRecyclers()
            }
        }

    var itemList: List<BaseItem> = emptyList()
        set(list) {
            field = list
            if (delegates.isNotEmpty()) {
                invalidateRecyclers()
            }
        }

    private fun invalidateRecyclers() {
        invalidateHorizontalRecycler(itemList)
        invalidateVerticalRecycler(itemList)
        selectedItem = selectedItemInt
        invalidateSnapHelper()
    }

    private fun invalidateHorizontalRecycler(items: List<BaseItem>) {
        horizontalAdapter.clear()
        horizontalAdapter.addItems(items)
        horizontalAdapter.notifyDataSetChanged()
    }

    private fun invalidateVerticalRecycler(items: List<BaseItem>) {
        verticalAdapter.clear()
        verticalAdapter.addItems(items)
        verticalAdapter.notifyDataSetChanged()
    }

    var selectedItem: BaseItem? = null
        set(value) {
            val itemIndex = if (value != null) itemList.indexOf(value) else 0
            if (itemIndex > -1) {
                field = value
            }
            val number = horizontalAdapter.itemCount / itemList.size / 2
            horizontalRecycler.scrollToPosition(number * itemList.size + itemIndex)
            invalidateSnapHelper()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_animated_selector, this, true)
        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AnimatedSelector, 0, 0)
            offset = typedArray.getDimensionPixelSize(
                R.styleable.AnimatedSelector_ps_offset,
                resources.getDimensionPixelSize(R.dimen.ps_offset_default)
            )
            typedArray.recycle()
        }
        snapHelper = SelectorSnapHelper(23.px, listener = this)
        horizontalAdapter = InfinityAdapter()
        verticalAdapter = DefaultAdapter()
        horizontalRecycler.layoutManager =
            ScrollDisablingLayoutManager(context, RecyclerView.HORIZONTAL, false)
        verticalRecycler.layoutManager =
            ScrollDisablingLayoutManager(context, RecyclerView.VERTICAL, false)
        horizontalRecycler.adapter = horizontalAdapter
        verticalRecycler.adapter = verticalAdapter
        bouncingTransformer = BouncingTransformer(-cardShift)
            .apply { attachToRecycler(horizontalRecycler) }
        emergingUpdater = EmergingUpdater(cardShift + screenWidth, heightCard)
            .apply { attachToRecycler(verticalRecycler) }
        scatterUpdater = ScatterUpdater(screenWidth - cardShift, cardShift)
            .apply { attachToRecycler(horizontalRecycler) }
        slideRightUpdater = SlideRightUpdater(cardShift)
            .apply { attachToRecycler(horizontalRecycler) }
        verticalAdapter.onViewAttachedListener = object : OnViewAttachedListener {
            override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                holder.itemView.setOnTouchListener { view, event ->
                    if (event.actionMasked == MotionEvent.ACTION_UP) {
                        transformToSingleItem(verticalRecycler.getChildAdapterPosition(view))
                    }
                    false
                }
                emergingUpdater.update(verticalProgress)
            }
        }
    }

    private fun invalidateSnapHelper() {
        post {
            snapHelper.attachToRecyclerView(null)
            snapHelper.attachToRecyclerView(horizontalRecycler)
        }
    }

    override fun onSnap(view: View?) {
        val item = view?.tag
        if (item is BaseItem && item != selectedItemInt) {
            selectedItemInt = item
            changeListener?.onItemChanged(item)
        }
    }

    private fun onTransformToSingleItem(baseItem: BaseItem) {
        selectedItem = baseItem
        Handler().postDelayed({
            transformListener?.onTransformToHorizontalList()
        }, TRANSFORM_NOTIFY_DELAY)
        isTransformToHorizontalList = true
    }

    fun transformation(progress: Float) {
        verticalProgress = if (progress < 0.6f) progress / 0.6f else 1f
        val horizontalProgress = 1f - (if (progress > 0.6f) (progress - 0.6f) / 0.4f else 0f)
        if (!isTransformToSingleItem) {
            emergingUpdater.update(verticalProgress)
        }
        if (isTransformToHorizontalList) {
            slideRightUpdater.update(horizontalProgress)
        } else {
            scatterUpdater.update(horizontalProgress)
        }
        when {
            progress == 1f -> {
                hideVerticalRecycler()
                if (isTransformToHorizontalList) {
                    invalidateVerticalRecycler(itemList)
                    isTransformToSingleItem = false
                    isTransformToHorizontalList = false
                }
            }
            progress > 0f && progress < 1f -> {
                setScrollVerticalRecyclerEnabled(false)
                if (progress > 0.6f) {
                    showHorizontalRecycler()
                    hideVerticalRecycler()
                } else {
                    showVerticalRecycler()
                }
            }
            progress == 0f -> {
                setScrollVerticalRecyclerEnabled(true)
                hideHorizontalRecycler()
            }
        }
    }

    private fun setScrollVerticalRecyclerEnabled(isEnabled: Boolean) {
        (verticalRecycler.layoutManager as ScrollDisablingLayoutManager).isScrollEnabled = isEnabled
    }

    private fun showHorizontalRecycler() {
        horizontalRecycler.visibility = View.VISIBLE
    }

    private fun hideHorizontalRecycler() {
        horizontalRecycler.visibility = View.INVISIBLE
    }

    private fun showVerticalRecycler() {
        verticalRecycler.visibility = View.VISIBLE
    }

    private fun hideVerticalRecycler() {
        verticalRecycler.visibility = View.GONE
    }

    fun transformToSingleItem(itemIndex: Int) {
        if (!isTransformToSingleItem) {
            isTransformToSingleItem = true
            var removeIndexes = emptyArray<Int>()
            for ((index, item) in verticalAdapter.getItems().withIndex()) {
                if (itemIndex != index) {
                    removeIndexes += index
                }
            }
            val animator = ValueAnimator.ofFloat(0f, 1f)
            val updater = SlideLeftWithExcludeUpdater(itemIndex, screenWidth)
                .attachToRecycler(verticalRecycler)
            animator.addUpdateListener { valueAnimator ->
                updater.update(valueAnimator.animatedValue as Float)
            }
            animator.duration = ANIMATION_DELAY
            animator.start()
            Handler().postDelayed({
                animator.removeAllUpdateListeners()
                removeAndNotifyOtherItems(removeIndexes)
            }, (ANIMATION_DELAY))
        }
    }

    private fun removeAndNotifyOtherItems(removeIndexes: Array<Int>) {
        for (removeIndex in removeIndexes.reversed()) {
            verticalAdapter.removeItem(removeIndex)
            verticalAdapter.notifyItemRemoved(removeIndex)
        }
        ResetFirstPositionYUpdater()
            .attachToRecycler(verticalRecycler)
            .update()
        Handler().postDelayed({
            verticalAdapter.getItem(0)?.let { onTransformToSingleItem(it) }
        }, (ANIMATION_DELAY))
    }

    private val Int.px: Int
        get() {
            val metrics = Resources.getSystem().displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
                .roundToInt()
        }
}

interface SelectorTransformListener {
    fun onTransformToHorizontalList()
}

interface ItemChangeListener {
    fun onItemChanged(item: BaseItem)
}