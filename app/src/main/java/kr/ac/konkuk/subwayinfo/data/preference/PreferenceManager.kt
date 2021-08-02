package kr.ac.konkuk.subwayinfo.data.preference

interface PreferenceManager {

    //timestamp 를 찍고 이전 timestamp 를 가져오는 작업 필요
    fun getLong(key: String): Long?

    fun putLong(key: String, value: Long)
}