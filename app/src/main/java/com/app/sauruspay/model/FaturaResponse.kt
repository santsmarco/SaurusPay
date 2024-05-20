package com.app.sauruspay.model

import java.io.Serializable

data class FaturaResponse(
    val list: List<FaturaItem>,
    val totalResults: Int,
    val totalPages: Int,
    val pageIndex: Int,
    val pageSize: Int
) : Serializable
