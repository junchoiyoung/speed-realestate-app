package org.speed.realestate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRealEstate(realEstate: RealEstate)

    @Delete
    suspend fun deleteRealEstate(realEstate: RealEstate)

    @Update
    suspend fun updateRealEstate(realEstate: RealEstate)

    @Query("SELECT * FROM real_estate_table")
    fun getAllRealEstates(): Flow<List<RealEstate>>

    @Query(" SELECT DISTINCT name FROM real_estate_table")
    fun getNames(): Flow<List<String>>

    @Query("SELECT DISTINCT tradeType FROM real_estate_table")
    fun getTradeTypes(): Flow<List<String>>

    @Query("SELECT DISTINCT type FROM real_estate_table")
    fun getTypes(): Flow<List<String>>

    @Query("SELECT DISTINCT dong FROM real_estate_table")
    fun getDongs(): Flow<List<String>>

    @Query("SELECT DISTINCT option FROM real_estate_table")
    fun getOptions(): Flow<List<String>>
}