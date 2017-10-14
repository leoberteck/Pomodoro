package com.kotlin.leo.pomodoro.mvp.impl

import android.databinding.BaseObservable
import com.kotlin.leo.pomodoro.configuration.PomodoroConfiguration
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp.IMainActivity
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp.IMainPresenter
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter() : BaseObservable(), IMainPresenter {

    private val restSessionLength = PomodoroConfiguration.instance.getRestSessionLength()
    private val workSessionLength = PomodoroConfiguration.instance.getWorkSessionLength()
    private var lastSessionType = PomodoroConfiguration.instance.getLastSessionType()

    private var mainActivityWeakReference : WeakReference<IMainActivity>? = null
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

    override fun setMainActivity(mainActivity: IMainActivity){
        mainActivityWeakReference = WeakReference(mainActivity)
        //Atualiza os valores da view, quando ela for construída
        val _bean =  bean
        if(_bean != null){
            mainActivityWeakReference?.get()?.onSessionTypeChanged(_bean.sessionType)
            mainActivityWeakReference?.get()?.onPlayStateChanged(_bean.playState)
            mainActivityWeakReference?.get()?.onProgressChanged(_bean.progress, _bean.time)
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
        timeLeft.set(Calendar.MINUTE, when(newSessionType){
            SessionType.WORK_SESSION -> workSessionLength
            else -> restSessionLength
        })
        timeLeft.set(Calendar.SECOND, 0)
        timeLeft.set(Calendar.MILLISECOND, 0)

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
                        stopSession();
                        startSession();
                    }
                }
            }
        }
    }

    private fun onPropertyChanged(sender : SessionBean, propertyId: PR) : Unit{
        when(propertyId){
            PR.sessionType -> {
                mainActivityWeakReference?.get()?.onSessionTypeChanged(sender.sessionType)
            }
            PR.sessionPlayState -> {
                mainActivityWeakReference?.get()?.onPlayStateChanged(sender.playState)
            }
            PR.sessionProgress -> {
                mainActivityWeakReference?.get()?.onProgressChanged(sender.progress, sender.time)
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

        private val format = SimpleDateFormat("mm : ss")
        var time : String = format.format(mTimeLeft.time)
            private set
        var progress : Float = 100F
            private set

        private val timeLeft : Calendar = mTimeLeft
        private val totalSeconds = mTimeLeft.get(Calendar.MINUTE) * 60

        fun tick(){
            timeLeft.add(Calendar.SECOND, -10)
            val totalSecondsLeft = (timeLeft.get(Calendar.MINUTE) * 60) + timeLeft.get(Calendar.SECOND).toFloat()
            progress = (totalSecondsLeft * 100) / totalSeconds
            time = format.format(timeLeft.time)
            onPropertyChanged(this, PR.sessionProgress)
        }
    }

    private enum class PR{
        sessionType
        , sessionPlayState
        , sessionProgress
    }
}
