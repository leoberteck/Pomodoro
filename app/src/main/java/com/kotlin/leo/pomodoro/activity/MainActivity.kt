package com.kotlin.leo.pomodoro.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.kotlin.leo.pomodoro.R
import com.kotlin.leo.pomodoro.enum.PlayState
import com.kotlin.leo.pomodoro.enum.SessionType
import com.kotlin.leo.pomodoro.mvp.impl.MainPresenter
import com.kotlin.leo.pomodoro.mvp.interfaces.IMainMvp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMainMvp.IMainActivity, View.OnClickListener {

    companion object {
        val presenter : IMainMvp.IMainPresenter = MainPresenter()
    }

    lateinit var binding : ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        presenter.setMainActivity(this)
        progressLayout.setOnClickListener(this)
        progressCircle.isEnabled = false
        fabStop.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        if (p0 != null) {
            when(p0.id){
                R.id.progressLayout -> presenter.onPlayPauseClick()
                R.id.fabStop -> presenter.onStopClick()
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
                runOnUiThread({
                    fabStop.visibility = View.GONE
                })
            }
        }
    }

    override fun onProgressChanged(progress: Float, text: String) {
        runOnUiThread {
            progressCircle.progress = progress
            txt_time.text = text
        }
    }

    fun stopButtonFadeIn(){
        runOnUiThread({
            if(fabStop.visibility == View.GONE){
                fabStop.postOnAnimation({fabStop.visibility = View.VISIBLE})
                val animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in)
                fabStop.startAnimation(animation)
            }
        })
    }

    fun stopButtonFadeOut(){
        runOnUiThread({
            if(fabStop.visibility == View.VISIBLE) {
                fabStop.postOnAnimation({ fabStop.visibility = View.GONE })
                val animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out)
                fabStop.startAnimation(animation)
            }
        })
    }

    fun startBlinking(){
        runOnUiThread({
            val animation = AnimationUtils.loadAnimation(this, R.anim.blink)
            txt_time.startAnimation(animation)
        })
    }

    fun stopBlinking(){
        runOnUiThread({
            txt_time.animation?.cancel()
            txt_time.clearAnimation()
        })
    }

    fun updateViewColors(color: Int) {
        runOnUiThread({
            val parsedColor = ContextCompat.getColor(this, color)
            progressCircle.circleProgressColor = parsedColor
            progressCircle.pointerColor = parsedColor
            txt_time.setTextColor(parsedColor)
            fabStop.backgroundTintList = getColorStateList(color)
        })
    }
}
