package com.schoolkeepa.dust.domain.usecase.local

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.classsroom.GetClassroomStatusDto
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetClassFineStatus @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(
        schoolCode: String,
        grade: String,
        classNum: String
    ): Resource<GetClassroomStatusDto> {
        return dustRepository.getClassFineStatus(schoolCode, grade, classNum)
    }
}