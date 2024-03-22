package com.saad.invitationmaker.features.editor.touchListners

import android.content.Context
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.features.editor.callbacks.UpdateTouchListenerCallback


class DraggableImageView(
    context: Context,
    x: Float,
    y: Float,
    imageType: String,
    bitmap: Bitmap?,
    val url: String,
    private val onItemClick: (Boolean) -> Unit,
    private val callback: UpdateTouchListenerCallback,
    val currentView: (view: View, isSelected: Boolean, data: String) -> Unit,
) :
    AppCompatImageView(context) {

    init {

        layoutParams = ViewGroup.LayoutParams(
            300,
            300
        )
        z = 1f
        this.x = x
        this.y = y
        this.setPadding(6)
        if (imageType == "BITMAP") {
            this.setImageBitmap(bitmap)
        } else {
            Glide.with(context).load(url).centerInside().into(this)
        }
        background = null
        scaleType = ScaleType.CENTER_CROP
    }

    private val MIN_ZOOM = 0.5f
    private val MAX_ZOOM = 2f

    fun enableDragAndDrop(

    ) {
        setOnTouchListener(object : View.OnTouchListener {
            private var lastX: Float = 0f
            private var lastY: Float = 0f
            private var d = 0f
            private var scaleFactor = 1f
            private var initialDistance = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                val allParent = view.parent as? ViewGroup ?: return false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onItemClick(true)
                        currentView(view, true, url)
                        resetBackgroundForAllViews(allParent)
                        setBackgroundResource(R.drawable.rounded_border_tv)
                        lastX = event.rawX
                        lastY = event.rawY

                    }

                    MotionEvent.ACTION_MOVE -> {

                        when (event.pointerCount) {
                            1 -> {
                                val deltaX = event.rawX - lastX
                                val deltaY = event.rawY - lastY

                                x += deltaX
                                y += deltaY
                                // Adjust position to stay within parent bounds
                                val maxX = (view.parent as View).width - view.width
                                val maxY = (view.parent as View).height - view.height

                                val clampedX = x.coerceIn(0f, maxX.toFloat())
                                val clampedY = y.coerceIn(0f, maxY.toFloat())

                                view.x = clampedX
                                view.y = clampedY

                                lastX = event.rawX
                                lastY = event.rawY
                            }

                            2 -> {
                                val newDistance = calculateFingerDistance(event)

                                if (initialDistance == 0f) {
                                    initialDistance = newDistance
                                }

                                val scale = newDistance / initialDistance
                                scaleFactor *= scale

                                scaleFactor = scaleFactor.coerceIn(MIN_ZOOM, MAX_ZOOM)

                                // Apply scaling with pivot at the center
                                view.scaleX = scaleFactor
                                view.scaleY = scaleFactor
                                d = rotation(event)
                            }

                            3 -> {
                                val newRot = rotation(event)
                                val deltaRot = newRot - d

                                view.rotation += deltaRot
                            }
                        }

                    }

                    MotionEvent.ACTION_UP -> {
                        callback.onDrag(view)

                    }
                }
                return true
            }
        })
    }

    private fun calculateFingerDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return kotlin.math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun rotation(event: MotionEvent): Float {
        val delta_x = (event.getX(0) - event.getX(1)).toDouble()
        val delta_y = (event.getY(0) - event.getY(1)).toDouble()
        val radians = Math.atan2(delta_y, delta_x)
        return Math.toDegrees(radians).toFloat()
    }

    fun resetBackgroundForAllViews(viewGroup: ViewGroup) {
        for (index in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            resetBackground(child)
        }
    }

    private fun resetBackground(v: View) {
        v.background = null
    }

}

