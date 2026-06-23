package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.models.DeviceInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceProfileDao {
    @Query("SELECT * FROM device_profiles ORDER BY timestamp DESC LIMIT 1")
    fun getLatestDeviceProfile(): Flow<DeviceInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(deviceInfo: DeviceInfo)
}
