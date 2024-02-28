package com.saad.invitationmaker.features.editor.touchListners

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.features.editor.callbacks.UpdateTouchListenerCallback


class DraggableTextView(
    context: Context,
    x: Float,
    y: Float,
    val viewId: Int,
    val width: String,
    val height: String,
    val color: String,
    val fontSize: String,
    val textStyle: String,
    val font: String,
    val alignment: String,
    val rotationX: String?,
    val rotationY: String?,
    val dRotationX: String?,
    val dRotationY: String?,
    val shadowAngleX: String?,
    val shadowAngleY: String?,
    val shadowBlur: String?,
    val shadowColor: String?,
    val viewType: String?,
    val lineHeight: String?,
    val priority: String?,
    val parent: ConstraintLayout,
    text: String,

    val letterSpacing: String,
    val rotation: String?,
    val opacity: String?,
    val currentPosition: (x: Float, y: Float) -> Unit,
    private val onItemClick: (Boolean) -> Unit,
    val currentView: (viewId: String, view: View, isSelected: Boolean) -> Unit,
    private val callback: UpdateTouchListenerCallback,

    ) :
    AppCompatTextView(context) {
    init {
//        Utils.log("Draggable Text view: $text")
        // Set TextView properties
        val pxWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            width.toFloat(),
            context.resources.displayMetrics
        )
        val widthInDp = pxToDp(width.toInt(), context)
        log("EDITORACTIVITY", " widthInDpNative $pxWidth || widthInDp $widthInDp")

        val pxHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            height.toFloat(),
            context.resources.displayMetrics
        )
        val heightInDp = pxToDp(height.toInt(), context)
        log("EDITORACTIVITY", " width $width || height $height")
        val fontSizeInSp = pxToSp(fontSize.toFloat(), context)
//        layoutParams = ViewGroup.LayoutParams(
//            pxWidth.toInt(),
//            pxHeight.toInt()
//        )

        when (font) {
            "poppins" -> {
                this.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_regular))
            }

            "cormorantSC" -> {
                this.setTypeface(ResourcesCompat.getFont(context, R.font.cormorantsc_regular))
            }

            "raleway" -> {
                this.setTypeface(ResourcesCompat.getFont(context, R.font.raleway_regular))
            }

            "anthonyHunter" -> {
                setTextColor(Color.parseColor("#29637B"))
                this.setTypeface(ResourcesCompat.getFont(context, R.font.anthony_hunter))
            }

            "greatVibes" -> {
                this.setTypeface(ResourcesCompat.getFont(context, R.font.greatvibes_regular))
            }
        }
        this.x = x
        this.y = y
        this.text = text
        gravity = Gravity.CENTER_VERTICAL
        gravity = Gravity.CENTER
        background = null
        this.textSize = fontSizeInSp

        setTextColor(Color.parseColor(color))
        setPadding(5)
        if (textStyle == "bold") {
            this.setTypeface(null, Typeface.BOLD)
        } else if (textStyle == "regular") {
            this.setTypeface(null, Typeface.NORMAL)
        }
    }

    private val MIN_ZOOM = 0.5f
    private val MAX_ZOOM = 1.8f
    fun enableDragAndDrop() {

        setOnTouchListener(object : OnTouchListener {
            private var lastX: Float = 0f
            private var lastY: Float = 0f
            private var d = 0f
            private var scaleFactor = 1f
            private var initialDistance = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
//                val allParent = view.parent as? ViewGroup ?: return false
                val allParent = parent
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onItemClick(true)
                        resetBackgroundForAllViews(allParent)
                        setBackgroundResource(R.drawable.rounded_border_tv)
                        currentView(viewId.toString(), view, true)
                        lastX = event.rawX
                        lastY = event.rawY
                        /*    callback.onGettingAllTheValues(
                                viewId = viewId.toString(),
    //                            viewData = text,
                                alignment = alignment,
                                opacity = opacity,
                                rotation = rotation,
                                rotationX = rotationX,
                                rotationY = rotationY,
                                dRotationX = dRotationX,
                                dRotationY = dRotationY,
                                shadowAngleX = shadowAngleX,
                                shadowAngleY = shadowAngleY,
                                shadowBlur = shadowBlur,
                                shadowColor = shadowColor,
                                viewType = viewType,
                                fontSize = fontSize,
                                font = font,
                                letterSpacing = letterSpacing,
                                lineHeight = lineHeight,
                                textStyle = textStyle,
                                color = color,
                                width = width,
                                height = height,
                                priority = priority,
                            )*/
                        /*      hideIcons(
                                  topLeftIcon,
                                  topRightIcon,
                                  bottomRightIcon,
                                  bottomLeftIcon,
      //                            bottomCenterIcon
                              )*/
//                        this@DraggableTextView.vibratePhone()
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

                    MotionEvent.ACTION_POINTER_UP -> {
                        initialDistance = 0f
                    }

                    MotionEvent.ACTION_UP -> {
                        currentPosition(x, y)
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
    /*    fun attachTo(
            textView: DraggableTextView,
            topLeftIcon: View,
            topRightIcon: View,
            bottomRightIcon: View,
            bottomLeftIcon: View,
    //        bottomCenterIcon: View,

        ) {
            topLeftIcon.visibility = View.VISIBLE
            topRightIcon.visibility = View.VISIBLE
            bottomRightIcon.visibility = View.VISIBLE
            bottomLeftIcon.visibility = View.VISIBLE
    //        bottomCenterIcon.visibility = View.VISIBLE

            updateCornerIconsPositionRelativeTo(
                textView,
                topLeftIcon,
                topRightIcon,
                bottomRightIcon,
                bottomLeftIcon,
    //            bottomCenterIcon
            )
            textView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

                updateCornerIconsPositionRelativeTo(
                    textView, topLeftIcon, topRightIcon, bottomRightIcon,
                    bottomLeftIcon,
    //                bottomCenterIcon
                )
            }
        }

        private fun updateCornerIconsPositionRelativeTo(
            textView: DraggableTextView,
            topLeftIcon: View,
            topRightIcon: View,
            bottomRightIcon: View,
            bottomLeftIcon: View,
    //        bottomCenterIcon: View,
        ) {
            val x = textView.x
            val y = textView.y


            //TopLeft
            topLeftIcon.x = x - (topLeftIcon.width / 2)
            topLeftIcon.y = y - (topLeftIcon.height / 2)

            //TopRight
            topRightIcon.x = x + width - (topRightIcon.width / 2)
            topRightIcon.y = y - (topRightIcon.height / 2)

            //BottomRight
            bottomRightIcon.x = x + width - (bottomRightIcon.width / 2)
            bottomRightIcon.y = y + height - (bottomRightIcon.height / 2)
            //BottomLeft
            bottomLeftIcon.x = x - (bottomLeftIcon.width / 2)
            bottomLeftIcon.y = y + height - (bottomLeftIcon.height / 2)
            //BottomCenter
    //        bottomCenterIcon.x = x + width / 2 - (bottomCenterIcon.width / 2)
    //        bottomCenterIcon.y = y + height - (bottomCenterIcon.height / 2)

        }*/

    /*   fun hideIcons(
           topLeftIcon: View,
           topRightIcon: View,
           bottomRightIcon: View,
           bottomLeftIcon: View,
   //        bottomCenterIcon: View,

       ) {
           *//*  topLeftIcon.visibility = View.GONE
          topRightIcon.visibility = View.GONE
          bottomRightIcon.visibility = View.GONE
          bottomLeftIcon.visibility = View.GONE*//*
//        bottomCenterIcon.visibility = View.GONE
    }*/

    fun resetBackgroundForAllViews(viewGroup: ViewGroup) {
        for (index in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            resetBackground(child)
        }
    }

    private fun resetBackground(v: View) {
        v.background = null
    }

    private fun pxToDp(px: Int, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    private fun pxToSp(px: Float, context: Context): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }
}


