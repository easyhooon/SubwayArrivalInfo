package kr.ac.konkuk.subwayinfo.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//entity 가 아닌 domain 으로 보기 편한 형태로 재정의

//Navigation의 argumet의 model로 들어가기 위해선 parcelize 해야한다.

@Parcelize
data class Station(
    val name: String,
    val isFavorited: Boolean,
    val connectedSubways: List<Subway>
) : Parcelable