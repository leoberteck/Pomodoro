package com.kotlin.leo.pomodoro.mvp.interfaces

import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType

interface IMainMvp {
    interface IMainActivity{
        fun onSessionTypeChanged(newSessionType: SessionType)
        fun onPlayStateChanged(newPlayState: PlayState)
        fun onProgressChanged(progress : Float, text : String)
    }

    interface IMainPresenter{
        fun onPlayPauseClick();
        fun onStopClick();
        fun setMainActivity(mainActivity: IMainActivity)
    }
}