package kr.ac.konkuk.subwayinfo.presentation.stationarrivals

import kr.ac.konkuk.subwayinfo.domain.ArrivalInformation
import kr.ac.konkuk.subwayinfo.presentation.BasePresenter
import kr.ac.konkuk.subwayinfo.presentation.BaseView

//Contract interface 에 선언한 메소드를 presenter 에서 구현
interface StationArrivalsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter : BasePresenter {

        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}