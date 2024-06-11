package com.schoolkeepa.dust.data.model.surveydata

data class Option(
    val id: Int,
    val input: Boolean,
    val range: Range,
    val selected: Boolean,
    val text: String,
    val unit: String,
    val placeholder: String,
)