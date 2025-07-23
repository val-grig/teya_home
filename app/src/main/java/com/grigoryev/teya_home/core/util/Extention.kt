package com.grigoryev.teya_home.core.util

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.math.ceil

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    ViewBindingDelegate(this, viewBindingFactory)

fun Int.dpToPx(): Int {
    return ceil(this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.pxToDp(): Int {
    return ceil(this / Resources.getSystem().displayMetrics.density).toInt()
}

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    factory: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToRoot: Boolean = true
) = factory(LayoutInflater.from(context), this, attachToRoot)
