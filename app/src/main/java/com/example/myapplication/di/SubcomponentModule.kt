package com.example.myapplication.di

import dagger.Module


@Module(subcomponents = [MainComponent::class, WordPairComponent::class])
class SubcomponentsModule {}