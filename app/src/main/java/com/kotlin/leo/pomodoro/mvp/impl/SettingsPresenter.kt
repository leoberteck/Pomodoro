package com.kotlin.leo.pomodoro.mvp.impl

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.mvp.interfaces.ISettingsMvp

/**
 * Created by leo on 10/15/17.
 */
class SettingsPresenter : BaseObservable(), ISettingsMvp.ISettingsPresenter {
    var workSessionLength : Int = PomodoroConfiguration.instance.getWorkSessionLength()
        @Bindable get() = field
        set(value)
}