package kr.ac.konkuk.subwayinfo.data.api

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kr.ac.konkuk.subwayinfo.data.db.entity.StationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.SubwayEntity

//FirebaseStorage 를 가져오기 위함
class StationStorageApi (
    firebaseStorage: FirebaseStorage
        ) : StationApi {

    //다운로드를 바거나 메타데이터를 가져온다거나
    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)

    override suspend fun getStationDataUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis
    //task 를 반환 (비동기를 위한) 평소 같았으면 addOnCompleteListener
    //Coroutine 을 쓰는 이유: listener 사용을 줄이기 위함 (coroutine play service 의존성 추가로 Task 에 대해 await() 사용이 가능)
    //task 가 완료가 될 경우에는 deferred을 받아 값을 리턴하는 형태로 구현

    //await()를 사용하여 coroutine 스럽게 변환
    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        //size 를 가져옴
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        //file download
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        //csv 한 line 씩 left,right \n
        //csv 가 일반적으로 parsing 하기 쉬움
        return byteArray.decodeToString()
            .lines()
            .drop(1) //header 필요없는 부분 날림
            .map{it.split((",")) }
                // 1. subway id, 2. station name,
                //station name 을 StationEntity 에 추가, subwayEntity 를 생성하면서 subwayId를 int 로 변환해서 전달을 해주는 형태로 구현해서 리턴
            .map{ StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }

    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}