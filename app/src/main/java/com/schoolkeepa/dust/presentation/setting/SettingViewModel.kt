package com.schoolkeepa.dust.presentation.setting

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.response.login.UserProfile
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.domain.usecase.GetSearchSchoolData
import com.schoolkeepa.dust.domain.usecase.ReadUserEntry
import com.schoolkeepa.dust.domain.usecase.SaveUserEntry
import com.schoolkeepa.dust.domain.usecase.teacher.SetUserProfile
import com.schoolkeepa.dust.domain.usecase.xls.GetSurveyAnswerByXls
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val saveUserEntry: SaveUserEntry,
    private val getUserEntry: ReadUserEntry,
    private val getSearchSchoolData: GetSearchSchoolData,
    private val providesFirebaseAuth: FirebaseAuth,
    private val setUserProfile: SetUserProfile,
    private val getSurveyAnswerByXls: GetSurveyAnswerByXls
) : ViewModel() {

    fun saveFileFromResponseBody(responseBody: ResponseBody, filePath: String) {
        try {
            val inputStream = responseBody.byteStream()
            val outputStream = FileOutputStream(filePath)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
        } catch (e: IOException) {
            // 에러 처리
        } finally {
            responseBody.close()
        }
    }

    suspend fun getXls(schoolCode: String, grade: String, classNum: String) =
        viewModelScope.launch {
            val downloadDir = Environment.getExternalStorageDirectory().absolutePath + "/Download"
            val a = getSurveyAnswerByXls(
                schoolCode, grade, classNum
            )
            a.data?.let {
                saveFileFromResponseBody(a.data, downloadDir)
            }
            println(a.data.toString())
        }

    fun resetUserEntry() = viewModelScope.launch {
        saveUserEntry("")
    }

    suspend fun searchSchoolList(text: String): Resource<SchoolListDto> {
        return getSearchSchoolData(text)
    }

    suspend fun saveUserData(user: String) {
        saveUserEntry(user)
    }

    suspend fun saveTeacherData(userProfile: UserProfile) {
        providesFirebaseAuth.currentUser?.let {
            setUserProfile(
                UserProfileRes(
                    uid = it.uid,
                    userProfile
                )
            )
        }
    }

    suspend fun signOut() = viewModelScope.launch {
        if (providesFirebaseAuth.currentUser == null) {
            println("NULL")
        }
        providesFirebaseAuth.signOut()
        saveUserEntry("")
    }
}