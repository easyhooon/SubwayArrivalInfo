package kr.ac.konkuk.subwayinfo.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// 단순히 두가지의 Entity 만 존재하는 것이 아닌 서로의  ID를 mapping 해주는 채로 가지고 있는
// cross reference table 이 필요
data class StationWithSubwaysEntity(
    // @Embedded 주석을 사용하여 테이블 내의 하위 필드로 분해하려고 하는 객체를 나타낼 수 있습니다. 그러면 다른 개별 열을 쿼리하듯 삽입된 필드를 쿼리할 수 있습니다.
    @Embedded val station: StationEntity,
    @Relation(
        parentColumn = "stationName",
        entityColumn = "subwayId",
        associateBy = Junction(StationSubwayCrossRefEntity::class)
    )
    val subways: List<SubwayEntity>
)