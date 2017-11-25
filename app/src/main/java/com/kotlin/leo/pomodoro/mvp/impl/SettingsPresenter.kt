package com.kotlin.leo.pomodoro.mvp.impl

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.mvp.interfaces.ISettingsMvp


class SettingsPresenter : BaseObservable(), ISettingsMvp.ISettingsPresenter {
    override var workSessionLength : Int = PomodoroConfiguration.instance.getWorkSessionLength()
        @Bindable get() = field
        set(value){
            field = value
            notifyPropertyChanged(0)
        }
    override var restSessionLength: Int = PomodoroConfiguration.instance.getRestSessionLength()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
    override var askBeforeSessionStart: Boolean = PomodoroConfiguration.instance.getAskBeforeSessionStart()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
    override var vibrateWhenSessionEnds: Boolean = PomodoroConfiguration.instance.getVibrateWhenSessionEnds()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
    override var playSoundWhenSessionEnds: Boolean = PomodoroConfiguration.instance.getPlaySoundWhenSessionEnds()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
    override var doNotDisturbDuringWorkSession: Boolean = PomodoroConfiguration.instance.getDoNotDisturbDuringWorkSession()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
    override var keepScreenOn: Boolean = PomodoroConfiguration.instance.getKeepScreenOn()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(0)
        }
}