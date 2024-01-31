package com.saad.invitationmaker.core.extensions

import android.os.SystemClock
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


//Handle monkey clicking for fragments
fun debounce(action: (view: View) -> Unit, view: View) {
    if (SystemClock.elapsedRealtime() - LastClickTimeSingleton.lastClickTime < 500L) return
    else action(view)
    LastClickTimeSingleton.lastClickTime = SystemClock.elapsedRealtime()
}

fun View.setClickWithDebounce(action: (view: View) -> Unit) {
    setOnClickListener {
        debounce(action, it)
    }
}


object LastClickTimeSingleton {
    var lastClickTime: Long = 0
}


//Fragment open dialog

fun Fragment.openFragmentDialog(tag: String, fragment: DialogFragment) {
    dismissFragmentDialog(tag)
    fragment.show(childFragmentManager.beginTransaction(), tag)
}


//Fragment dismiss dialog
fun Fragment.dismissFragmentDialog(tag: String) {
    childFragmentManager.apply {
        val fragment = findFragmentByTag(tag)
        if (fragment != null && fragment is DialogFragment) fragment.dismissAllowingStateLoss()
    }
}


//Bottom sheet dialog in fragment
fun Fragment.openBottomSheetFragmentDialog(tag: String, fragment: BottomSheetDialogFragment) {
    dismissBottomSheetFragmentDialog(tag)
    fragment.show(childFragmentManager.beginTransaction(), tag)
}


fun Fragment.dismissBottomSheetFragmentDialog(tag: String) {
    childFragmentManager.apply {
        val fragment = findFragmentByTag(tag)
        if (fragment != null && fragment is DialogFragment) fragment.dismissAllowingStateLoss()
    }
}


//Navigation in fragment
fun Fragment.navigate(
    @IdRes navHost: Int, navDirection: NavDirections, extras: Navigator.Extras? = null,
) {
    val navHostFragment =
        requireActivity().supportFragmentManager.findFragmentById(navHost) as NavHostFragment
    val navController = navHostFragment.navController
    try {
        if (extras == null) navController.navigate(navDirection)
        else navController.navigate(navDirection, extras)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}
