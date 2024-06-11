package com.schoolkeepa.dust.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.classsroom.GetClassroomStatusDto
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.data.response.post.classroom.ClassStatusRes
import com.schoolkeepa.dust.domain.usecase.GetFineStatus
import com.schoolkeepa.dust.domain.usecase.GetWeather
import com.schoolkeepa.dust.domain.usecase.ReadSurveyData
import com.schoolkeepa.dust.domain.usecase.ReadUserEntry
import com.schoolkeepa.dust.domain.usecase.SaveUserEntry
import com.schoolkeepa.dust.domain.usecase.local.GetClassFineStatus
import com.schoolkeepa.dust.domain.usecase.local.SetClassroomStatus
import com.schoolkeepa.dust.domain.usecase.teacher.GetUserProfile
import com.schoolkeepa.dust.presentation.navgraph.DustDestinations
import com.schoolkeepa.dust.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readSurveyData: ReadSurveyData,
    private val readUserEntry: ReadUserEntry,
    private val getWeather: GetWeather,
    private val getFineStatus: GetFineStatus,
    private val getClassFineStatus: GetClassFineStatus,
    private val setClassroomStatus: SetClassroomStatus,
    private val providesFirebaseAuth: FirebaseAuth,
    private val saveUserEntry: SaveUserEntry,
    private val getUserProfile: GetUserProfile
) : ViewModel() {

    private val _startDestination = mutableStateOf(Screen.Main.route)
    val startDestination: State<String> = _startDestination

    private val _userData = mutableStateOf("")
    val userData: State<String> = _userData

    private val _isUserInfo = mutableStateOf(false)
    val isUserInfo: State<Boolean> = _isUserInfo

    private val _userType = mutableStateOf("")
    val userType: State<String> = _userType

    private val _userDataList = mutableStateOf(listOf("", "", "", "", "", "", ""))
    val userDataList: State<List<String>> = _userDataList

    suspend fun getCurrentWeather(lat: String, lng: String, date: String): Resource<WeatherDto> {
        return getWeather(lat, lng, date)
    }

    suspend fun getOutsideFineStatus(locationCode: String): Resource<FineStatusDto> {
        return getFineStatus(locationCode)
    }

    suspend fun setClassFine(classStatusRes: ClassStatusRes) {
        setClassroomStatus(classStatusRes)
    }

    suspend fun getClassFine(
        schoolCode: String,
        grade: String,
        classNum: String
    ): Resource<GetClassroomStatusDto> {
        println("schoolCode=" + schoolCode + "&grade=" + grade + "&classNum=" + classNum)
        return getClassFineStatus(schoolCode, grade, classNum)
    }

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    suspend fun saveUserGrade(grade: String) = viewModelScope.launch {
        saveUserEntry(grade)
    }

    init {
        println("@@@@@ MAINVIEWMODEL INIT")
        readUserEntry().onEach {
//            println("@@@@@@@ StartDestination Value" + startDestination.value + "@@@@@")
            println("@@@@@@@ User Entry " + it + "@@@@@")
            if (it.isNotEmpty()) {
                _startDestination.value = Screen.Main.route
                _userData.value = it
                val dataList = it.split("/")
                _userDataList.value = dataList
                if (dataList.size != 1) {
                    _isUserInfo.value = true
                }
                println("@@@@@@@ USER DATA $it")
                _userType.value = if (it.contains("elementary")) {
                    "elementary"
                } else if (it.contains("middle")) {
                    "middle"
                } else if (it.contains("high")) {
                    "high"
                } else {
                    "teacher"
                }
            } else if (providesFirebaseAuth.currentUser != null) {
                _startDestination.value = Screen.Main.route
                _userType.value = "teacher"

//                getUserProfile(providesFirebaseAuth.currentUser!!.uid).data?.let {
//                    val userProfile = it
//                    saveUserGrade("teacher/${userProfile.name}/${userProfile.school_name}/${userProfile.grade}/${userProfile.class_num}/0/${userProfile.school_code}")
//                    _startDestination.value = DustDestinations.HOME_ROUTE
//                    println("@@@@@SUCCESS HI!")
//                }
            } else {
                _startDestination.value = Screen.Intro.route
                readSurveyData().onEach {
                    println("HI@@@@" + it)
                }
                println("@@@@@@@ NOT USER DATA $it")
            }

            delay(500L)
            _loading.value = false
        }.launchIn(viewModelScope)

        readSurveyData().onEach {
            println("@@@" + it)
        }.launchIn(viewModelScope)
//        authRepository.autoLogin()
    }

    fun getUserInfoSaved() {
        val a = userData.value.split("/")
        println(a.toString() + "@@###@@@")
        if (a.size != 1 && a[1].isNotEmpty()) {
            _isUserInfo.value = true
        } else {
            _isUserInfo.value = false
        }
    }

    private val _tmpUserData: MutableStateFlow<String> = MutableStateFlow("")
    val tmpUserData: StateFlow<String> = _tmpUserData

    private val _surveyData: MutableStateFlow<String> = MutableStateFlow("")
    val surveyData: StateFlow<String> = _surveyData

    suspend fun setSurveyStatus() = viewModelScope.launch {
        readSurveyData().collectLatest {
            _surveyData.value = it
        }
    }

    fun getUserData() =
        readUserEntry().onEach {
            _userData.value = it
            val dataList = it.split("/")
            _userDataList.value = dataList
            if (dataList.size != 1) {
                _isUserInfo.value = true
            }
            println("@@@@@@@ USER DATA $it")
            _userType.value = if (it.contains("elementary")) {
                "elementary"
            } else if (it.contains("middle")) {
                "middle"
            } else if (it.contains("high")) {
                "high"
            } else {
                "teacher"
            }
        }.launchIn(viewModelScope)


}