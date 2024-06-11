package com.schoolkeepa.dust


// 포켓몬 데이터를 불러오는 과정에서 Succes, Error, Loading 으로 데이터 분기

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

}