package com.example.trackthis

import android.app.Application
import com.example.trackthis.data.database.AppContainer
import com.example.trackthis.data.database.AppDataContainer


class TrackApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
