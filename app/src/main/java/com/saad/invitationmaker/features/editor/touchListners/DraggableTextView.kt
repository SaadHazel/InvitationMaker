package com.saad.invitationmaker.features.editor.touchListners

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.core.extensions.vibratePhone
import com.saad.invitationmaker.features.editor.callbacks.UpdateTouchListenerCallback


class DraggableTextView(
    context: Context,
    x: Float,
    y: Float,
    text: String,
    val currentPosition: (x: Float, y: Float) -> Unit,
    private val onItemClick: (Boolean) -> Unit,
    val currentView: (view: View, isSelected: Boolean) -> Unit,
    private val callback: UpdateTouchListenerCallback,

    ) :
    AppCompatTextView(context) {
    init {
        // Set TextView properties
        layoutParams = ViewGroup.LayoutParams(
            200,
            200
        )
        this.x = x
        this.y = y
        this.text = text
        gravity = Gravity.CENTER_VERTICAL
        gravity = Gravity.CENTER
        background = null
        setTextColor(Color.WHITE)
        setPadding(5)
    }

    fun enableDragAndDrop(
        topLeftIcon: View,
        topRightIcon: View,
        bottomRightIcon: View,
        bottomLeftIcon: View,
//        bottomCenterIcon: View,
    ) {

        setOnTouchListener(object : OnTouchListener {
            private var lastX: Float = 0f
            private var lastY: Float = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                val allParent = view.parent as? ViewGroup ?: return false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        resetBackgroundForAllViews(allParent)
                        setBackgroundResource(R.drawable.rounded_border_tv)
                        currentView(view, true)
                        lastX = event.rawX
                        lastY = event.rawY
                        hideIcons(
                            topLeftIcon,
                            topRightIcon,
                            bottomRightIcon,
                            bottomLeftIcon,
//                            bottomCenterIcon
                        )
                        this@DraggableTextView.vibratePhone()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        onItemClick(true)
                        hideIcons(
                            topLeftIcon,
                            topRightIcon,
                            bottomRightIcon,
                            bottomLeftIcon,
//                            bottomCenterIcon
                        )
                        val deltaX = event.rawX - lastX
                        val deltaY = event.rawY - lastY

                        x += deltaX
                        y += deltaY

                        lastX = event.rawX
                        lastY = event.rawY
                    }

                    MotionEvent.ACTION_UP -> {
                        currentPosition(x, y)
                        callback.onDrag(view)
                        attachTo(
                            this@DraggableTextView,
                            topLeftIcon,
                            topRightIcon,
                            bottomRightIcon,
                            bottomLeftIcon,
//                            bottomCenterIcon
                        )
                    }
                }
                return true
            }
        })
    }

    fun attachTo(
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

        Utils.log("CurrentViewX: $x")
        Utils.log("CurrentViewY: $y")
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

    }

    fun hideIcons(
        topLeftIcon: View,
        topRightIcon: View,
        bottomRightIcon: View,
        bottomLeftIcon: View,
//        bottomCenterIcon: View,

    ) {
        topLeftIcon.visibility = View.GONE
        topRightIcon.visibility = View.GONE
        bottomRightIcon.visibility = View.GONE
        bottomLeftIcon.visibility = View.GONE
//        bottomCenterIcon.visibility = View.GONE
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

