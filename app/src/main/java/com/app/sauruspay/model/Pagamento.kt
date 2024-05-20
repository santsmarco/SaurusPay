package com.app.sauruspay.model

import java.io.Serializable

data class Pagamento(val nome: String,
                     val tipoPagamento: Int,
                     val numeroParcelas: Int) :Serializable
