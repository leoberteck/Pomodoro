package com.kotlin.leo.pomodoro.mvp.interfaces

import android.support.annotation.StringRes
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType

interface IMainMvp {
    interface ISessionFragment {
        fun onSessionTypeChanged(newSessionType: SessionType)
        fun onPlayStateChanged(newPlayState: PlayState)
        fun onProgressChanged(progress : Float, text : String)
        fun showStartNextSessionConfirmation()
        fun vibrate(milliseconds : Long)
        fun notifySessionEnded(@StringRes title: Int, @StringRes message: Int)
    }

    interface ISessionPresenter {
        fun onPlayPauseClick()
        fun onStopClick()
        fun setFragment(sessionFragment: ISessionFragment)
    }
}