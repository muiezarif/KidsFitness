package com.muiezarif.kidsfitness.network.response

data class UpdateUserResult(
    val address: String,
    val email: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val phone_number: String,
    val is_premium:Boolean,
    val username: String
)

data class UpdateUserResponse(
    val message: UpdateUserMessage,
    val result: UpdateUserResult,
    val status: Boolean
)

data class UpdateUserMessage(
    val success: List<String>,
    val address: List<String>,
    val email: List<String>)
