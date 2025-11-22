package org.speed.realestate

import android.app.Application
import org.speed.realestate.data.AppContainer
import org.speed.realestate.data.AppDataContainer

class RealEstateApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}