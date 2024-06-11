package com.schoolkeepa.dust.data.network

import com.schoolkeepa.dust.data.model.school.SchoolListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NeisApi {

    @GET("hub/schoolInfo")
    suspend fun getSchoolList(
        @Query("KEY") key: String,
        @Query("Type") type: String,
        @Query("pIndex") pIndex: Int,
        @Query("pSize") pSize: Int
    ): SchoolListDto

    @GET("hub/schoolInfo")
    suspend fun searchSchool(
        @Query("KEY") key: String,
        @Query("Type") type: String,
        @Query("SCHUL_NM") schoolName: String
    ): SchoolListDto
}