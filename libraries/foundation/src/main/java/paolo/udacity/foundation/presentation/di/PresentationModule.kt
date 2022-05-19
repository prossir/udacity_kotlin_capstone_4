package paolo.udacity.foundation.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import paolo.udacity.foundation.presentation.mapper.FailureMapper


@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    fun providesFailureMapper() = FailureMapper()

}