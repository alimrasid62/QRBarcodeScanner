package com.alimrasid.qrbarcodescanner

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(history: ScanHistory)

    @Query("SELECT * FROM scan_history ORDER BY date DESC")
    fun getAll(): LiveData<List<ScanHistory>>
}
