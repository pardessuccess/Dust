package com.schoolkeepa.dust.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.schoolkeepa.dust.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun autoLogin(): FirebaseAuth?

    fun sendPasswordResetEmail(email: String): Flow<Resource<Void>>

}