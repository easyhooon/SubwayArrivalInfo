package kr.ac.konkuk.subwayinfo.data.api

import kr.ac.konkuk.subwayinfo.data.db.entity.StationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.SubwayEntity

interface StationApi {

    //언제 최종적으로 업데이트 되었는지
    suspend fun getStationDataUpdatedTimeMillis(): Long

    //station 과 subway 의 pair
    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}