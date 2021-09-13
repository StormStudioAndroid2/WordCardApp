package com.example.myapplication.di

import com.example.myapplication.presentaion.view.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent()
interface MainComponent {
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent

    }
}