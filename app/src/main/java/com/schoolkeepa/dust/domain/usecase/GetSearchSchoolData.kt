package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.domain.repository.NeisRepository
import javax.inject.Inject

class GetSearchSchoolData @Inject constructor(
    private val neisRepository: NeisRepository
) {
    suspend operator fun invoke(text: String): Resource<SchoolListDto> {
        return neisRepository.searchSchool(text)
    }
}