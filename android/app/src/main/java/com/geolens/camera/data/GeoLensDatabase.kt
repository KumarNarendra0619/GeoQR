package com.geolens.camera.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CaptureRecord::class], version = 1, exportSchema = false)
abstract class GeoLensDatabase : RoomDatabase() {
    abstract fun captureDao(): CaptureDao

    companion object {
        @Volatile private var INSTANCE: GeoLensDatabase? = null

        fun get(context: Context): GeoLensDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                GeoLensDatabase::class.java,
                "geolens.db"
            ).build().also { INSTANCE = it }
        }
    }
}
