package com.example.hoopstats.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface DrillSessionDao {

    @Insert
    fun createDrillSession(drillSession: DrillSession)

    @Query("SELECT * FROM DrillSession")
    fun getAllDrillSessions(): List<DrillSession>

    @Query("SELECT * FROM DrillSession WHERE drillId = :id")
    fun getAllSessionsForDrill(id: Int): List<DrillSession>

    @Query("SELECT * FROM DrillSession WHERE drillId = :id AND date BETWEEN :startDate AND :endDate")
    fun getAllSessionsForDrillBetweenDates(id: Int, startDate: Date, endDate: Date): List<DrillSession>


    @Update
    fun updateDrillSession(drillSession: DrillSession)

    @Delete
    fun deleteDrillSession(drillSession: DrillSession)

    @Query("DELETE FROM DrillSession")
    fun deleteAll()
}