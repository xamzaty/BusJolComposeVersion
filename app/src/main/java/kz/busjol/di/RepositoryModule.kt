package kz.busjol.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.busjol.data.repository.CityPickerRepositoryImpl
import kz.busjol.data.repository.JourneyRepositoryImpl
import kz.busjol.data.repository.SeatsRepositoryImpl
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.repository.SeatsListRepository
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

    @Binds
    @Singleton
    abstract fun journeyListRepository(
        journeyRepositoryImpl: JourneyRepositoryImpl
    ): JourneyListRepository

    @Binds
    @Singleton
    abstract fun seatsListRepository(
        seatsRepositoryImpl: SeatsRepositoryImpl
    ): SeatsListRepository
}