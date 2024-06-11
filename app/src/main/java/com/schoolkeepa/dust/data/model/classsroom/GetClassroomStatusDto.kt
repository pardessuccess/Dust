package com.schoolkeepa.dust.data.model.classsroom

data class GetClassroomStatusDto(
    val finedust_factor: String,
    val ultrafine_factor: String,
    val fine_status: String,
    val ultra_status: String
)