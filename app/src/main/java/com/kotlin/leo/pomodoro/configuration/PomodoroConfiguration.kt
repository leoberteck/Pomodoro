package com.kotlin.leo.pomodoro.configuration

import android.content.Context
import android.content.SharedPreferences
import com.kotlin.leo.pomodoro.App
import com.kotlin.leo.pomodoro.enum.SessionType

class PomodoroConfiguration private constructor() {

    companion object {
        val instance = PomodoroConfiguration()
    }

    private val PREFERENCES_NAME = "KOTLIN_POMODORO"
    private val PREFERENCES_KEY_WORK_SESSION_LENGTH = "WORK_SESSION_LENGTH"
    private val PREFERENCES_KEY_REST_SESSION_LENGTH = "REST_SESSION_LENGTH"
    private val PREFERENCES_KEY_SESSION_STATE = "SESSION_STATE"

    private val sharedPreferences: SharedPreferences =
            App.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getWorkSessionLength(): Int {
        //return sharedPreferences.getInt(PREFERENCES_KEY_WORK_SESSION_LENGTH, 25);
        return sharedPreferences.getInt(PREFERENCES_KEY_WORK_SESSION_LENGTH, 1);
    }

    fun getRestSessionLength(): Int {
        //return sharedPreferences.getInt(PREFERENCES_KEY_REST_SESSION_LENGTH, 5);
        return sharedPreferences.getInt(PREFERENCES_KEY_REST_SESSION_LENGTH, 1);
    }

    fun getLastSessionType(): SessionType {
        return SessionType.valueOf(
                sharedPreferences.getString(PREFERENCES_KEY_SESSION_STATE, SessionType.WORK_SESSION.toString())
        )
    }

    fun setWorkSessionLength(length: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(PREFERENCES_KEY_WORK_SESSION_LENGTH, length)
        editor.apply()
    }

    fun setRestSessionLength(length: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(PREFERENCES_KEY_REST_SESSION_LENGTH, length)
        editor.apply()
    }

    fun setLastSessionState(sessionType: SessionType) {
        val editor = sharedPreferences.edit()
        editor.putString(PREFERENCES_KEY_SESSION_STATE, sessionType.toString())
        editor.apply()
    }
}