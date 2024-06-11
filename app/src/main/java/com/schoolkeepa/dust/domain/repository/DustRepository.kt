package com.schoolkeepa.dust.domain.repository

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.classsroom.GetClassroomStatusDto
import com.schoolkeepa.dust.data.model.email.FindEmailDto
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.data.model.surveydata.SurveyDataDto
import com.schoolkeepa.dust.data.model.teacher.GetUserProfileDto
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.data.response.post.classroom.ClassStatusRes
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import okhttp3.ResponseBody

interface DustRepository {

    suspend fun getSurveyData(type: String): Resource<SurveyDataDto>

    suspend fun getWeather(lat: String, lng: String, date: String): Resource<WeatherDto>

    suspend fun setSurveyAnswer(surveySetRes: SurveySetRes): Resource<String>

    suspend fun setClassroomStatus(classStatusRes: ClassStatusRes): Resource<String>

    suspend fun getFineStatus(locationCode: String): Resource<FineStatusDto>

    suspend fun getClassFineStatus(
        schoolCode: String,
        grade: String,
        classNum: String
    ): Resource<GetClassroomStatusDto>

    suspend fun setUserProfile(userProfileRes: UserProfileRes): Resource<String>

    suspend fun getUserProfile(uid: String): Resource<GetUserProfileDto>

    suspend fun getFindEmail(name: String, schoolName: String): Resource<FindEmailDto>

    suspend fun getSurveyAnswerByXls(schoolCode: String, grade: String, classNum: String): Resource<ResponseBody>
}