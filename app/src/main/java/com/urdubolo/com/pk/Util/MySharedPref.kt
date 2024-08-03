package com.urdubolo.com.pk.Util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.urdubolo.com.pk.Model.UserData

object MySharedPref {
    private const val PREF_NAME = "MySharedPreferences"
    private const val USER_DATA_KEY = "userData"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val gson = Gson()

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveUserModel(userData: UserData) {
        val json = gson.toJson(userData)
        editor.putString(USER_DATA_KEY, json)
        editor.apply()
    }

    fun getUserModel(): UserData? {
        val json = sharedPreferences.getString(USER_DATA_KEY, null)
        return if (json != null) {
            gson.fromJson(json, UserData::class.java)
        } else {
            null
        }
    }



    fun saveUserLoggedIn()
    {
        editor.putBoolean("IsLoggedIn",true)
        editor.apply()
    }
    fun IsUserLoggedIn():Boolean
    {
        return sharedPreferences.getBoolean("IsLoggedIn",false)
    }

}
