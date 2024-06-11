package com.schoolkeepa.dust.data.network

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface DustApi {

    @POST("/user/set")
    suspend fun setUserProfile(
        @Body userProfile: UserProfileRes
    ): String

    @GET("/user/get")
    suspend fun getUserProfile(
        @Query("uid") uid: String,
    ): GetUserProfileDto

    @GET("survey/data")
    suspend fun getSurveyData(
        @Query("type") type: String
    ): SurveyDataDto

    @POST("survey/set")
    suspend fun setSurveyAnswer(
        @Body surveySetRes: SurveySetRes
    ): String

    @POST("classroom/set")
    suspend fun setClassroomStatus(
        @Body classStatusRes: ClassStatusRes
    ): String

    @GET("classroom/get")
    suspend fun getClassroomStatus(
        @Query("schoolCode") schoolCode: String,
        @Query("grade") grade: String,
        @Query("classNum") classNum: String,
    ): GetClassroomStatusDto

    @GET("weather/get")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("date") date: String
    ): WeatherDto

    @GET("finestatus/get")
    suspend fun getFineStatus(
        @Query("location") code: String,
    ): FineStatusDto

    @GET("user/findEmail")
    suspend fun getFindEmail(
        @Query("name") name: String,
        @Query("schoolName") schoolName: String,
    ): FindEmailDto

    @Streaming
    @GET("survey/download")
    suspend fun getSurveyAnswerByXls(
        @Query("schoolCode") schoolCode:String,
        @Query("grade") grade:String,
        @Query("class_num") classNum:String,
    ): ResponseBody


}