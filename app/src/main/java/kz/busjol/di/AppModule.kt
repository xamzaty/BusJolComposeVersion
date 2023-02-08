package kz.busjol.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.busjol.AppSettings
import kz.busjol.AppSettingsSerializer
import kz.busjol.BuildConfig
import kz.busjol.data.remote.api.*
import kz.busjol.utils.Consts.BASE_URL
import kz.busjol.data.repository.DataStoreManager
import kz.busjol.utils.Consts.BASE_AUTH_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder()
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(provideHttpLoggingInterceptor())

        return httpBuilder
            .protocols(mutableListOf(Protocol.HTTP_1_1))
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return interceptor
    }

    @Provides
    @Singleton
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun getAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_AUTH_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun provideCityListApi(): CityListApi {
        return getRetrofit().create(CityListApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJourneyListApi(): SearchJourneyApi {
        return getRetrofit().create(SearchJourneyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSeatsListApi(): SeatsListApi {
        return getRetrofit().create(SeatsListApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingListApi(): BookingApi {
        return getRetrofit().create(BookingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketsApi(): TicketsApi {
        return getRetrofit().create(TicketsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return getAuthRetrofit().create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJourneysApi(): JourneysApi {
        return getRetrofit().create(JourneysApi::class.java)
    }

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext: Context): DataStore<AppSettings> =
        DataStoreFactory.create(
            serializer = AppSettingsSerializer,
            produceFile = { appContext.dataStoreFile("app-settings.json") },
        )
    }
