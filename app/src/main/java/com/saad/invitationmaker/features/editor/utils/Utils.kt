package com.saad.invitationmaker.features.editor.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import com.bumptech.glide.Glide

class CreateViews(private val context: Context) {

    val backgroundDesigns =
        "https://firebasestorage.googleapis.com/v0/b/invitationmaker-caca8.appspot.com/o/invitation%2Fwedding%2Fbackground%2Fwedding_1.png?alt=media&token=5f763be9-be07-4723-b9a3-7869e30d0117"
    val viewImage =
        "https://firebasestorage.googleapis.com/v0/b/fir-58baf.appspot.com/o/wedding_cards%2Fbackgrounds%2Fbackground.png?alt=media&token=726ca54e-c028-4e79-9a98-73fa18e9f619"

    fun addNewTextView(
        text: String,
        x: Float = 100f,
        y: Float = 300f,
        textSize: Float = 26f,
        color: String = "#FFFFFF",
        parentContainer: ConstraintLayout,
    ) {
        val newTextView = TextView(context)
        newTextView.text = text
        newTextView.textSize = textSize
        newTextView.gravity = Gravity.CENTER
        newTextView.setTextColor(Color.BLACK)

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newTextView.x = x // X-coordinate
        newTextView.y = y // Y-coordinate
        newTextView.setTextColor(Color.parseColor(color))
        newTextView.layoutParams = layoutParams

        parentContainer.addView(newTextView)
        newTextView.setPadding(20)
    }

    fun addNewImageView(
        imgUrl: String = "",
        x: Float = 200f,
        y: Float = 300f,
        imgSize: Float = 300f,
        width: Int = 300,
        height: Int = 300,
        parentContainer: ConstraintLayout,
    ) {

        val newImageView = ImageView(context)
        newImageView.id = View.generateViewId()

        Glide.with(context).load(imgUrl).centerInside().into(newImageView)

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.width = width
        layoutParams.height = height

        newImageView.x = x
        newImageView.y = y

        newImageView.layoutParams = layoutParams
        newImageView.scaleType = ImageView.ScaleType.MATRIX
//        newImageView.setOnTouchListener(updateTouchListener)
        parentContainer.addView(newImageView)
    }

}