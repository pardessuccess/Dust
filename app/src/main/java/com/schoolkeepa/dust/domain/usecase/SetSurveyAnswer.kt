package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class SetSurveyAnswer @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(surveySetData: SurveySetRes): Resource<String> {
        return dustRepository.setSurveyAnswer(surveySetData)
    }
}