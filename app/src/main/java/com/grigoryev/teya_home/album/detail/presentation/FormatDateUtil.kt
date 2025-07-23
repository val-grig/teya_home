package com.grigoryev.teya_home.album.detail.presentation

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatDateUtil @Inject constructor(
    private val context: Context
) {
    fun invoke(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        return try {
            val inputFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
            )

            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                    dateFormat.isLenient = false
                    parsedDate = dateFormat.parse(dateString)
                    break
                } catch (e: Exception) {
                    continue
                }
            }

            if (parsedDate != null) {
                val outputFormat = SimpleDateFormat("MMMM d, yyyy", context.resources.configuration.locales[0])
                outputFormat.format(parsedDate)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
}