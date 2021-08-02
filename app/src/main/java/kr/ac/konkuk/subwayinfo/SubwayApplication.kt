package kr.ac.konkuk.subwayinfo

import android.app.Application
import kr.ac.konkuk.subwayinfo.di.appModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//koin 초기화 작업
//Manifest 에 android:name=".SubwayApplication" 이라고 추가해야 기본 application 이 아닌 상속 받은 subwayApplication 을 실행함
class SubwayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                //개발시에만 로그를 찍도록
                if (BuildConfig.DEBUG) {
                    Level.DEBUG
                } else {
                    Level.NONE
                }
            )
            androidContext(this@SubwayApplication)
            modules(appModule)
        }
    }
}