package com.schoolkeepa.dust.presentation.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.model.surveydata.SurveyDataDto
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import com.schoolkeepa.dust.domain.usecase.GetSchoolData
import com.schoolkeepa.dust.domain.usecase.GetSearchSchoolData
import com.schoolkeepa.dust.domain.usecase.GetSurveyData
import com.schoolkeepa.dust.domain.usecase.ReadSurveyData
import com.schoolkeepa.dust.domain.usecase.SaveSurveyData
import com.schoolkeepa.dust.domain.usecase.SaveUserEntry
import com.schoolkeepa.dust.domain.usecase.SetSurveyAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val getSurveyData: GetSurveyData,
    private val getSchoolData: GetSchoolData,
    private val getSearchSchoolData: GetSearchSchoolData,
    private val postSurveyData: SetSurveyAnswer,
    private val saveUserEntry: SaveUserEntry,
    private val readSurveyData: ReadSurveyData,
    private val saveSurveyData: SaveSurveyData,
) : ViewModel() {

    private var _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private var _schoolName = MutableLiveData("학교를 입력해 주세요.")
    val schoolName: LiveData<String> = _schoolName

    private var _grade = MutableLiveData("")
    val grade: LiveData<String> = _grade

    private var _ban = MutableLiveData("")
    val ban: LiveData<String> = _ban

    private var _studentNumber = MutableLiveData("")
    val studentNumber: LiveData<String> = _studentNumber

    private var _schoolCode = MutableLiveData("")
    val schoolCode: LiveData<String> = _schoolCode

    private var _agreement = MutableLiveData(false)
    val agreement: LiveData<Boolean> = _agreement

    fun setInfo(
        name: String,
        schoolName: String,
        grade: String,
        ban: String,
        studentNumber: String,
        agreement: Boolean
    ) {
        _name.value = name
        _schoolName.value = schoolName
        _grade.value = grade
        _ban.value = ban
        _studentNumber.value = studentNumber
        _agreement.value = agreement
    }


    private var _data = MutableLiveData("")
    val data: LiveData<String> = _data

    suspend fun finishSurvey(date: String) = viewModelScope.launch {
        println("@@@@@@" + date)
        readSurveyData().collect {
            _data.value = it
            println("@@@@@WHY" + it)
        }
    }

    suspend fun saveSurvey(date: String) = viewModelScope.launch {
        println(data.value.toString() + "@@@@##")
        if (!data.value!!.contains(date)) {
            saveSurveyData(data.value!! + date + "/")
        }
    }

    private var _surveyData = MutableLiveData(SurveyDataDto())
    val surveyData: LiveData<SurveyDataDto> = _surveyData

    fun setSurveyData(surveyData: SurveyDataDto) {
        _surveyData.value = surveyData
    }

    fun saveUserData(data: String) = viewModelScope.launch {
        saveUserEntry(data)
    }

//    suspend fun getSurvey(type: String) = viewModelScope.launch {
//
//    }

    suspend fun getSurvey(type: String): Resource<SurveyDataDto> {
        return getSurveyData(type)
    }

    suspend fun getSchoolList(): SchoolListDto? {
        return getSchoolData().data
    }

    suspend fun searchSchoolList(text: String): Resource<SchoolListDto> {
        return getSearchSchoolData(text)
    }

    suspend fun postSurvey(surveySetData: SurveySetRes): Resource<String> {
        return postSurveyData(surveySetData)
    }
}