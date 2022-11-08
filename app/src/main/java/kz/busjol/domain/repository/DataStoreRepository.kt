package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.AppSettings
import kz.busjol.Language
import kz.busjol.UserState

interface DataStoreRepository {
    suspend fun setLanguage(language: Language)
    suspend fun setUserState(userState: UserState)
    suspend fun setNotificationsState(isNotificationsAvailable: Boolean)
    fun getAppSettings(): Flow<AppSettings>
}