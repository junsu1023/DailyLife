package com.example.domain.model

data class AccountModel(
    val id: Long? = null,
    val ym: String,
    val kind: String,
    val date: String,
    val cost: Long,
    val classification: String? = null,
    val content: String? = null
)