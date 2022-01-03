package com.example.myapplication.di

import dagger.Module


@Module(subcomponents = [MainComponent::class, WordPairComponent::class, WordPackageListComponent::class, WordPairCardComponent::class])
class SubcomponentsModule {}