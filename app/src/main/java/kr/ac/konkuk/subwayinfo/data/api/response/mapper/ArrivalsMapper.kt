package kr.ac.konkuk.subwayinfo.data.api.response.mapper

import kr.ac.konkuk.subwayinfo.data.api.response.RealtimeArrival
import kr.ac.konkuk.subwayinfo.domain.ArrivalInformation
import kr.ac.konkuk.subwayinfo.domain.Subway
import java.text.SimpleDateFormat
import java.util.*

private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.'0'", Locale.KOREA)
private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

private const val INVALID_FIELD = "-"


//http://swopenapi.seoul.go.kr/api/subway/sample/json/realtimeStationArrival/1/5/%EB%B0%B1%EC%84%9D# 참고

fun RealtimeArrival.toArrivalInformation(): ArrivalInformation =
    ArrivalInformation(
        subway = Subway.findById(subwayId),
        direction = trainLineNm?.split("-")
            ?.get(1)
            ?.trim() //좌우 여백이 깎인 문자열이 반환
            ?: INVALID_FIELD,
        //마지막 종착역역
        destination = bstatnNm ?: INVALID_FIELD,
        message = arvlMsg2
                //현재 역과 이름이 동일하면 "당역"으로 교체
            ?.replace(statnNm.toString(), "당역")
                //정규식을 통해 대괄호를 지움
            ?.replace("[\\[\\]]".toRegex(),"") ?: INVALID_FIELD,
        updatedAt = recptnDt
            ?.let { apiDateFormat.parse(it)}
            ?.let { dateFormat.format(it)}
            ?: INVALID_FIELD
    )

fun List<RealtimeArrival>.toArrivalInformation(): List<ArrivalInformation> =
    map{it.toArrivalInformation()}