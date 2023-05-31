package com.vijanthi.computervathiyar.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vijanthi.computervathiyar.data.model.AdViewReport
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefManager @Inject constructor(@ApplicationContext val applicationContext: Context) {

    private var prefCourseRead: String = "course_chapter_read"
    private var prefAdViewReport: String = "ad_view_report"
    private val gson by lazy { Gson() }

    private val preference: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            "_preferenceNameStr",
            Context.MODE_PRIVATE
        )
    }

    @SuppressLint("ApplySharedPref")
    private fun putString(prefName: String, value: String?) {
        preference.edit().apply {
            putString(prefName, value)
            commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun putInt(prefName: String, value: Int?, async: Boolean = false) {
        preference.edit().run {
            putInt(prefName, value)
            if (async)
                apply()
            else
                commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun putPrefBoolean(prefName: String, value: Boolean) {
        preference.edit().run {
            putBoolean(prefName, value)
            apply()
        }
    }

    var courseChapterRead: List<String>
        get() {
            val ccr = preference.getString(prefCourseRead, "") ?: ""
            return ccr.split(",")
        }
        set(value) {
            putString(prefCourseRead, value.joinToString(","))
        }

    var adViewReport: AdViewReport?
        get() {
            val avr = preference.getString(prefAdViewReport, "")
            if (avr.isNullOrEmpty()) {
                return null
            }
            return gson.fromJson<AdViewReport>(avr)
        }
        set(value) {
            putString(prefAdViewReport, if (value == null)  "" else gson.toJson(value))
        }
}