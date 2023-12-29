package com.jlpc.facetimelapsemaker

import android.app.Application
import androidx.room.Room
import com.jlpc.facetimelapsemaker.model.PhotoDatabase
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.model.PreferenceManager

class FaceTimelapseMakerApp : Application() {

    companion object {
        lateinit var database: PhotoDatabase
            private set
        lateinit var repository: PhotoRepository
            private set
        lateinit var preferences: PreferenceManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.inMemoryDatabaseBuilder(
            applicationContext,
            PhotoDatabase::class.java,
        ).build()
        repository = PhotoRepository(database)
        preferences = PreferenceManager(context = applicationContext)
    }
}
