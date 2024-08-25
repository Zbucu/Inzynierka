package com.example.hoopstats.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hoopstats.R
import java.util.Date



@Entity
data class Drill(
    @PrimaryKey(autoGenerate = true) val drillId: Int = 0,
    var name: String,
    var lastDone: Date? = null,
    var percentage: Float = 0f,
    var image: Int = R.drawable.ic_launcher_background,
    var shotsTaken: Int = 0,
    var shotsMade: Int = 0
)