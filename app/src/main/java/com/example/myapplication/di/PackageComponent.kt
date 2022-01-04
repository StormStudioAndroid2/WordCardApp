package com.example.myapplication.di

import android.content.Intent
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.view.*
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.IllegalArgumentException

@ActivityScope
@Subcomponent
interface PackageComponent {
    fun inject(activity: PackageActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PackageComponent

    }
}
