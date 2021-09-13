package com.example.myapplication.di

import com.example.myapplication.presentaion.view.PackageActivity
import com.example.myapplication.presentaion.view.WordPairListFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent()
interface PackageComponent {
    fun inject(activity: PackageActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PackageComponent

    }
}