package com.saad.invitationmaker.core.extensions

import android.content.Context
import android.graphics.Color
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

//SnackBar
fun Context.showSnackBarMessage(view: View?, message: CharSequence) {
    val snackBar: Snackbar? = view?.let { currentView ->
        Snackbar.make(currentView, message, Constants.SNACK_BAR_DURATION)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.black))
            .setTextColor(Color.WHITE)
    }
    snackBar?.show()
}