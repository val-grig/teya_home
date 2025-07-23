package com.grigoryev.teya_home.core.app.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface PreferencesRepository {
    fun getAppRating(): Int
    fun setAppRating(rating: Int)
    fun shouldShowRatingRequest(): Boolean
}

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, 
        Context.MODE_PRIVATE
    )
    
    override fun getAppRating(): Int {
        return prefs.getInt(KEY_APP_RATING, 0)
    }
    
    override fun setAppRating(rating: Int) {
        prefs.edit().putInt(KEY_APP_RATING, rating).apply()
    }
    
    override fun shouldShowRatingRequest(): Boolean {
        return getAppRating() != 5
    }
    
    companion object {
        private const val PREFERENCES_NAME = "app_preferences"
        private const val KEY_APP_RATING = "app_rating"
    }
} 