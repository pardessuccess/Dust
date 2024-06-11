package com.schoolkeepa.dust.domain.usecase.xls

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.email.FindEmailDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class GetSurveyAnswerByXls @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(schoolCode: String, grade: String, classNum: String): Resource<ResponseBody> {
        return dustRepository.getSurveyAnswerByXls(schoolCode, grade, classNum)
    }
}