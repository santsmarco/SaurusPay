package com.app.sauruspay.model

import java.io.Serializable

data class LoginRequest(val aplicacaoId: String, val usuario: String, val senha: String) : Serializable
