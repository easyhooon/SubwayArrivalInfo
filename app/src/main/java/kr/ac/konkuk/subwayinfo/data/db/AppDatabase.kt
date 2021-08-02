package kr.ac.konkuk.subwayinfo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.ac.konkuk.subwayinfo.data.db.entity.StationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.StationSubwayCrossRefEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.SubwayEntity

//entity 와 Dao 를 정의한 이유 나중에 Repository 를 쓰기 위함
@Database(
    entities = [StationEntity::class, SubwayEntity::class, StationSubwayCrossRefEntity::class],
    version = 1,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao

    companion object {

        private  const val DATABASE_NAME = "station.db"

        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
}