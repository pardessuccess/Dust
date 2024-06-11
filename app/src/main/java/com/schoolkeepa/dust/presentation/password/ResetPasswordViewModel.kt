package com.schoolkeepa.dust.presentation.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.domain.repository.AuthRepository
import com.schoolkeepa.dust.presentation.intro.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private var _registerState = MutableStateFlow<RegisterState>(value = RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    suspend fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        authRepository.sendPasswordResetEmail(email).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _registerState.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    _registerState.update { it.copy(isError = result.message) }
                }

                is Resource.Success -> {
                    println(result.message.toString() + "result.message.toString()")
                    _registerState.update { it.copy(isSuccess = "Reset Password Successful!") }
                }
            }
        }
    }

}