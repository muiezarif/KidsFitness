package com.muiezarif.kidsfitness.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }

    @Provides
    internal fun provideSharedPrefs(): SharedPreferences {
        return context.getSharedPreferences("ec-dash", Context.MODE_PRIVATE)
    }
}