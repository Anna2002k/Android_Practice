package com.example.android_practice.content

import com.example.android_practice.entity.MovieEntity

fun MovieEntity.formattedDescription(): String {
    return "Описание: ${this.description ?: "Описание отсутствует"}"
}