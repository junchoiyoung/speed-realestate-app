package org.speed.realestate.data

import kotlinx.coroutines.flow.Flow

class OfflineRealEstateRepository(
    private val realEstateDao: RealEstateDao,
    private val tagDao: TagDao,
) : RealEstateRepository{

    override suspend fun deleteRealEstate(realEstate: RealEstate) {
        realEstateDao.deleteRealEstate(realEstate)
    }

    override suspend fun updateRealEstate(realEstate: RealEstate) {
        realEstateDao.updateRealEstate(realEstate)
    }

    override suspend fun addRealEstate(realEstate: RealEstate) {
        realEstateDao.addRealEstate(realEstate)
    }

    override fun getAllRealEstates(): Flow<List<RealEstate>> {
        return realEstateDao.getAllRealEstates()
    }

    override fun getNames(): Flow<List<String>> {
        return realEstateDao.getNames()
    }

    override fun getTradeTypes(): Flow<List<String>> {
        return realEstateDao.getTradeTypes()
    }

    override fun getTypes(): Flow<List<String>> {
        return realEstateDao.getTypes()
    }

    override fun getDongs(): Flow<List<String>> {
        return realEstateDao.getDongs()
    }

    override fun getOptions(): Flow<List<String>> {
        return realEstateDao.getOptions()
    }

    override suspend fun addTag(tag: Tag) {
        return tagDao.addTag(tag)
    }

    override fun getTags(): Flow<List<Tag>> {
        return tagDao.getTags()
    }
}
