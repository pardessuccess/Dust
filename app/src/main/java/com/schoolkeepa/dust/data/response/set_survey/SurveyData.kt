package com.schoolkeepa.dust.data.response.set_survey

data class SurveyData(
    val answers: List<Answer>,
    val date: String,
    val question_id: Int
)