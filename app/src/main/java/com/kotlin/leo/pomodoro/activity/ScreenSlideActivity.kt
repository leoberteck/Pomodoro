package com.kotlin.leo.pomodoro.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
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

        ensureAllPermissions()
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

    private fun ensureAllPermissions() {
        val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.VIBRATE
        )
        permissions.forEach { permission -> checkPermission(permission, 0) }
    }

    private fun checkPermission(permission : String, resultCode : Int){
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), resultCode)
        }
    }
}
