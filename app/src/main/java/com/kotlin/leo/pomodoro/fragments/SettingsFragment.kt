package com.kotlin.leo.pomodoro.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.kotlin.leo.pomodoro.R
import com.kotlin.leo.pomodoro.databinding.FragmentSettingsBinding
import com.kotlin.leo.pomodoro.mvp.impl.SettingsPresenter
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    companion object {
        val PRESENTER = SettingsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentSettingsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container, false)
        binding.presenter = PRESENTER
        return binding.root
    }

}