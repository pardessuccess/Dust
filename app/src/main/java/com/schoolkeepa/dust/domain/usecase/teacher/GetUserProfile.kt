package com.schoolkeepa.dust.domain.usecase.teacher

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.teacher.GetUserProfileDto
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetUserProfile @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(uid: String): Resource<GetUserProfileDto> {
        return dustRepository.getUserProfile(uid = uid)
    }
}