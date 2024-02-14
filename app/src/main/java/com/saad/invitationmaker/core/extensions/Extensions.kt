package com.saad.invitationmaker.core.extensions

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.constants.Constants

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun ViewStub.deflate(): ViewStub {
    val viewParent = parent

    if (viewParent != null && viewParent is ViewGroup) {
        val viewStub = ViewStub(context).apply {
            inflatedId = this@deflate.inflatedId
            layoutParams = this@deflate.layoutParams
        }
        val index = viewParent.indexOfChild(this)

        viewParent.removeView(this)
        viewParent.addView(viewStub, index)
        return viewStub
    } else {
        throw IllegalStateException("Inflated View has not a parent")
    }
}

fun ViewStub.inflateAndGone() {
    this.inflate()
    this.gone()
}

fun View.vibratePhone() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}

//SnackBar
fun Context.showSnackBarMessage(view: View?, message: CharSequence) {
    val snackBar: Snackbar? = view?.let { currentView ->
        Snackbar.make(currentView, message, Constants.SNACK_BAR_DURATION)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.black))
            .setTextColor(Color.WHITE)
    }
    snackBar?.show()
}