package com.app.sauruspay.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.app.sauruspay.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    @RequiresApi(Build.VERSION_CODES.M)
    fun getInstance(context: Context): ApiService {
        val BASE_URL = "https://api-pedido-erp-gateway-prod.saurus.net.br/api/"
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == false) {
            Toast.makeText(context, "Wifi não habilitado", Toast.LENGTH_SHORT).show()
            throw IllegalStateException("Conexão Wi-Fi necessária para acessar este serviço.")
        } else {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}