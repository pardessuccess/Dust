package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.surveydata.SurveyDataDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetSurveyData @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(type: String): Resource<SurveyDataDto> {
        return dustRepository.getSurveyData(type)
    }
}