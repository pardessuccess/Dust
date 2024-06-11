package com.schoolkeepa.dust.presentation.manual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManualDetailViewModel @Inject constructor() : ViewModel() {

    private val _searchList = MutableLiveData<List<Int>>(arrayListOf())
    val searchList: LiveData<List<Int>> = _searchList

    fun setSearchList(list: List<Int>) {
        _searchList.value = list
    }

    private val _searchCount = MutableLiveData<Int>(0)
    val searchCount: LiveData<Int> = _searchCount

    fun setSearchCount(count: Int) {
        _searchCount.value = count
    }

    fun setSearchNext() {
        _searchCount.value = _searchCount.value!! + 1
    }

    fun setSearchPrev() {
        _searchCount.value = _searchCount.value!! - 1
    }

}