package com.app.sauruspay.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

object Util {
    fun showDialog(context: Context, title: String, message: String, nameButton: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(nameButton, null)
            .show()
    }

    fun showDialogFunction(context: Context, title: String, message: String, nameButton: String, onOkClicked: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(nameButton) { _, _ ->
                onOkClicked()
            }
            .show()
    }

    fun verifyWifi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val isWifiEnabled = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

        if (!isWifiEnabled) {
            Toast.makeText(context, "Wi-Fi não está habilitado", Toast.LENGTH_SHORT).show()
        }
        return isWifiEnabled
    }
}