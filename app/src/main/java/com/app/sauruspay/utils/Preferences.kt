package com.app.sauruspay.utils

import android.app.Activity
import android.content.Context
import com.app.sauruspay.model.RequestBody
import com.app.sauruspay.ui.LoginActivity

object Preferences {
    fun saveUserCredentials(username: String, aplicacaoId: String, activity:Activity) {
        val sharedPreferences = activity.getSharedPreferences("SaurusPay", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("aplicacaoId", aplicacaoId)
        editor.apply()
    }

    fun verifyCrendentials(activity: Activity): Boolean {
        val sharedPreferences = activity.getSharedPreferences("SaurusPay", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val aplicacaoId = sharedPreferences.getString("aplicacaoId", null)
        return !(username.isNullOrEmpty() || aplicacaoId.isNullOrEmpty())
    }

    fun recupereCredentials(activity: Activity): RequestBody? {
        val sharedPreferences = activity.getSharedPreferences("SaurusPay", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val aplicacaoId = sharedPreferences.getString("aplicacaoId", null)

        if (username.isNullOrEmpty() || aplicacaoId.isNullOrEmpty()) {
            return null
        }
        return RequestBody(aplicacaoId, username)
    }

    fun recupereUser(activity: Activity) : String?{
        val sharedPreferences = activity.getSharedPreferences("SaurusPay", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        return username
    }

    fun logout(activity: Activity){
        val sharedPreferences = activity.getSharedPreferences("SaurusPay", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", "")
        editor.putString("aplicacaoId", "aplicacaoId")
        editor.apply()
        Navigation.startActivity(LoginActivity::class.java,activity)
    }
}