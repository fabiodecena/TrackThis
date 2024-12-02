package com.example.trackthis

import android.app.Application
import com.example.trackthis.data.database.HistoryDatabase


class TrackApplication : Application() {
    val database: HistoryDatabase by lazy { HistoryDatabase.getDatabase(this) }
}
