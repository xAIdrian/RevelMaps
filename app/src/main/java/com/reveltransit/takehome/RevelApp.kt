package com.reveltransit.takehome

import android.app.Application
import com.reveltransit.takehome.dagger.ApplicationModule
import com.reveltransit.takehome.dagger.DaggerApplicationComponent

class RevelApp: Application() {
    lateinit var appComponent: DaggerApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.appComponent = this.initDagger() as DaggerApplicationComponent
    }

    private fun initDagger() = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
}