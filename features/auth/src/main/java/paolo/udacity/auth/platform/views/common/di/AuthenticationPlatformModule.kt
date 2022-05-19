package paolo.udacity.auth.platform.views.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import paolo.udacity.auth.domain.repository.AuthenticationRepository
import paolo.udacity.auth.domain.use_case.SetCurrentUserUseCase
import paolo.udacity.auth.platform.views.common.mapper.UserMapper


@Module
@InstallIn(ViewModelComponent::class)
object AuthenticationPlatformModule {

    @ViewModelScoped
    @Provides
    fun providesRegisterUseCase(authenticationRepository: AuthenticationRepository) =
        SetCurrentUserUseCase(authenticationRepository)

    @Provides
    fun providesUserMapper() = UserMapper()

}