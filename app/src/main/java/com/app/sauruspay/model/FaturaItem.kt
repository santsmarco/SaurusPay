package com.app.sauruspay.model

import java.io.Serializable

data class FaturaItem(val numeroFatura: String,
                      val historico: String,
                      val valorFatura: Double,
                      val pagamentoParcial: Boolean,
                      val pessoa: Pessoa,
                      val pagamento: List<Pagamento>?,
                      val origem: List<Origem>) : Serializable
