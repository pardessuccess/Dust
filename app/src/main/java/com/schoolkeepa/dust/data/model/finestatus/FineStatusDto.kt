package com.schoolkeepa.dust.data.model.finestatus

data class FineStatusDto(
    val finedust_factor: String,
    val ultrafine_factor: String,
    val fine_status: String,
    val ultra_status: String,
    val result: String
)