package com.kotlin.leo.pomodoro.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.kotlin.leo.pomodoro.App
import com.kotlin.leo.pomodoro.R
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType
import com.kotlin.leo.pomodoro.mvp.impl.SessionPresenter
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp
import kotlinx.android.synthetic.main.fragment_pomodoro.*
import android.media.RingtoneManager
import android.media.Ringtone
import android.support.v4.app.NotificationCompat
import com.kotlin.leo.pomodoro.R.mipmap.ic_launcher
import android.app.NotificationManager
import android.support.annotation.StringRes


class SessionFragment : Fragment(), IMainMvp.ISessionFragment, View.OnClickListener {

    companion object {
        val PRESENTER: IMainMvp.ISessionPresenter = SessionPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PRESENTER.setFragment(this)
        progressLayout.setOnClickListener(this)
        progressCircle.isEnabled = false
        fabStop.setOnClickListener(this)
        btnStartNext.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when(view.id){
                R.id.progressLayout -> PRESENTER.onPlayPauseClick()
                R.id.fabStop -> PRESENTER.onStopClick()
                R.id.btnStartNext -> PRESENTER.onPlayPauseClick()
            }
        }
    }

    override fun onSessionTypeChanged(newSessionType: SessionType) {
        var color = when(newSessionType){
            SessionType.WORK_SESSION -> R.color.colorAccent
            else -> R.color.colorAccentInverted
        }
        updateViewColors(color)
    }

    override fun onPlayStateChanged(newPlayState: PlayState) {
        when(newPlayState){
            PlayState.PLAYING -> {
                btnNextSessionFadeOut()
                stopButtonFadeOut()
                stopBlinking()
            }
            PlayState.PAUSED -> {
                stopButtonFadeIn()
                startBlinking()
            }
            PlayState.STOPPED -> {
                activity.runOnUiThread({
                    fabStop.visibility = View.GONE
                })
            }
        }
    }

    override fun onProgressChanged(progress: Float, text: String) {
        activity?.runOnUiThread {
            progressCircle.progress = progress
            txt_time.text = text
        }
    }

    override fun showStartNextSessionConfirmation() {
        btnNextSessionFadeIn()
    }

    override fun vibrate(milliseconds: Long) {
        val v = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(milliseconds)
        }
    }

    override fun notifySessionEnded(@StringRes title: Int, @StringRes message : Int){
        try {
            val alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            val mBuilder =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationCompat.Builder(activity, NotificationChannel.DEFAULT_CHANNEL_ID)
                    } else {
                        NotificationCompat.Builder(activity)
                    }

            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(title))
                .setContentText(getString(message))
                .setAutoCancel(true)
                .setTimeoutAfter(5000)
                .setSound(alertSound)
                .build()

            val mNotificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(0, mBuilder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun btnNextSessionFadeIn(){
        viewFadeIn(btnStartNext)
    }

    private fun btnNextSessionFadeOut(){
        viewFadeOut(btnStartNext)
    }

    private fun stopButtonFadeIn(){
        viewFadeIn(fabStop)
    }

    private fun stopButtonFadeOut(){
        viewFadeOut(fabStop)
    }

    private fun viewFadeIn(view: View){
        activity?.runOnUiThread {
            if(view.visibility == View.GONE){
                view.postOnAnimation {view.visibility = View.VISIBLE}
                val animation = AnimationUtils.loadAnimation(activity, R.anim.abc_fade_in)
                view.startAnimation(animation)
            }
        }
    }

    private fun viewFadeOut(view: View){
        activity?.runOnUiThread {
            if(view.visibility == View.VISIBLE){
                view.postOnAnimation {view.visibility = View.GONE}
                val animation = AnimationUtils.loadAnimation(activity, R.anim.abc_fade_out)
                view.startAnimation(animation)
            }
        }
    }

    private fun startBlinking(){
        activity?.runOnUiThread({
            val animation = AnimationUtils.loadAnimation(activity, R.anim.blink)
            txt_time.startAnimation(animation)
        })
    }

    private fun stopBlinking(){
        activity?.runOnUiThread({
            txt_time.animation?.cancel()
            txt_time.clearAnimation()
        })
    }

    private fun updateViewColors(color: Int) {
        activity?.runOnUiThread({
            val parsedColor = ContextCompat.getColor(activity, color)
            progressCircle.circleProgressColor = parsedColor
            progressCircle.pointerColor = parsedColor
            txt_time.setTextColor(parsedColor)
            fabStop.backgroundTintList = activity.getColorStateList(color)
            btnStartNext.setTextColor(parsedColor)
        })
    }
}