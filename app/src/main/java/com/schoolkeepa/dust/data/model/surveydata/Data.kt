package com.schoolkeepa.dust.data.model.surveydata

data class Data(
    val category_color: String = "",
    val category_id: Int = 0,
    val category_name: String = "",
    val help: String = "",
    val id: Int = 0,
    val question: String = "",
    val sub_text: String = "",
    val sub_questions: List<SubQuestion> = listOf()
)