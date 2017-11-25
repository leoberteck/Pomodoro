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
    private val PREFERENCES_KEY_ASK_BEFORE_SESSION_START = "ASK_BEFORE_SESSION_START"
    private val PREFERENCES_KEY_VIBRATE = "VIBRATE"
    private val PREFERENCES_KEY_PLAY_SOUND = "PLAY_SOUND"
    private val PREFERENCES_KEY_DO_NOT_DISTURB = "DO_NOT_DISTURB"
    private val PREFERENCES_KEY_KEEP_SCREEN_ON = "KEEP_SCREEN_ON"


    private val sharedPreferences: SharedPreferences =
            App.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getWorkSessionLength(): Int {
        return sharedPreferences.getInt(PREFERENCES_KEY_WORK_SESSION_LENGTH, 25)
    }

    fun getRestSessionLength(): Int {
        return sharedPreferences.getInt(PREFERENCES_KEY_REST_SESSION_LENGTH, 5)
    }

    fun getLastSessionType(): SessionType {
        return SessionType.valueOf(
                sharedPreferences.getString(PREFERENCES_KEY_SESSION_STATE, SessionType.WORK_SESSION.toString())
        )
    }

    fun getAskBeforeSessionStart() : Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_KEY_ASK_BEFORE_SESSION_START, false)
    }

    fun getVibrateWhenSessionEnds() : Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_KEY_VIBRATE, false)
    }

    fun getPlaySoundWhenSessionEnds() : Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_KEY_PLAY_SOUND, false)
    }

    fun getDoNotDisturbDuringWorkSession() : Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_KEY_DO_NOT_DISTURB, false)
    }

    fun getKeepScreenOn() : Boolean {
        return sharedPreferences.getBoolean(PREFERENCES_KEY_KEEP_SCREEN_ON, false)
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

    fun setAskBeforeSessionStart(value : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFERENCES_KEY_ASK_BEFORE_SESSION_START, value)
        editor.apply()
    }

    fun setVibrateWhenSessionEnds(value : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFERENCES_KEY_VIBRATE, value)
        editor.apply()
    }

    fun setPlaySoundWhenSessionEnds(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFERENCES_KEY_PLAY_SOUND, value)
        editor.apply()
    }

    fun setDoNotDisturbDuringWorkSession(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFERENCES_KEY_DO_NOT_DISTURB, value)
        editor.apply()
    }

    fun setKeepScreenOn(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREFERENCES_KEY_KEEP_SCREEN_ON, value)
        editor.apply()
    }
}