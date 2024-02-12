package com.saad.invitationmaker.core.extensions

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
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