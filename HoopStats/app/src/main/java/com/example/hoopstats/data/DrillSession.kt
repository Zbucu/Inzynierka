package com.example.hoopstats.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Drill::class,
        parentColumns = ["drillId"],
        childColumns = ["drillId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DrillSession(
    @PrimaryKey(autoGenerate = true) val sessionId: Int = 0,
    var drillId: Int, // Foreign key referencing Drill entity
    var shotsTaken: Int,
    var shotsMade: Int,
    var percentage: Float,
    var date: Date
)
