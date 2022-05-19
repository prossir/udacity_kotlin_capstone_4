package paolo.udacity.location_reminder.data.datasource.local

import paolo.udacity.foundation.database.models.PointOfInterestEntity
import paolo.udacity.foundation.database.providers.DaoProvider


class PointOfInterestLocalDatasource(
    daoProvider: DaoProvider
) {

    private val pointOfInterestDao = daoProvider.getPointOfInterestDao()

    suspend fun createPointOfInterest(pointOfInterestEntity: PointOfInterestEntity) {
        pointOfInterestDao.insertOrUpdate(pointOfInterestEntity)
    }

}