package com.reveltransit.takehome.dagger

import android.app.Application
import android.content.Context
import com.reveltransit.takehome.RevelApp
import com.reveltransit.takehome.helper.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

//    @Provides
//    @Singleton
//    fun provideResourceProvider(context: Context) = ResourceProvider(context)
}