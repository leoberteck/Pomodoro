package com.kotlin.leo.pomodoro.mvp.impl

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import com.kotlin.leo.pomodoro.BR
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.mvp.interfaces.ISettingsMvp

class SettingsPresenter : BaseObservable(), ISettingsMvp.ISettingsPresenter {

    companion object {
        private const val MIN_WORK_SESSION_VALUE = 10
        private const val MIN_REST_SESSION_VALUE = 1
    }

    override var workSessionLength : Int = PomodoroConfiguration.instance.getWorkSessionLength()
        @Bindable get() = field
        set(value){
            field = if(value >= MIN_WORK_SESSION_VALUE) value else MIN_WORK_SESSION_VALUE
            notifyPropertyChanged(BR.workSessionLength)
        }
    override var restSessionLength: Int = PomodoroConfiguration.instance.getRestSessionLength()
        @Bindable get() = field
        set(value) {
            field = if(value >= MIN_REST_SESSION_VALUE) value else MIN_REST_SESSION_VALUE
            notifyPropertyChanged(BR.restSessionLength)
        }
    override var askBeforeSessionStart: Boolean = PomodoroConfiguration.instance.getAskBeforeSessionStart()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.askBeforeSessionStart)
        }
    override var vibrateWhenSessionEnds: Boolean = PomodoroConfiguration.instance.getVibrateWhenSessionEnds()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.vibrateWhenSessionEnds)
        }
    override var playSoundWhenSessionEnds: Boolean = PomodoroConfiguration.instance.getPlaySoundWhenSessionEnds()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.playSoundWhenSessionEnds)
        }
    override var doNotDisturbDuringWorkSession: Boolean = PomodoroConfiguration.instance.getDoNotDisturbDuringWorkSession()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.doNotDisturbDuringWorkSession)
        }
    override var keepScreenOn: Boolean = PomodoroConfiguration.instance.getKeepScreenOn()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.keepScreenOn)
        }

    init {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(p0: Observable?, propertyId: Int) {
                when(propertyId){
                    BR.workSessionLength -> PomodoroConfiguration.instance.setWorkSessionLength(workSessionLength)
                    BR.restSessionLength -> PomodoroConfiguration.instance.setRestSessionLength(restSessionLength)
                    BR.askBeforeSessionStart -> PomodoroConfiguration.instance.setAskBeforeSessionStart(askBeforeSessionStart)
                    BR.vibrateWhenSessionEnds -> PomodoroConfiguration.instance.setVibrateWhenSessionEnds(vibrateWhenSessionEnds)
                    BR.playSoundWhenSessionEnds -> PomodoroConfiguration.instance.setPlaySoundWhenSessionEnds(playSoundWhenSessionEnds)
                    BR.doNotDisturbDuringWorkSession -> PomodoroConfiguration.instance.setDoNotDisturbDuringWorkSession(doNotDisturbDuringWorkSession)
                    BR.keepScreenOn -> PomodoroConfiguration.instance.setKeepScreenOn(keepScreenOn)
                }
            }
        })
    }
}