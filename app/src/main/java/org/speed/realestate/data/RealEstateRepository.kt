package org.speed.realestate.data

import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {

    fun getAllRealEstates(): Flow<List<RealEstate>>

    suspend fun addRealEstate(realEstate: RealEstate)

    suspend fun updateRealEstate(realEstate: RealEstate)

    suspend fun deleteRealEstate(realEstate: RealEstate)

    fun getNames(): Flow<List<String>>

    fun getTradeTypes(): Flow<List<String>>

    fun getTypes(): Flow<List<String>>

    fun getDongs(): Flow<List<String>>

    fun getOptions(): Flow<List<String>>

    suspend fun addTag(tag: Tag)

    fun getTags() : Flow<List<Tag>>
}
