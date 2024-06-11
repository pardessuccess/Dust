package com.schoolkeepa.dust.presentation.auth

import androidx.lifecycle.ViewModel
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.email.FindEmailDto
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.domain.usecase.GetFindEmail
import com.schoolkeepa.dust.domain.usecase.GetSearchSchoolData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FineEmailViewModel @Inject constructor(
    private val getSearchSchoolData: GetSearchSchoolData,
    private val getFindEmail: GetFindEmail
) : ViewModel() {

    suspend fun searchSchoolList(text: String): Resource<SchoolListDto> {
        return getSearchSchoolData(text)
    }

    suspend fun findEmail(name: String, schoolName: String): Resource<FindEmailDto> {
        return getFindEmail(name, schoolName)
    }

}