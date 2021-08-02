package kr.ac.konkuk.subwayinfo.presentation.stationarrivals

import android.database.sqlite.SQLiteCantOpenDatabaseException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kr.ac.konkuk.subwayinfo.data.repository.StationRepository
import kr.ac.konkuk.subwayinfo.domain.Station
import java.lang.Exception

class StationArrivalsPresenter(
    private val view: StationArrivalsContract.View,
    private val station: Station,
    private val stationRepository: StationRepository
) : StationArrivalsContract.Presenter {

    override val scope = MainScope()

    override fun onViewCreated() {
        fetchStationArrivals()
    }

    override fun onDestroyView() {}

    override fun fetchStationArrivals() {
        scope.launch {
            try{
                view.showLoadingIndicator()
                view.showStationArrivals(stationRepository.getStationArrivals(station.name))
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorDescription(exception.message ?: "알 수 없는 문제가 발생했어요 😢")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    //station 에 있는 메소드와 흡사
    //하나의 station 에 도착정보만을 보여줌, station 을 이미 가지고 있음, 따로 station을 전달 받지 않음
    override fun toggleStationFavorite() {
        scope.launch {
            //가지고 있던 station 을 copy 하는 방향으로 구현
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }

}