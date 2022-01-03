package com.example.myapplication.di

import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.presentaion.view.*
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.IllegalArgumentException


@Subcomponent(
    modules = [WordPairCardStatisticFragmentModule::class]
)
interface WordPairCardStatisticComponent {

    fun inject(fragment: WordPairCardStatisticFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: WordPairCardStatisticFragment): WordPairCardStatisticComponent

    }
}

@Module
class WordPairCardStatisticFragmentModule {

    @Provides
    fun provideResultStatistic(fragment: WordPairCardStatisticFragment): ResultStatistic {
        return fragment.arguments?.getParcelable(CARD_STATISTIC_TAG)
            ?: throw IllegalArgumentException("Parcelable argument not been provided")
    }
}