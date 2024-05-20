package com.app.sauruspay.model

import java.io.Serializable

data class Origem(val origem: String,
                  val numero: String,
                  val infAdic: String) : Serializable
