package com.schoolkeepa.dust.data.response.set_survey

data class User(
    val school_code: Int,
    val grade: Int,
    val class_num: Int,
    val name: String,
    val user_type: String,
    val student_num: Int
)