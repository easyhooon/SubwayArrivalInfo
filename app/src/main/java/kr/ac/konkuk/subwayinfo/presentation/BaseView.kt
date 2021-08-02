package kr.ac.konkuk.subwayinfo.presentation

//MVP 에서는 view 는 presenter 를 알고 presenter 도 view 를 암 (직접적인 참조x 인터페이스를 통해)
interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}