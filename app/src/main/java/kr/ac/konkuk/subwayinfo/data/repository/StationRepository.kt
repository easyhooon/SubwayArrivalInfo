package kr.ac.konkuk.subwayinfo.data.repository

import kotlinx.coroutines.flow.Flow
import kr.ac.konkuk.subwayinfo.domain.ArrivalInformation
import kr.ac.konkuk.subwayinfo.domain.Station

interface StationRepository {

    //Flow 를 observing 하는 stations
    val stations: Flow<List<Station>>

    //station db 를 update 해야하는 상황인지 판단, db 에 update
    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)
}