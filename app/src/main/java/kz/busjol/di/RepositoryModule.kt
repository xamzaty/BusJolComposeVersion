package kz.busjol.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.busjol.data.repository.*
import kz.busjol.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bookingListRepository(
        bookingListRepositoryImpl: BookingRepositoryImpl
    ): BookingListRepository
}