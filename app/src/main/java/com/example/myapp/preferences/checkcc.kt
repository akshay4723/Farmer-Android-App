package com.example.myapp.preferences

import android.content.Context
import android.util.Log

class checkcc(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("checkcc_prefs", Context.MODE_PRIVATE)

    var want: String
        get() = sharedPreferences.getString("want", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("want", value).apply()
        }

    fun checking(get: String) {
        want = get
    }

    fun letscheck(): Int {
        Log.d("want", want)
        return when (want) {
            "client" -> 89
            else -> 98
        }
    }
}


