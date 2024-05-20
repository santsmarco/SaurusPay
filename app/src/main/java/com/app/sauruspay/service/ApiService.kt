package com.app.sauruspay.service

import com.app.sauruspay.model.FaturaResponse
import com.app.sauruspay.model.InvoiceFinalizePayment
import com.app.sauruspay.model.LoginRequest
import com.app.sauruspay.model.LoginResponse
import com.app.sauruspay.model.ResponsePayment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("v2/auth")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("v2/financeiro/faturas?DueForUser=false")
    fun getInvoices(@Header("aplicacaoId") aplicacaoId: String, @Header("username") usuario: String): Call<FaturaResponse>

    @POST("v2/financeiro/retorno")
    fun sendTransactionData(@Header("aplicacaoId") aplicacaoId: String, @Header("username") usuario: String, @Body requestBody: InvoiceFinalizePayment): Call<ResponsePayment>
}
