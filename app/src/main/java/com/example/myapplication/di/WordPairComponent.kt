package com.example.myapplication.di

import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.view.WORD_PACKAGE_ID
import com.example.myapplication.presentaion.view.WordPairListFragment
import dagger.*
import java.lang.IllegalArgumentException

@ActivityScope
@Subcomponent(
    modules = [WordPairListFragmentModule::class]
)
interface WordPairComponent {
    fun inject(fragment: WordPairListFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: WordPairListFragment): WordPairComponent
    }

}

@Module
class WordPairListFragmentModule {

    @Provides
    fun provideWordPackageInfoModel(fragment: WordPairListFragment): WordPackageInfoModel {
        return fragment.arguments?.getParcelable(WORD_PACKAGE_ID)
            ?: throw IllegalArgumentException("Parcelable argument not been provided")
    }
}