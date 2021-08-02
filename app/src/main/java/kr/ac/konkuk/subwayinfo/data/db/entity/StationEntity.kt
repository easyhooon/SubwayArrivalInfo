package kr.ac.konkuk.subwayinfo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
//역
data class StationEntity(
    @PrimaryKey val stationName: String,
    val isFavorited: Boolean = false
)