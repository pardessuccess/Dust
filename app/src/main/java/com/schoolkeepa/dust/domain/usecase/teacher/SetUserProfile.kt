package com.schoolkeepa.dust.domain.usecase.teacher

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class SetUserProfile @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(userProfileRes: UserProfileRes): Resource<String> {
        return dustRepository.setUserProfile(userProfileRes = userProfileRes)
    }
}