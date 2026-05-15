package com.example.rentalready // KEEP THIS LINE! Yours might say something slightly different.

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("EV_Prefs", Context.MODE_PRIVATE)

    fun savePreferredCategory(category: String) {
        prefs.edit().putString("PREF_CATEGORY", category).apply()
    }

    fun getPreferredCategory(): String {
        return prefs.getString("PREF_CATEGORY", "All") ?: "All"
    }
}