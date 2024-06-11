package com.schoolkeepa.dust.data.response.login

data class UserProfile(
    val school_code: Int,
    val grade: Int,
    val class_num: Int,
    val name: String,
    val user_type: String,
    val teacher_code: String,
    val school_name: String,
    val school_address: String
)