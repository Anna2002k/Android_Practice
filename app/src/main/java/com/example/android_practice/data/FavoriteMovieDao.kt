package com.example.android_practice.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_practice.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovieEntity)

    @Delete
    suspend fun delete(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    suspend fun getById(id: Int): FavoriteMovieEntity?

    @Query("SELECT EXISTS(SELECT * FROM favorite_movies WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}