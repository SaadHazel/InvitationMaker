package com.saad.invitationmaker.features.editor.touchListners

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.core.extensions.vibratePhone


class DraggableImageView(context: Context, x: Float, y: Float, url: String) :
    AppCompatImageView(context) {


    init {

        layoutParams = ViewGroup.LayoutParams(
            200,
            200
        )
        z = 1f
        this.x = x
        this.y = y
        Utils.log(url)
        Glide.with(context).load(url).centerInside().into(this)
        background = null
        scaleType = ScaleType.CENTER_CROP
    }

    fun enableDragAndDrop(
        topLeftIcon: View,
        topRightIcon: View,
        bottomRightIcon: View,
        bottomLeftIcon: View,
//        bottomCenterIcon: View,
    ) {
        setOnTouchListener(object : View.OnTouchListener {
            private var lastX: Float = 0f
            private var lastY: Float = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                val allParent = view.parent as? ViewGroup ?: return false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        resetBackgroundForAllViews(allParent)
                        setBackgroundResource(R.drawable.rounded_border_tv)
                        hideIcons(
                            topLeftIcon,
                            topRightIcon,
                            bottomRightIcon,
                            bottomLeftIcon,
//                            bottomCenterIcon
                        )
                        lastX = event.rawX
                        lastY = event.rawY
                        this@DraggableImageView.vibratePhone()
                    }

                    MotionEvent.ACTION_MOVE -> {
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
                        attachTo(
                            this@DraggableImageView,
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
        imageView: DraggableImageView,
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
            imageView,
            topLeftIcon,
            topRightIcon,
            bottomRightIcon,
            bottomLeftIcon,
//            bottomCenterIcon
        )
        imageView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateCornerIconsPositionRelativeTo(
                imageView, topLeftIcon, topRightIcon, bottomRightIcon,
                bottomLeftIcon,
//                bottomCenterIcon
            )
        }
    }

    private fun updateCornerIconsPositionRelativeTo(
        imageView: DraggableImageView,
        topLeftIcon: View,
        topRightIcon: View,
        bottomRightIcon: View,
        bottomLeftIcon: View,
//        bottomCenterIcon: View,
    ) {

        val x = imageView.x
        val y = imageView.y

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

    private fun resetBackgroundForAllViews(viewGroup: ViewGroup) {
        for (index in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            resetBackground(child)
        }
    }

    private fun resetBackground(v: View) {
        v.background = null
    }

}

