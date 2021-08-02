package kr.ac.konkuk.subwayinfo.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferenceManager(
    private val sharedPreferences: SharedPreferences
) : PreferenceManager {
    //반환하는 Long 이 nullable 하도록 정의
    override fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, INVALID_LONG_VALUE)

        return if (value == INVALID_LONG_VALUE) {
            null
        } else {
            value
        }
    }

    override fun putLong(key: String, value: Long) =
        sharedPreferences.edit { putLong(key, value) }

    companion object {
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
    }

}