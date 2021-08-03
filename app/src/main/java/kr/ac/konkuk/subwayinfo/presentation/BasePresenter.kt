package kr.ac.konkuk.subwayinfo.presentation

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface BasePresenter {

    //rx 가 아닌 coroutine 으로 진행
    val scope: CoroutineScope
        //비동기 처리
//        get() = MainScope()

    fun onViewCreated()

    fun onDestroyView()

    //실제로 presenter 가 onDestroy 될 경우에 cancel (scope handling)
    //화면 구성이 간단한 관계로 fragment 에 직접적으로 presenter.onDestroy()를 호출하는 방법을 채택
    //화면 구성이 복잡하거나 협업일 경우 human error(실수로 빠뜨릴 경우)를 대비하기위해 interface 에 정의하는 것이 아닌
    //lifecycle 에 맞춰 호출해주는 BaseFragment 까지 구현하는 방식을 권장
    @CallSuper
    fun onDestroy() {
        scope.cancel()
    }

}