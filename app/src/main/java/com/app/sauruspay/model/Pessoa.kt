package com.app.sauruspay.model

import java.io.Serializable

data class Pessoa(val cpfCnpj: String,
                  val codigo: String,
                  val nome: String) : Serializable
