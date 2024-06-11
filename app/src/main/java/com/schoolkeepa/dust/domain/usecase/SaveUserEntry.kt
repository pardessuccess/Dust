package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.domain.manager.LocalUserManager
import javax.inject.Inject

class SaveUserEntry @Inject constructor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(grade: String){
        localUserManager.saveUserEntry(grade)
    }
}