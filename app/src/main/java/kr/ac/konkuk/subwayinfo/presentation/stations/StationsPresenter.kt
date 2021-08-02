package kr.ac.konkuk.subwayinfo.presentation.stations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.ac.konkuk.subwayinfo.data.repository.StationRepository
import kr.ac.konkuk.subwayinfo.domain.Station

class StationsPresenter(
    // view 와 stationRepository 를 주입 받을 예정
    private val view: StationsContract.View,
    private val stationRepository: StationRepository
) : StationsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    //stateFlow 에 직접 값을 넣어줄 수 있는, 방출할 수 있는 마지막 것을 가지고 있는 flow
    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList()) //<-- MutableStateFlow 정의

    init {
        observeStations()
    }

    override fun onViewCreated() {
        scope.launch {
            //다른 화면에 갔다가 다시 왔을 때 실제 값이 존재할 경우 station 에 보여주려고 별도의 MutableStateFlow 정의
            view.showStations(stations.value)
            //stations 의 마지막 state 를 가져와서 보여줌
            stationRepository.refreshStations()
        }
    }

    override fun onDestroyView() {}

    override fun filterStations(query: String) {
        scope.launch {
            //queryString 을 방출 -> 한번 더 observeStations가 호출
            queryString.emit(query)
        }
    }

    //presenter 에서는 observing 을  수행
    private fun observeStations() {
        stationRepository
            .stations
                //stations 에 담긴 정보와 queryString 을 combine
            .combine(queryString) { stations, query->
                if(query.isBlank()){
                    //query 가 비어있다면 그냥 stations 전체
                    stations
                } else {
                    //queryString 에 해당하는 이름을 포함하는 역만 전달
                    stations.filter {
                        it.name.contains(query)
                    }
                }
            }
            //stations 라는 flow를 구독을 시작했을 때
            .onStart { view.showLoadingIndicator() }
            //새로운 값이 들어올 때 마다 내부 로직을 수행
            .onEach {
                if(it.isNotEmpty()){
                    view.hideLoadingIndicator()
                }
                stations.value = it
                view.showStations(it)
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
                //scope 안에서 launch 를 수행  collect() == observing
                //scope 가 종료되면 작업 역시 cancel 된다
            .launchIn(scope)
    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            //기존 reference 데이터는 유지(영향을 주지 않고), 새로운 값을 전달하는 것을 권장
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }



}