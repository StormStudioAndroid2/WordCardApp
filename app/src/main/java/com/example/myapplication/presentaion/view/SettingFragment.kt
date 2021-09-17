package com.example.myapplication.presentaion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.adapter.WordPackageAdapter
import com.example.myapplication.presentaion.adapter.WordPackageAdapterCallback
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.WordPackageListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import javax.inject.Inject

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val switch = view.findViewById<SwitchCompat>(R.id.dark_setting_switch)
        setCheckedSwitch(switch)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                val sharedPreferences =
                    context?.getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor?.putBoolean("isDarkModeOn", false)
                editor?.apply()
            } else {
                val sharedPreferences =
                    context?.getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor?.putBoolean("isDarkModeOn", true)
                editor?.apply()
            }
        }
        return view
    }

    private fun setCheckedSwitch(switch: SwitchCompat) {
        val sharedPreferences = context?.getSharedPreferences(
            "sharedPrefs", AppCompatActivity.MODE_PRIVATE
        )
        val isDarkModeOn = sharedPreferences?.getBoolean("isDarkModeOn", false)
        if (isDarkModeOn == true) {
            switch.isChecked = true
        }
    }
}