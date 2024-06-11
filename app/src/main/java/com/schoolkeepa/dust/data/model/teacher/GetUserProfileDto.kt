package com.schoolkeepa.dust.data.model.teacher

data class GetUserProfileDto(
    val class_num: Int,
    val grade: Int,
    val name: String,
    val school_address: String,
    val school_code: Int,
    val school_name: String,
    val user_type: String,
    val teacher_code: String,
)