package com.kotlin.leo.pomodoro.mvp.impl

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import com.kotlin.leo.pomodoro.BR
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.mvp.interfaces.ISettingsMvp


class SettingsPresenter : BaseObservable, ISettingsMvp.ISettingsPresenter {

    private val minWorkSessionValue = 10
    private val minRestSessionValue = 1

    constructor(){
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

    override var workSessionLength : Int = PomodoroConfiguration.instance.getWorkSessionLength()
        @Bindable get() = field
        set(value){
            field = if(value >= minWorkSessionValue) value else minWorkSessionValue
            notifyPropertyChanged(BR.workSessionLength)
        }
    override var restSessionLength: Int = PomodoroConfiguration.instance.getRestSessionLength()
        @Bindable get() = field
        set(value) {
            field = if(value >= minRestSessionValue) value else minRestSessionValue
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
}