package com.muiezarif.kidsfitness.network.response

data class LoginResponse(
    val message: String,
    val result: LoginResult,
    val status: Boolean
)

data class LoginResult(
    val user: LoginUser
)

data class LoginUser(
    val token: String,
    val user: LoginUserX
)

data class LoginUserX(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val username: String,
    val student_city: String,
    val age: Int,
    val is_premium: Boolean,
    val is_student: Boolean
)