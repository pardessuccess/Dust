package com.schoolkeepa.dust.domain.usecase.local

import com.schoolkeepa.dust.data.response.post.classroom.ClassStatusRes
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class SetClassroomStatus @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(classStatusRes: ClassStatusRes) {
        dustRepository.setClassroomStatus(classStatusRes)
    }
}