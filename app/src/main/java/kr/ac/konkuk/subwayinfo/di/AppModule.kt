package kr.ac.konkuk.subwayinfo.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kr.ac.konkuk.subwayinfo.BuildConfig
import kr.ac.konkuk.subwayinfo.data.api.StationApi
import kr.ac.konkuk.subwayinfo.data.api.StationArrivalsApi
import kr.ac.konkuk.subwayinfo.data.api.StationStorageApi
import kr.ac.konkuk.subwayinfo.data.api.Url
import kr.ac.konkuk.subwayinfo.data.api.Url.SEOUL_DATA_API_URL
import kr.ac.konkuk.subwayinfo.data.db.AppDatabase
import kr.ac.konkuk.subwayinfo.data.preference.PreferenceManager
import kr.ac.konkuk.subwayinfo.data.preference.SharedPreferenceManager
import kr.ac.konkuk.subwayinfo.data.repository.StationRepository
import kr.ac.konkuk.subwayinfo.data.repository.StationRepositoryImplement
import kr.ac.konkuk.subwayinfo.presentation.stationarrivals.StationArrivalsContract
import kr.ac.konkuk.subwayinfo.presentation.stationarrivals.StationArrivalsPresenter
import kr.ac.konkuk.subwayinfo.presentation.stations.StationsContract
import kr.ac.konkuk.subwayinfo.presentation.stations.StationsFragment
import kr.ac.konkuk.subwayinfo.presentation.stations.StationsPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val appModule = module {

    single {Dispatchers.IO}

    //Database
    //AppDatabase 를 빌드
    single { AppDatabase.build(androidApplication())}
    //주입받은 AppDatabase 를 통해 다시 Dao 를 따로 뽑아냄
    single { get<AppDatabase>().stationDao()}

    //Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get())}

    //Api
    single{
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        //개발용일때는 로그를 다 보여주고
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        //릴리즈일때는 보여주지 않음
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
                //get 을 통해 정의된 값을 주입
            .client(get())
            .build()
            .create()
    }


    single<StationApi> { StationStorageApi(Firebase.storage) }

    //Repository
    single<StationRepository> { StationRepositoryImplement(get(), get(), get(), get(), get()) }

    //Presentation
    scope<StationsFragment> {
        //프래그먼트가 종료되게 되면 내부의 presenter도 더이상 상요, 공유 못하는
        //다만 scope 내에서는 scope된 애들끼리 얼마든지 서로 공유가 가능한 형태로 구성이 가능
        //메모리 효율적 관리
        scoped<StationsContract.Presenter> { StationsPresenter(getSource(), get())}
    }
    scope<StationsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get())

        }
    }

}