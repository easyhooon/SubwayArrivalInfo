package kr.ac.konkuk.subwayinfo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kr.ac.konkuk.subwayinfo.R
import kr.ac.konkuk.subwayinfo.databinding.ActivityMainBinding
import kr.ac.konkuk.subwayinfo.extension.toGone
import kr.ac.konkuk.subwayinfo.extension.toVisible
import kr.ac.konkuk.subwayinfo.presentation.stationarrivals.StationArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    //네비게이션과 액션바 정상 연동
    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews() {
        //화면 전환시마다 호출
        //argument 까지 전달
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if(destination.id == R.id.station_arrivals_dest) {
                //지하철 역 도착정보 화면에 갈 때 마다
                //argument 로 전달 받은 station 의 역 이름을 지정
                title = StationArrivalsFragmentArgs.fromBundle(argument!!).station.name
                //상세화면으로 갔을 때 툴바를 보이게 함
                binding.toolbar.toVisible()
            } else {
                binding.toolbar.toGone()
            }
        }
    }


}