package com.muiezarif.kidsfitness.network.response

data class SubscriptionResponse(
    val message: SubscriptionMessage,
    val result: SubscriptionResult,
    val status: Boolean
)

data class SubscriptionMessage(
    val success: List<String>,
    val order_id: List<String>,
    val product_id: List<String>,
    val purchase_time: List<String>,
    val purchase_token: List<String>
)

data class SubscriptionResult(
    val id: Int,
    val order_id: String,
    val product_id: String,
    val purchase_time: String,
    val purchase_token: String
)