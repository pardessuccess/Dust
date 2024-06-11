package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadSurveyData @Inject constructor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readSurveyData()
    }
}