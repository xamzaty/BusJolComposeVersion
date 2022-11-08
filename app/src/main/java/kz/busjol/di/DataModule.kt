package kz.busjol.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.busjol.data.repository.DataStoreManager
import kz.busjol.domain.repository.DataStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Reusable
    fun providesDataRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository {
        return DataStoreManager(context)
    }
}