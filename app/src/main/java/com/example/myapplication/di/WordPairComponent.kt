package com.example.myapplication.di
import com.example.myapplication.presentaion.view.WordPairListFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent()
interface WordPairComponent {
    fun inject(fragment: WordPairListFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordPairComponent

    }
}