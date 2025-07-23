package com.grigoryev.teya_home.core.util

import android.content.res.Resources
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.grigoryev.teya_home.R
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

fun View.setSafeOnClickListener(action: () -> Unit) {
    setOnClickListener(SafeClickListener(onSafeCLick = { action.invoke() }))
}

private class SafeClickListener(
    private val onSafeCLick: (View) -> Unit,
    private var defaultInterval: Int = 1_000
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }

        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun FragmentTransaction.setSlideAnimType(): FragmentTransaction {
    return apply {
        setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right,
        )
    }
}
