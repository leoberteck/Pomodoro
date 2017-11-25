package com.kotlin.leo.pomodoro.mvp.interfaces

import android.databinding.Bindable
import android.databinding.Observable
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration


interface ISettingsMvp {
    interface ISettingsFragment{

    }

    interface ISettingsPresenter : Observable {
        var workSessionLength: Int
        var restSessionLength : Int
        var askBeforeSessionStart : Boolean
        var vibrateWhenSessionEnds : Boolean
        var playSoundWhenSessionEnds : Boolean
        var doNotDisturbDuringWorkSession : Boolean
        var keepScreenOn : Boolean
    }
}