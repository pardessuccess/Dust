package com.schoolkeepa.dust.data.model.surveydata

data class SurveyDataDto(
    val content_type: String = "",
    val data : List<Data> = listOf()
)