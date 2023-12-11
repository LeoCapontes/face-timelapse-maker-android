package com.jlpc.facetimelapsemaker

import com.jlpc.facetimelapsemaker.model.PhotoDatabase
import android.app.Application
import androidx.room.Room

class FaceTimelapseMakerApp: Application() {

    companion object {
        lateinit var database: PhotoDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            PhotoDatabase::class.java, "PhotoDB"
        ).build()
    }
}