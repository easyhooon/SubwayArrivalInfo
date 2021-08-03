package kr.ac.konkuk.subwayinfo.data.db.entity.mapper

import kr.ac.konkuk.subwayinfo.data.db.entity.StationEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.StationWithSubwaysEntity
import kr.ac.konkuk.subwayinfo.data.db.entity.SubwayEntity
import kr.ac.konkuk.subwayinfo.domain.Station
import kr.ac.konkuk.subwayinfo.domain.Subway

//확장 함수
fun StationWithSubwaysEntity.toStation() =
    Station(
        name = station.stationName,
        isFavorited = station.isFavorited,
        connectedSubways = subways.toSubways()
    )

fun Station.toStationEntity() =
    StationEntity(
        stationName = name,
        isFavorited = isFavorited,
    )

fun List<StationWithSubwaysEntity>.toStations() = map {it.toStation()}

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId)}