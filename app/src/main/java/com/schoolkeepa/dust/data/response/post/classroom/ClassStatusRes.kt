package com.schoolkeepa.dust.data.response.post.classroom

import com.google.gson.annotations.SerializedName
import com.schoolkeepa.dust.data.response.login.UserProfile

data class ClassStatusRes(
    val classroom: Classroom,
    val userProfile: UserProfile
)