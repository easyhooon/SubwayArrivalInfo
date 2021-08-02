package kr.ac.konkuk.subwayinfo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
//노선
data class SubwayEntity(
    @PrimaryKey val subwayId: Int,
)