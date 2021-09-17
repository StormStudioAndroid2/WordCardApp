package com.example.myapplication.di

import com.example.myapplication.presentaion.view.WordPackageListFragment
import com.example.myapplication.presentaion.view.WordPairListFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent()
interface WordPackageListComponent {
    fun inject(fragment: WordPackageListFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordPackageListComponent

    }
}