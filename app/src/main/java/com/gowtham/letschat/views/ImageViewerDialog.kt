package com.gowtham.letschat.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * Simple full screen image viewer (pinch to zoom, tap to close).
 * Replaces the external image viewer library so the build has no extra dependency.
 */
object ImageViewerDialog {

    class Builder<T>(
        private val context: Context?,
        private val images: List<T>,
        private val loader: (ImageView, T) -> Unit
    ) {
        private var dismissListener: (() -> Unit)? = null

        fun withDismissListener(listener: () -> Unit): Builder<T> {
            dismissListener = listener
            return this
        }

        fun show() {
            val ctx = context ?: return
            val item = images.firstOrNull() ?: return
            val dialog = Dialog(ctx, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            val root = FrameLayout(ctx).apply { setBackgroundColor(Color.BLACK) }
            val imageView = ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            root.addView(imageView)
            loader(imageView, item)

            var scale = 1f
            var moved = false
            var lastX = 0f
            var lastY = 0f
            val detector = ScaleGestureDetector(ctx, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(d: ScaleGestureDetector): Boolean {
                    scale = (scale * d.scaleFactor).coerceIn(1f, 5f)
                    imageView.scaleX = scale
                    imageView.scaleY = scale
                    if (scale <= 1f) { imageView.translationX = 0f; imageView.translationY = 0f }
                    return true
                }
            })
            imageView.setOnTouchListener { _, e ->
                detector.onTouchEvent(e)
                when (e.actionMasked) {
                    MotionEvent.ACTION_DOWN -> { moved = false; lastX = e.x; lastY = e.y }
                    MotionEvent.ACTION_MOVE -> {
                        if (e.pointerCount == 1 && scale > 1f) {
                            imageView.translationX += e.x - lastX
                            imageView.translationY += e.y - lastY
                            moved = true
                        } else if (Math.abs(e.x - lastX) > 10 || Math.abs(e.y - lastY) > 10) moved = true
                        if (e.pointerCount == 1) { lastX = e.x; lastY = e.y }
                    }
                    MotionEvent.ACTION_UP -> if (!moved && scale <= 1.01f) dialog.dismiss()
                }
                true
            }
            dialog.setContentView(root)
            dialog.setOnDismissListener { dismissListener?.invoke() }
            dialog.show()
        }
    }
}
