package com.schoolkeepa.dust.data.repository

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.classsroom.GetClassroomStatusDto
import com.schoolkeepa.dust.data.model.email.FindEmailDto
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.data.model.surveydata.SurveyDataDto
import com.schoolkeepa.dust.data.model.teacher.GetUserProfileDto
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.data.network.DustApi
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.data.response.post.classroom.ClassStatusRes
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import com.schoolkeepa.dust.di.AppModule
import com.schoolkeepa.dust.domain.repository.DustRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DustRepositoryImpl @Inject constructor(
    @AppModule.DustRetrofit private val dustApi: DustApi,
) : DustRepository {

    override suspend fun getSurveyData(type: String): Resource<SurveyDataDto> {
        val response = try {
            dustApi.getSurveyData(type)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun getWeather(lat: String, lng: String, date: String): Resource<WeatherDto> {
        val response = try {
            dustApi.getWeather(lat, lng, date)
        } catch (e: Exception) {
            println(e.message.toString() + "@@@@@")
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun setSurveyAnswer(surveySetRes: SurveySetRes): Resource<String> {
        println("@@@@@@surveySetRes" + surveySetRes.toString())
        val response = try {
            dustApi.setSurveyAnswer(surveySetRes)
        } catch (e: Exception) {
            println(e.message.toString() + " setSurveyAnswer Error @@@@@")
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun setClassroomStatus(classStatusRes: ClassStatusRes): Resource<String> {
        val response = try {

            dustApi.setClassroomStatus(classStatusRes)
        } catch (e: Exception) {
            println(e.message.toString() + "@@@@@")
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    override suspend fun getFineStatus(locationCode: String): Resource<FineStatusDto> {
        val response = try {
            dustApi.getFineStatus(locationCode)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "getFineStatus@@@@@")
        }
        return Resource.Success(response)
    }

    override suspend fun getClassFineStatus(
        schoolCode: String,
        grade: String,
        classNum: String
    ): Resource<GetClassroomStatusDto> {
        val response = try {
            dustApi.getClassroomStatus(schoolCode, grade, classNum)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "getClassFineStatus@@@@")
        }
        return Resource.Success(response)
    }

    override suspend fun setUserProfile(userProfileRes: UserProfileRes): Resource<String> {
        val response = try {
            dustApi.setUserProfile(userProfileRes)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "getClassFineStatus@@@@")
        }
        return Resource.Success(response)
    }

    override suspend fun getUserProfile(uid: String): Resource<GetUserProfileDto> {
        val response = try {
            dustApi.getUserProfile(uid)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "GetUserProfileDto@@@@")
        }
        return Resource.Success(response)
    }

    override suspend fun getFindEmail(name: String, schoolName: String): Resource<FindEmailDto> {
        val response = try {
            dustApi.getFindEmail(name, schoolName)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "getFindEmail@@@@")
        }
        return Resource.Success(response)
    }

    override suspend fun getSurveyAnswerByXls(
        schoolCode: String,
        grade: String,
        classNum: String
    ): Resource<ResponseBody> {
        val response = try {
            dustApi.getSurveyAnswerByXls(schoolCode, grade, classNum)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString() + "getFindEmail@@@@")
        }
        return Resource.Success(response)
    }


}