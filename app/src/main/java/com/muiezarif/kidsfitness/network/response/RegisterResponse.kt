package com.muiezarif.kidsfitness.network.response

data class RegisterResponse(
    val `data`: RegisterData,
    val message: String,
    val status: Boolean
)

data class RegisterData(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val token: String,
    val username: String
)