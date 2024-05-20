package com.app.sauruspay.model

data class InvoiceFinalizePayment(
    val numeroFatura: String,
    val historico: String,
    val valorFatura: Int,
    val pagamentoParcial: Boolean,
    val pessoa: Pessoa,
    val valorTotal: Int,
    val idTransacao: String,
    val nsu: String,
    val codAut: String,
    val codControle: String,
    val dRetorno: String,
    val numCartao: String,
    val bandeira: String,
    val rede: String,
    val adquirente: String,
    val valorPagamento: Int,
    val tipoPagamento: Int,
    val qtdeParcelas: Int,
    val dTransacao: String,
    val status: Int,
    val msgRetorno: String,
    val arqRetorno: String
)
