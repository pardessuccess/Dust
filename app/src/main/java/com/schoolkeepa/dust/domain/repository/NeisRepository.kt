package com.schoolkeepa.dust.domain.repository

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto

interface NeisRepository {

    suspend fun getSchoolList(): Resource<SchoolListDto>
    suspend fun searchSchool(text: String): Resource<SchoolListDto>

}