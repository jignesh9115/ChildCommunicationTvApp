package com.jv.parentapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PrefManager(private val context: Context, pref_name: String?) {
    val sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    init {
        Log.i("PrefManager", "inside PrefManager initialize")
        sp = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE)
    }

    fun setPrefString(key: String?, value: String?) {
        editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPrefString(key: String): String {
        return sp.getString(key, "").toString()
    }
}