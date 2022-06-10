package paolo.udacity.foundation.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.UserEntity
import paolo.udacity.foundation.database.models.enums.UserStatusEnum
import paolo.udacity.foundation.providers.DateTimeProvider


@Dao
abstract class UserDao: BaseDao<UserEntity> {

    @Transaction
    open suspend fun insertOrUpdate(entity: UserEntity,
                                    dateTimeProvider: DateTimeProvider<OffsetDateTime>) {
        val now = dateTimeProvider.now()
        val item = findById(entity.id)
        if (item == null) {
            insert(entity.apply {
                createdAt = now
                updatedAt = now
            })
        } else {
            update(entity.apply {
                createdAt = item.createdAt
                updatedAt = now
            })
        }
    }

    @Query("SELECT * FROM user WHERE id = :id")
    abstract suspend fun findById(id: Long): UserEntity?

    @Query("SELECT * FROM user WHERE email = :email")
    abstract suspend fun findByEmail(email: String): UserEntity?

    @Query("UPDATE user SET status = :status")
    abstract suspend fun logout(status: UserStatusEnum = UserStatusEnum.CREATED)

}