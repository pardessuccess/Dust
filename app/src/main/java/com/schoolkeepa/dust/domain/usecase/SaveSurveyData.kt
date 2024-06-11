package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.domain.manager.LocalUserManager
import javax.inject.Inject

class SaveSurveyData @Inject constructor(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(surveyData: String) {
        localUserManager.saveSurveyData(surveyData)
    }
}