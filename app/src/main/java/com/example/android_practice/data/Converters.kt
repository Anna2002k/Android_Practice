package com.example.android_practice.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.android_practice.entity.FavoriteMovieEntity

class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}