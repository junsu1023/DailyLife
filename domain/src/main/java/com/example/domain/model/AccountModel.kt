package com.example.domain.model

data class AccountModel(
    val id: Long? = null,
    val kind: String,
    val date: String,
    val cost: Long,
    val classification: String,
    val cardCompany: String,
    val content: String
)