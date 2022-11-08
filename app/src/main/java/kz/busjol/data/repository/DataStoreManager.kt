package kz.busjol.data.repository

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kz.busjol.*
import kz.busjol.domain.repository.DataStoreRepository
import javax.inject.Inject

private val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)

class DataStoreManager @Inject constructor(
    @ApplicationContext val appContext: Context
): DataStoreRepository {

    override suspend fun setLanguage(language: Language) {
        appContext.dataStore.updateData {
            it.copy(language = language)
        }
    }

    override suspend fun setUserState(userState: UserState) {
        appContext.dataStore.updateData {
            it.copy(userState = userState)
        }
    }

    override suspend fun setNotificationsState(isNotificationsAvailable: Boolean) {
        appContext.dataStore.updateData {
            it.copy(isNotificationsAvailable = isNotificationsAvailable)
        }
    }

    override fun getAppSettings() = appContext.dataStore.data
}