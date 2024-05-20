package com.app.sauruspay.model

import java.io.Serializable

data class LoginResponse(val credenciais: List<Credencial>) : Serializable
