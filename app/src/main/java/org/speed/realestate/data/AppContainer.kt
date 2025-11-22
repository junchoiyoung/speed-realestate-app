package org.speed.realestate.data

import android.content.Context

interface AppContainer {
    val realEstateRepository : RealEstateRepository
}

class AppDataContainer(context : Context) : AppContainer {
    override val realEstateRepository : RealEstateRepository by lazy {
        OfflineRealEstateRepository(
            RealEstateDatabase.getDatabase(context).realEstateDao(),
            RealEstateDatabase.getDatabase(context).tagDao())
    }
}