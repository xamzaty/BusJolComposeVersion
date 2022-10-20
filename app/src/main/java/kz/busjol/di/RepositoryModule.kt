package kz.busjol.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.busjol.data.repository.CityPickerRepositoryImpl
import kz.busjol.domain.repository.CityListRepository
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun cityListRepository(
        cityPickerRepositoryImpl: CityPickerRepositoryImpl
    ): CityListRepository
}