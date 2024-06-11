package com.schoolkeepa.dust.presentation.intro


data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: String = "",
    val isError: String? = "",
)
