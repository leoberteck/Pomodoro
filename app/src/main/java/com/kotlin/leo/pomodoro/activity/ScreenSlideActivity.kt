package com.kotlin.leo.pomodoro.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.kotlin.leo.pomodoro.R
import com.kotlin.leo.pomodoro.fragments.SessionFragment
import com.kotlin.leo.pomodoro.fragments.SettingsFragment

import kotlinx.android.synthetic.main.activity_screen_slide.*

class ScreenSlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_slide)
        viewPager.adapter = ScreenSlideFragmentAdapter(supportFragmentManager)
        viewPager.currentItem = 1
    }

    class ScreenSlideFragmentAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> SettingsFragment()
                1 -> SessionFragment()
                else -> SessionFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
