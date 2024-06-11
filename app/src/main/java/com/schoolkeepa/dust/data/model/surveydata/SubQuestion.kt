package com.schoolkeepa.dust.data.model.surveydata

data class SubQuestion(
    val options: List<Option>,
    val sub_question_id: Int,
    val text: String?,
    val type: String
)