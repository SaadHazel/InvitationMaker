package com.saad.invitationmaker.features.editor.touchListners


import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView


class CornerIconListener(context: Context, iconSize: Int, @DrawableRes iconResource: Int) :
    AppCompatImageView(context) {

    private var corner: Corner = Corner.TOP_RIGHT

    enum class Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER
    }

    init {
        // Set ImageView properties
        z = 200f
        setImageResource(iconResource)
        layoutParams = ViewGroup.LayoutParams(iconSize, iconSize)
    }


    /*    fun attachTo(textView: View, corner: Corner = Corner.BOTTOM_RIGHT) {
            this.corner = corner
            updatePositionRelativeTo(textView)

            textView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                updatePositionRelativeTo(textView)
            }
        }

        private fun updatePositionRelativeTo(view: View) {
            when (corner) {
                Corner.TOP_LEFT -> {
                    x = view.x - (layoutParams.width / 2)
                    y = view.y - (layoutParams.height / 2)
                }

                Corner.TOP_RIGHT -> {
                    x = view.x + view.width - (layoutParams.width / 2)
                    y = view.y - (layoutParams.height / 2)
                }

                Corner.BOTTOM_LEFT -> {
                    x = view.x - (layoutParams.width / 2)
                    y = view.y + view.height - (layoutParams.height / 2)
                }

                Corner.BOTTOM_RIGHT -> {
                    x = view.x + view.width - (layoutParams.width / 2)
                    y = view.y + view.height - (layoutParams.height / 2)
                }

                Corner.BOTTOM_CENTER -> {
                    x = view.x + view.width / 2 - (layoutParams.width / 2)
                    y = view.y + view.height - (layoutParams.height / 2)
                }
                /* Corner.TOP_CENTER -> {
                     x = view.x + view.width / 2 - (layoutParams.width / 2)
                     y = view.y - (layoutParams.height / 2)
                 }

                 Corner.RIGHT_CENTER -> {
                     x = view.x + view.width - (layoutParams.width / 2)
                     y = view.y + view.height / 2 - (layoutParams.height / 2)
                 }

                 Corner.LEFT_CENTER -> {
                     x = view.x - (layoutParams.width / 2)
                     y = view.y + view.height / 2 - (layoutParams.height / 2)
                 }*/

            }
        }
    */
    fun enableResizeOnTouch(
        textView: View,
        minWidth: Int,
        minHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
    ) {

        setOnTouchListener(object : OnTouchListener {
            private var lastX: Float = 0f
            private var lastY: Float = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY

                    }

                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - lastX
                        val deltaY = event.rawY - lastY


                        when (corner) {
                            Corner.TOP_LEFT -> {
                                val newWidth = textView.layoutParams.width - deltaX.toInt()
                                val newHeight = textView.layoutParams.height - deltaY.toInt()

                                if (newWidth >= minWidth && newHeight >= minHeight) {
                                    textView.layoutParams.width = minOf(newWidth, maxWidth)
                                    textView.layoutParams.height = minOf(newHeight, maxHeight)
                                    textView.x += deltaX
                                    textView.y += deltaY
                                }
                            }

                            Corner.TOP_RIGHT -> {
                                val newWidth = textView.layoutParams.width + deltaX.toInt()
                                val newHeight = textView.layoutParams.height - deltaY.toInt()

                                if (newWidth >= minWidth && newHeight >= minHeight) {
                                    textView.layoutParams.width = minOf(newWidth, maxWidth)
                                    textView.layoutParams.height = minOf(newHeight, maxHeight)
                                    textView.y += deltaY
                                }
                            }

                            Corner.BOTTOM_LEFT -> {
                                val newWidth = textView.layoutParams.width - deltaX.toInt()
                                val newHeight = textView.layoutParams.height + deltaY.toInt()

                                if (newWidth >= minWidth && newHeight >= minHeight) {
                                    textView.layoutParams.width = minOf(newWidth, maxWidth)
                                    textView.layoutParams.height = minOf(newHeight, maxHeight)
                                    textView.x += deltaX
                                }
                            }

                            Corner.BOTTOM_RIGHT -> {
                                val newWidth = textView.layoutParams.width + deltaX.toInt()
                                val newHeight = textView.layoutParams.height + deltaY.toInt()

                                if (newWidth >= minWidth && newHeight >= minHeight) {
                                    textView.layoutParams.width = minOf(newWidth, maxWidth)
                                    textView.layoutParams.height = minOf(newHeight, maxHeight)
                                }
                            }

                            Corner.BOTTOM_CENTER -> {
                                val newHeight = textView.layoutParams.height + deltaY.toInt()

                                if (newHeight in minHeight..maxHeight) {
                                    textView.layoutParams.height = minOf(newHeight, maxHeight)
                                }
                            }
                            /*
                                                        Corner.RIGHT_CENTER -> {
                                                            val newWidth = textView.layoutParams.width + deltaX.toInt()

                                                            if (newWidth >= minWidth && newWidth <= maxWidth) {
                                                                textView.layoutParams.width = minOf(newWidth, maxWidth)
                                                            }
                                                        }

                                                        Corner.TOP_CENTER -> {

                                                            val newHeight = textView.layoutParams.height + deltaY.toInt()

                                                            if (newHeight >= minHeight && newHeight <= maxHeight) {
                                                                textView.layoutParams.height = minOf(newHeight, maxHeight)
                                                            }
                                                        }

                                                        Corner.LEFT_CENTER -> {
                                                            val newWidth = textView.layoutParams.width - deltaX.toInt()

                                                            if (newWidth >= minWidth && newWidth <= maxWidth) {
                                                                textView.layoutParams.width = minOf(newWidth, maxWidth)
                                                            }
                                                        }*/
                        }

                        textView.requestLayout()

                        lastX = event.rawX
                        lastY = event.rawY
                    }

                    MotionEvent.ACTION_UP -> {

                    }
                }
                return true
            }
        })
    }
}
