package paolo.udacity.foundation.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.providers.DaoProvider
import paolo.udacity.foundation.database.providers.DatabaseProvider
import paolo.udacity.foundation.providers.DateTimeProvider
import paolo.udacity.foundation.providers.OffsetDateTimeProvider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDateTimeProvider(): DateTimeProvider<OffsetDateTime> =
        OffsetDateTimeProvider()

    @Singleton
    @Provides
    fun providesDatabaseProvider(@ApplicationContext context: Context) =
        DatabaseProvider(context)

    @Singleton
    @Provides
    fun providesDaoProvider(databaseProvider: DatabaseProvider) =
        DaoProvider(databaseProvider)

}