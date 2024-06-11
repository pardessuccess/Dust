package com.schoolkeepa.dust.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth?
) : AuthRepository {
    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(value = Resource.Loading())
            val result =
                firebaseAuth?.signInWithEmailAndPassword(email, password)!!.addOnSuccessListener {
                    println("@@@@LOGIN SUCCESSFUL")
                }.addOnFailureListener {
                    println("@@@@LOGIN FAILED")
                }.await()
            emit(value = Resource.Success(data = result))
        }.catch {
            emit(value = Resource.Error(it.message.toString()))
        }
    }

    override fun autoLogin(): FirebaseAuth? {
        return firebaseAuth
    }

    override fun sendPasswordResetEmail(email: String): Flow<Resource<Void>> {
        return flow {
            emit(value = Resource.Loading())
            val result = firebaseAuth?.sendPasswordResetEmail(email)?.addOnFailureListener {
            }!!.addOnCompleteListener {
                println("@@@@@sendPasswordResetEmail")
            }.await()
            emit(value = Resource.Success(data = result))
        }.catch {
            emit(value = Resource.Error(data = null, message = it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(value = Resource.Loading())
            val result = firebaseAuth?.createUserWithEmailAndPassword(email, password)!!.await()
            emit(value = Resource.Success(data = result))
        }.catch {
            emit(value = Resource.Error(it.message.toString()))
        }
    }
}