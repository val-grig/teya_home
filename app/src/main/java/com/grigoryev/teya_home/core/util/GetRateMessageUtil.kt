package com.grigoryev.teya_home.core.util

import android.content.Context
import com.grigoryev.teya_home.R
import javax.inject.Inject

class GetRateMessageUtil @Inject constructor(
    private val context: Context
) {
    fun getMessage(rating: Int): String {
        return when (rating) {
            1 -> context.getString(R.string.rate_message_1)
            2 -> context.getString(R.string.rate_message_2)
            3 -> context.getString(R.string.rate_message_3)
            4 -> context.getString(R.string.rate_message_4)
            5 -> context.getString(R.string.rate_message_5)
            else -> context.getString(R.string.rate_message_default)
        }
    }
} 