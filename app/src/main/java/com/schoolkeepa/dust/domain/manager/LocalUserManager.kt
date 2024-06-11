package com.schoolkeepa.dust.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {

    suspend fun saveUserEntry(status: String)

    fun readUserEntry(): Flow<String>

    suspend fun saveSurveyData(surveyStatus: String)

    fun readSurveyData(): Flow<String>



}