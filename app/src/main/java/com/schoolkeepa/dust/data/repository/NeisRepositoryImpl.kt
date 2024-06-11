package com.schoolkeepa.dust.data.repository

import com.schoolkeepa.dust.BuildConfig
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.network.NeisApi
import com.schoolkeepa.dust.di.AppModule
import com.schoolkeepa.dust.domain.repository.NeisRepository
import javax.inject.Inject

class NeisRepositoryImpl @Inject constructor(
    @AppModule.NeisRetrofit private val neisApi: NeisApi
) : NeisRepository {
    override suspend fun getSchoolList(): Resource<SchoolListDto> {
        val response = try {
            neisApi.getSchoolList(
                BuildConfig.NEIS_KEY,
                "json",
                1, 100
            )
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message.toString() + "@@@@@####")
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun searchSchool(text: String): Resource<SchoolListDto> {
        val response = try {
            neisApi.searchSchool(
                BuildConfig.NEIS_KEY,
                "JSON",
                text
            )
        } catch (e: Exception) {
            println(e.message.toString() + "@@@@@####")
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
}