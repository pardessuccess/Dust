package com.schoolkeepa.dust.data.response.set_survey

data class Answer(
    val sub_question_id: Int,
    val type: String,
    val sub_question_answer: String,
    val sub_question_input: String,
)