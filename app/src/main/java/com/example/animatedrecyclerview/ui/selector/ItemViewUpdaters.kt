package com.example.animatedrecyclerview.ui.selector

import android.view.View
import com.example.animatedrecyclerview.ui.itemviewanimator.ItemViewAnimatorUpdater

/**
 * Created by Grigorii Shadrin on 11.12.2019.
 *
 * Имплементации абстрактного класса ItemViewAnimatorUpdater для анимации AnimatedSelector
 */
class EmergingUpdater(private val horizontalOffset: Int, private val verticalOffset: Int) : ItemViewAnimatorUpdater() {
    override fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float) {
        view.translationX = progress * (visiblePosition) * horizontalOffset
        if (visiblePosition > 1) {
            view.translationY = -verticalOffset * visiblePosition + (1 - progress) * visiblePosition * verticalOffset
        }
    }
}

class ScatterUpdater(private val leftOffset: Int, private val rightOffset: Int) : ItemViewAnimatorUpdater() {
    override fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float) {
        if (visiblePosition == 1) {
            view.translationX = -progress * leftOffset
        } else {
            view.translationX = progress * rightOffset - view.width * 0.1f
        }
    }
}

class SlideRightUpdater(private val rightOffset: Int) : ItemViewAnimatorUpdater() {
    override fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float) {
        if (visiblePosition == 1) {
            view.translationX = 0f
        } else {
            view.translationX = progress * rightOffset - view.width * 0.1f
        }
    }
}

class SlideLeftWithExcludeUpdater(private val excludedPosition: Int, private val leftOffset: Int) : ItemViewAnimatorUpdater() {
    override fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float) {
        if (adapterPosition != excludedPosition) {
            view.translationX = -progress * leftOffset
        }
    }
}

class ResetFirstPositionYUpdater : ItemViewAnimatorUpdater() {
    override fun onUpdate(view: View, visiblePosition: Int, adapterPosition: Int, progress: Float) {
        if (adapterPosition == 0) {
            view.translationY = 0f
        }
    }
}
