package com.schoolkeepa.dust.presentation.intro

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.response.login.UserProfile
import com.schoolkeepa.dust.data.response.login.UserProfileRes
import com.schoolkeepa.dust.domain.repository.AuthRepository
import com.schoolkeepa.dust.domain.usecase.GetSearchSchoolData
import com.schoolkeepa.dust.domain.usecase.ReadSurveyData
import com.schoolkeepa.dust.domain.usecase.ReadUserEntry
import com.schoolkeepa.dust.domain.usecase.SaveSurveyData
import com.schoolkeepa.dust.domain.usecase.SaveUserEntry
import com.schoolkeepa.dust.domain.usecase.teacher.GetUserProfile
import com.schoolkeepa.dust.domain.usecase.teacher.SetUserProfile
import com.schoolkeepa.dust.presentation.navgraph.DustDestinations.HOME_ROUTE
import com.schoolkeepa.dust.presentation.navgraph.DustDestinations.INTRO_ROUTE
import com.schoolkeepa.dust.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val readUserEntry: ReadUserEntry,
    private val saveUserEntry: SaveUserEntry,
    private val readSurveyData: ReadSurveyData,
    private val saveSurveyData: SaveSurveyData,
    private val getUserProfile: GetUserProfile,
    private val setUserProfile: SetUserProfile,
    private val getSearchSchoolData: GetSearchSchoolData
) : ViewModel() {

    suspend fun searchSchoolList(text: String): Resource<SchoolListDto> {
        return getSearchSchoolData(text)
    }

    suspend fun postUserProfile(userProfileRes: UserProfileRes) {
        setUserProfile(userProfileRes = userProfileRes)
    }

    private val _startDestination = mutableStateOf(Screen.Main.route)
    val startDestination: State<String> = _startDestination

    private val _userData = mutableStateOf(false)
    val userData: State<Boolean> = _userData

    private val _isTeacher = mutableStateOf(false)
    val isTeacher: State<Boolean> = _isTeacher

    private val _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    init {
//        readUserEntry().onEach {
////            println("@@@@@@@ StartDestination Value" + startDestination.value + "@@@@@")
//            println("@@@@@@@ User Entry " + it + "@@@@@")
//            if (it.isNotEmpty()) {
//                _startDestination.value = Screen.Main.route
//                val a = it.split("/")
//                if (a.size != 1) {
//                    _userData.value = true
//                }
//                println("@@@@@@@ USER DATA $it")
//            } else {
//                _startDestination.value = Screen.Intro.route
//                readSurveyData().onEach {
//                    println("HI@@@@" + it)
//                }
//                println("@@@@@@@ NOT USER DATA $it")
//            }
//            delay(500L)
//            _loading.value = false
//
//        }.launchIn(viewModelScope)
//
//        readSurveyData().onEach {
//            println("@@@" + it)
//        }.launchIn(viewModelScope)
//        authRepository.autoLogin()
    }

    private var _registerState = MutableStateFlow<RegisterState>(value = RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    suspend fun registerUser(email: String, password: String, userProfile: UserProfile) =
        viewModelScope.launch {
            authRepository.registerUser(email = email, password = password)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _registerState.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            _registerState.update { it.copy(isSuccess = "Register Successful!") }
                            result.data!!.user!!.uid
                            setUserProfile(
                                UserProfileRes(
                                    uid = result.data.user!!.uid,
                                    userProfile = userProfile
                                )
                            )
                        }

                        is Resource.Error -> {
                            _registerState.update { it.copy(isError = result.message) }
                        }
                    }
                }
        }

    suspend fun saveUserGrade(grade: String) = viewModelScope.launch {
        saveUserEntry(grade)
    }

    suspend fun saveSurvey(surveyData: String) = viewModelScope.launch {
        saveSurveyData(surveyData)
    }

    fun readSurvey() = readSurveyData()

    fun readUserGrade() = readUserEntry()

    suspend fun loginUser(email: String, password: String, onNavigateRoute: (String) -> Unit) =
        viewModelScope.launch {
            authRepository.loginUser(email, password).collectLatest {
                if (it is Resource.Success) {
                    getUserProfile(it.data!!.user!!.uid).data?.let {
                        val userProfile = it
                        saveUserGrade("teacher/${userProfile.name}/${userProfile.school_name}/${userProfile.grade}/${userProfile.class_num}/0/${userProfile.school_code}/${userProfile.teacher_code}/${userProfile.school_address}")
                        _startDestination.value = HOME_ROUTE
                        println("@@@@@SUCCESS HI!")
                    }
//                saveUserGrade("teacher//////")
                }
            }
        }
}