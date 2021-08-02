package kr.ac.konkuk.subwayinfo.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kr.ac.konkuk.subwayinfo.data.api.StationApi
import kr.ac.konkuk.subwayinfo.data.api.StationArrivalsApi
import kr.ac.konkuk.subwayinfo.data.api.response.mapper.toArrivalInformation
import kr.ac.konkuk.subwayinfo.data.db.StationDao
import kr.ac.konkuk.subwayinfo.data.db.entity.mapper.toStationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.mapper.toStations
import kr.ac.konkuk.subwayinfo.data.preference.PreferenceManager
import kr.ac.konkuk.subwayinfo.domain.ArrivalInformation
import kr.ac.konkuk.subwayinfo.domain.Station

class StationRepositoryImplement (
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
        ) : StationRepository {

            override val stations: Flow<List<Station>> =
                stationDao.getStationWithSubways()
                        //의도하지 않은 변경에도 observable item이 방출됨, 이러한 과도한 방출을 막기 위해 권장되는 메소드
                    .distinctUntilChanged()
                        //내림차순으로 정렬, true 를 false 보다 더 높은 값으로 인식(true, isFavorited 인 역들이 더 위로 올라오도록)
                    .map { stations -> stations.toStations().sortedByDescending { it.isFavorited }}
                        //어떤 스레드에서 데이터가 흐를 것인가 정의 (IO)
                    .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher) {
        //파일 업데이트 시점
        val fileUpdatedTimeMills = stationApi.getStationDataUpdatedTimeMillis()
        //마지막으로 DB 가 업데이트 된 시점
        val lastDatabaseUpdatedMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        //받은 적이 없거나 파베에 파일 업데이트 시점이 DB 업데이트 시점보다 나중일 경우
        if(lastDatabaseUpdatedMillis == null || fileUpdatedTimeMills > lastDatabaseUpdatedMillis) {
           stationDao.insertStationSubways(stationApi.getStationSubways())
            //저장이 끝나고 저장 시점을 time stamp 를 찍음
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMills)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy{it.direction} //필드에 대해 중복되는 부분이 있으면 가장 첫번째 것을 가져오도록
            ?.sortedBy{it.subway} //호선별로 sort
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다")

    }

    override suspend fun updateStation(station: Station) = withContext(dispatcher) {
        //station 을 StationEntity 로 변환해서 dao 에 정의한 updateStation 으로 전달하는 방식
        stationDao.updateStation(station.toStationEntity())
    }

    companion object {
        const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}