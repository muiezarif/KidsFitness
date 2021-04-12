package com.muiezarif.kidsfitness

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.muiezarif.kidsfitness.di.component.AppComponent
import com.muiezarif.kidsfitness.di.component.DaggerAppComponent
import com.muiezarif.kidsfitness.di.modules.AppModule

class ApplicationClass : MultiDexApplication(){

    private var instance: ApplicationClass? = null
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }


    companion object{
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as ApplicationClass).component
        }
    }
}