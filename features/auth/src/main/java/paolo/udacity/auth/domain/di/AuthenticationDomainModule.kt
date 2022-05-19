package paolo.udacity.auth.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import paolo.udacity.auth.data.datasource.local.UserLocalDatasource
import paolo.udacity.auth.data.mapper.local.UserLocalMapper
import paolo.udacity.auth.data.repository.AuthenticationDataRepository
import paolo.udacity.auth.domain.repository.AuthenticationRepository
import paolo.udacity.foundation.database.providers.DaoProvider


@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthenticationDomainModule {

    // Mappers
    // Local
    @Provides
    fun providesUserLocalMapper() = UserLocalMapper()

    // Datasource
    // Local
    @ActivityRetainedScoped
    @Provides
    fun providesUserLocalDatasource(daoProvider: DaoProvider) = UserLocalDatasource(daoProvider)

    @ActivityRetainedScoped
    @Provides
    fun providesAuthenticationRepository(
        userLocalDatasource: UserLocalDatasource,
        userLocalMapper: UserLocalMapper
    ): AuthenticationRepository = AuthenticationDataRepository(userLocalDatasource, userLocalMapper)

}