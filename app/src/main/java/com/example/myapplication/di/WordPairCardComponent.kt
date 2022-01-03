package com.example.myapplication.di

import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.view.CARD_PACKAGE_TAG
import com.example.myapplication.presentaion.view.WORD_PACKAGE_ID
import com.example.myapplication.presentaion.view.WordPairCardFragment
import com.example.myapplication.presentaion.view.WordPairListFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.IllegalArgumentException

@ActivityScope
@Subcomponent(
    modules = [WordPairCardFragmentModule::class]
)
interface WordPairCardComponent {

    fun inject(fragment: WordPairCardFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: WordPairCardFragment): WordPairCardComponent

    }
}

@Module
class WordPairCardFragmentModule {

    @Provides
    fun provideWordPackage(fragment: WordPairCardFragment): WordPackage {
        return fragment.arguments?.getParcelable(CARD_PACKAGE_TAG)  ?: throw IllegalArgumentException("Parcelable argument not been provided")
    }
}