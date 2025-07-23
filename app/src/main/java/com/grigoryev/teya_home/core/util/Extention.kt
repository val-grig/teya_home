package com.grigoryev.teya_home.core.util

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

inline fun <T> Flow<T>.launchAndCollectLatestIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collectLatest {
            action(it)
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectLatestIn(
    coroutineScope: CoroutineScope,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = coroutineScope.launch {
    collectLatest {
        action(it)
    }
}
