package com.example.aihousekeeper.datas

import com.google.gson.annotations.SerializedName

data class ValidateUsernameResponse(@SerializedName("is_unique") val isUnique: Boolean)
