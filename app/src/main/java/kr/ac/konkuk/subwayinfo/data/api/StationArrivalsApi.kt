package kr.ac.konkuk.subwayinfo.data.api

import kr.ac.konkuk.subwayinfo.BuildConfig
import kr.ac.konkuk.subwayinfo.data.api.response.RealtimeStationArrivals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationArrivalsApi {

    @GET("api/subway/${BuildConfig.SEOUL_API_ACCESS_KEY}/json/realtimeStationArrival/0/100/{stationName}")
    suspend fun getRealtimeStationArrivals(@Path("stationName") stationName: String): Response<RealtimeStationArrivals>

}