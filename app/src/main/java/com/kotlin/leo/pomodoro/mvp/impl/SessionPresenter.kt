package com.kotlin.leo.pomodoro.mvp.impl

import android.content.Context
import android.databinding.BaseObservable
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp.ISessionFragment
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp.ISessionPresenter
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.Temporal
import java.util.*
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.kotlin.leo.pomodoro.App
import com.kotlin.leo.pomodoro.R


class SessionPresenter : BaseObservable(), ISessionPresenter {

    private val restSessionLength
        get() = PomodoroConfiguration.instance.getRestSessionLength()
    private val workSessionLength
        get() = PomodoroConfiguration.instance.getWorkSessionLength()
    private var lastSessionType
        get() = PomodoroConfiguration.instance.getLastSessionType()
        set(value) = PomodoroConfiguration.instance.setLastSessionState(value)
    private val askBeforeSessionStart
        get() = PomodoroConfiguration.instance.getAskBeforeSessionStart()
    private val vibrateWhenSessionEnd
        get() = PomodoroConfiguration.instance.getVibrateWhenSessionEnds()
    private val playSoundWhenSessionEnd
        get() = PomodoroConfiguration.instance.getPlaySoundWhenSessionEnds()
    private val doNotDisturbDuringWorkSession
        get() = PomodoroConfiguration.instance.getDoNotDisturbDuringWorkSession()
    private val keepScreenOn
        get() = PomodoroConfiguration.instance.getKeepScreenOn()

    private var sessionFragmentWeakReference: WeakReference<ISessionFragment>? = null
    private var timer = Timer()
    private var bean : SessionBean? = null

    init {
        resetSession()
    }

    override fun onPlayPauseClick() {
        if(bean?.playState == PlayState.PLAYING) {
            pauseSession()
        }else {
            startSession()
        }
    }

    override fun onStopClick() {
        stopSession()
    }

    override fun setFragment(sessionFragment: ISessionFragment){
        sessionFragmentWeakReference = WeakReference(sessionFragment)
        //Atualiza os valores da view, quando ela for construída
        val _bean =  bean
        if(_bean != null){
            sessionFragmentWeakReference?.get()?.onSessionTypeChanged(_bean.sessionType)
            sessionFragmentWeakReference?.get()?.onPlayStateChanged(_bean.playState)
            sessionFragmentWeakReference?.get()?.onProgressChanged(_bean.progress, _bean.time)
        }
    }

    private fun resetSession(){
        val newSessionType = when(lastSessionType){
            SessionType.REST_SESSION -> SessionType.WORK_SESSION
            SessionType.WORK_SESSION -> SessionType.REST_SESSION
            else -> SessionType.WORK_SESSION
        }

        lastSessionType = newSessionType

        val timeLeft = Calendar.getInstance()

        timeLeft.set(Calendar.HOUR, 0)
        timeLeft.set(Calendar.MINUTE, 0)
        timeLeft.set(Calendar.SECOND, 0)
        timeLeft.set(Calendar.MILLISECOND, 0)

        timeLeft.set(Calendar.MINUTE, when(newSessionType){
            SessionType.WORK_SESSION -> workSessionLength
            else -> restSessionLength
        })

        bean = SessionBean(
            {sender, propertyId -> onPropertyChanged(sender, propertyId)}
            , newSessionType
            , PlayState.STOPPED
            , timeLeft
        )
        onPropertyChanged(bean!!, PR.sessionType)
        onPropertyChanged(bean!!, PR.sessionProgress)
    }

    private fun startSession(){
        if(bean == null){
            resetSession()
        }
        bean?.playState = PlayState.PLAYING
        timer.schedule(getTimerTask(), 0)
    }

    private fun pauseSession(){
        timer.cancel()
        timer.purge()
        timer = Timer()
        bean?.playState = PlayState.PAUSED
    }

    private fun stopSession(){
        timer.cancel()
        timer.purge()
        timer = Timer()
        bean?.playState = PlayState.STOPPED
        resetSession()
    }

    private fun getTimerTask() : TimerTask{
        return object : TimerTask(){
            override fun run() {
                if(bean?.playState == PlayState.PLAYING) {
                    bean?.tick()
                    //If session is still valid
                    if(bean?.progress != 0F){
                        timer.schedule(getTimerTask(), 1000)
                    //If session expired
                    }else{
                        stopSession()
                        if(vibrateWhenSessionEnd){
                            sessionFragmentWeakReference?.get()?.vibrate(300)
                        }
                        if(playSoundWhenSessionEnd){
                            sessionFragmentWeakReference?.get()?.notifySessionEnded(
                                R.string.session_ended, R.string.your_session_has_come_to_an_end
                            )
                        }
                        if(askBeforeSessionStart){
                            sessionFragmentWeakReference?.get()?.showStartNextSessionConfirmation()
                        }else{
                            startSession()
                        }
                    }
                }
            }
        }
    }

    private fun onPropertyChanged(sender : SessionBean, propertyId: PR){
        when(propertyId){
            PR.sessionType -> {
                sessionFragmentWeakReference?.get()?.onSessionTypeChanged(sender.sessionType)
            }
            PR.sessionPlayState -> {
                sessionFragmentWeakReference?.get()?.onPlayStateChanged(sender.playState)
            }
            PR.sessionProgress -> {
                sessionFragmentWeakReference?.get()?.onProgressChanged(sender.progress, sender.time)
            }
        }
    }

    private class SessionBean(
            val onPropertyChanged: (sender : SessionBean, propertyId: PR) -> Unit
            , mSessionType: SessionType
            , mPlayState: PlayState
            , mTimeLeft: Calendar) {

        val sessionType : SessionType = mSessionType
        var playState : PlayState = mPlayState
            set(value){
                field = value
                onPropertyChanged(this, PR.sessionPlayState)
            }

        private val short = SimpleDateFormat("mm : ss")
        private val long = SimpleDateFormat("hh : mm : ss")
        var time : String = format(mTimeLeft)
            private set
        var progress : Float = 100F
            private set

        private val timeLeft : Calendar = mTimeLeft
        private val totalSeconds = (timeLeft.get(Calendar.HOUR) * 3600) + (mTimeLeft.get(Calendar.MINUTE) * 60)

        fun tick(){
            timeLeft.add(Calendar.SECOND, -1)
            val totalSecondsLeft = (timeLeft.get(Calendar.HOUR) * 3600) + (timeLeft.get(Calendar.MINUTE) * 60) + timeLeft.get(Calendar.SECOND).toFloat()
            progress = (totalSecondsLeft * 100) / totalSeconds
            time = format(timeLeft)
            onPropertyChanged(this, PR.sessionProgress)
        }

        private fun format(value : Calendar) : String{
            return if(value.get(Calendar.HOUR) > 0) long.format(value.time) else short.format(value.time)
        }
    }

    private enum class PR{
        sessionType
        , sessionPlayState
        , sessionProgress
    }
}
