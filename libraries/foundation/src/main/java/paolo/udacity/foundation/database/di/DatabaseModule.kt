package paolo.udacity.foundation.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import paolo.udacity.foundation.database.providers.DaoProvider
import paolo.udacity.foundation.database.providers.DatabaseProvider


@Module
@InstallIn(ActivityRetainedComponent::class)
object DatabaseModule {

    @ActivityRetainedScoped
    @Provides
    fun providesDatabaseProvider(@ApplicationContext context: Context) =
        DatabaseProvider(context)

    @ActivityRetainedScoped
    @Provides
    fun providesDaoProvider(databaseProvider: DatabaseProvider) =
        DaoProvider(databaseProvider)

}