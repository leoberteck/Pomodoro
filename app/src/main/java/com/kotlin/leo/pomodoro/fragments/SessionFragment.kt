package com.kotlin.leo.pomodoro.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.kotlin.leo.pomodoro.R
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType
import com.kotlin.leo.pomodoro.mvp.impl.SessionPresenter
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp
import kotlinx.android.synthetic.main.fragment_pomodoro.*

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
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when(p0.id){
                R.id.progressLayout -> PRESENTER.onPlayPauseClick()
                R.id.fabStop -> PRESENTER.onStopClick()
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
        activity.runOnUiThread {
            progressCircle.progress = progress
            txt_time.text = text
        }
    }

    private fun stopButtonFadeIn(){
        activity.runOnUiThread({
            if(fabStop.visibility == View.GONE){
                fabStop.postOnAnimation({fabStop.visibility = View.VISIBLE})
                val animation = AnimationUtils.loadAnimation(activity, R.anim.abc_fade_in)
                fabStop.startAnimation(animation)
            }
        })
    }

    private fun stopButtonFadeOut(){
        activity.runOnUiThread({
            if(fabStop.visibility == View.VISIBLE) {
                fabStop.postOnAnimation({ fabStop.visibility = View.GONE })
                val animation = AnimationUtils.loadAnimation(activity, R.anim.abc_fade_out)
                fabStop.startAnimation(animation)
            }
        })
    }

    private fun startBlinking(){
        activity.runOnUiThread({
            val animation = AnimationUtils.loadAnimation(activity, R.anim.blink)
            txt_time.startAnimation(animation)
        })
    }

    private fun stopBlinking(){
        activity.runOnUiThread({
            txt_time.animation?.cancel()
            txt_time.clearAnimation()
        })
    }

    private fun updateViewColors(color: Int) {
        activity.runOnUiThread({
            val parsedColor = ContextCompat.getColor(activity, color)
            progressCircle.circleProgressColor = parsedColor
            progressCircle.pointerColor = parsedColor
            txt_time.setTextColor(parsedColor)
            fabStop.backgroundTintList = activity.getColorStateList(color)
        })
    }
}