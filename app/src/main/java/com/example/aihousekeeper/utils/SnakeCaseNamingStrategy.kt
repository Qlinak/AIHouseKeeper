package com.example.aihousekeeper.utils

import com.google.gson.FieldNamingStrategy
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Field

class SnakeCaseNamingStrategy : FieldNamingStrategy {
    override fun translateName(f: Field): String {
        val snakeCase = f.name
            .replace("([A-Z]+)".toRegex(), "_$1")
            .lowercase()
        return if (f.isAnnotationPresent(SerializedName::class.java)) {
            f.getAnnotation(SerializedName::class.java)!!.value
        } else {
            snakeCase
        }
    }
}