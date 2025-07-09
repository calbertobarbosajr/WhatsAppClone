package com.calberto_barbosa_jr.interactus.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {

    @Insert
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Query("SELECT * FROM profile")
    suspend fun getAllProfile(): List<Profile>
}