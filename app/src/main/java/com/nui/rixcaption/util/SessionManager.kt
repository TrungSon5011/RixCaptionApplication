package com.nui.rixcaption.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SessionManager {

    private lateinit var preferences: SharedPreferences

    fun setFirstTime(activity: Activity, isFirstTime: Boolean) {
        preferences = activity.getSharedPreferences("storage_permission", Context.MODE_PRIVATE)
        preferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }

    fun isFirstTime(activity: Activity): Boolean {
        preferences = activity.getSharedPreferences("storage_permission", Context.MODE_PRIVATE)
        return preferences.getBoolean("isFirstTime", true)
    }
}