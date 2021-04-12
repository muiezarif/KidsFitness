package com.muiezarif.kidsfitness.di.modules

import androidx.lifecycle.ViewModelProvider
import com.muiezarif.kidsfitness.utils.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}