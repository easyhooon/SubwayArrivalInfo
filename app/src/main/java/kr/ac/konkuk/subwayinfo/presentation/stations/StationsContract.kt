package kr.ac.konkuk.subwayinfo.presentation.stations

import kr.ac.konkuk.subwayinfo.domain.Station
import kr.ac.konkuk.subwayinfo.presentation.BasePresenter
import kr.ac.konkuk.subwayinfo.presentation.BaseView

interface StationsContract {
    interface View : BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    interface Presenter : BasePresenter {
        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}