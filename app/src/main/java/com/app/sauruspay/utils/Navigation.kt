package com.app.sauruspay.utils

import android.app.Activity
import android.content.Intent
import com.app.sauruspay.model.FaturaItem

object Navigation {

    fun startActivity(directClass:Class<*>, activity: Activity){
        val it = Intent(activity, directClass)
        activity.startActivity(it)
        activity.finish()
    }

    fun startActivityPayment(directClass: Class<*>, activity: Activity, faturaItem: FaturaItem) {
        val it = Intent(activity, directClass).apply {
            putExtra("faturaItem", faturaItem)
        }
        activity.startActivity(it)
    }
}