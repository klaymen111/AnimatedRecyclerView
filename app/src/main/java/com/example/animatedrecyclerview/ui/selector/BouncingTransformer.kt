package com.example.animatedrecyclerview.ui.selector

import android.view.View
import com.example.animatedrecyclerview.ui.itemviewanimator.ItemViewTransformer

class BouncingTransformer(currentItemViewOffset: Int) : ItemViewTransformer(currentItemViewOffset) {
    override fun transformItemView(view: View, position: Float) {
        when {
            position > -1f && position < 0 -> {
                view.scaleX = 1f
                view.scaleY = 1f
                view.translationX = 0f
            }
            position >= 0f && position < 0.5f -> {
                view.scaleY = 1f - 0.2f * position / 0.5f
                view.scaleX = 1f - 0.2f * position / 0.5f
                view.translationX = view.width * 0.4f - view.width * 0.4f * (0.5f - position) / 0.5f
            }
            position >= 0.5f && position < 1f -> {
                view.scaleY = 0.8f
                view.scaleX = 0.8f
                view.translationX = view.width * (0.9f - position)
            }
            position >= 1f && position < 2f -> {
                view.scaleY = 0.8f
                view.scaleX = 0.8f
                view.translationX = -view.width * 0.1f
            }
        }
    }
}