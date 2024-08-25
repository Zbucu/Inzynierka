package com.example.hoopstats.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DrillDao {

    @Insert
    fun createDrill(drill: Drill)

    @Query("SELECT * FROM Drill")
    fun getAllDrills(): List<Drill>

    @Query("SELECT * FROM Drill WHERE drillId = :id")
    fun getDrillById(id: Int): Drill?

    @Update
    fun updateDrill(drill: Drill)

    @Delete
    fun deleteDrill(drill: Drill)

    @Query("DELETE FROM Drill")
    fun deleteAll()
}