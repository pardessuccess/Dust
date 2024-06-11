package com.schoolkeepa.dust.data.manager

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.schoolkeepa.dust.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserManagerImpl @Inject constructor(
    private val application: Application
) : LocalUserManager {
    override suspend fun saveUserEntry(status: String) {
        application.dataStore.edit {
            it[PreferencesKeys.USER_STATUS] = status
        }
    }

    override fun readUserEntry(): Flow<String> {
        return application.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_STATUS] ?: ""
        }
    }

    override suspend fun saveSurveyData(surveyStatus: String) {
        application.dataStore.edit {
            it[PreferencesKeys.SURVEY_STATUS] = surveyStatus
        }
    }

    override fun readSurveyData(): Flow<String> {
        return application.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SURVEY_STATUS] ?: ""
        }
    }

}

private val userProperty = preferencesDataStore(name = "user_settings")

val Context.dataStore: DataStore<Preferences> by userProperty

private object PreferencesKeys {
    val USER_STATUS = stringPreferencesKey("USER_STATUS")
    val SURVEY_STATUS = stringPreferencesKey("SURVEY_STATUS")
}