package kr.ac.konkuk.subwayinfo.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.ac.konkuk.subwayinfo.data.db.entity.StationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.StationSubwayCrossRefEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.StationWithSubwaysEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.SubwayEntity


@Dao
interface StationDao {

    @Transaction
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    //insert 는 내부적으로 transaction 을 해주기 때문에 query 일때만 transaction 을 붙여주면 됨
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(subways: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(reference: List<StationSubwayCrossRefEntity>)

    @Transaction
    suspend fun insertStationSubways(stationSubways: List<Pair<StationEntity, SubwayEntity>>) {
        insertStations(stationSubways.map {it.first})
        insertSubways(stationSubways.map{ it.second})
        //안에서 실패가 일어날 경우 롤백하는 기능을 포함
        insertCrossReferences(
            stationSubways.map{ (station, subway) ->
                StationSubwayCrossRefEntity(
                    station.stationName,
                    subway.subwayId
                )
            }
        )
    }
    @Update
    suspend fun updateStation(station: StationEntity)
}