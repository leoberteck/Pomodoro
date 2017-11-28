package com.kotlin.leo.pomodoro.mvp.interfaces

import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType

interface IMainMvp {
    interface ISessionFragment {
        fun onSessionTypeChanged(newSessionType: SessionType)
        fun onPlayStateChanged(newPlayState: PlayState)
        fun onProgressChanged(progress : Float, text : String)
    }

    interface ISessionPresenter {
        fun onPlayPauseClick()
        fun onStopClick()
        fun setFragment(sessionFragment: ISessionFragment)
    }
}