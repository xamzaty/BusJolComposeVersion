package kz.busjol.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    private val russianLanguage = "ru"
    private val isAuthorized = false
    private val notificationsIsAvailable = true

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPrefs")
        private val appLanguage = stringPreferencesKey("language")
        private val userIsAuthorized = booleanPreferencesKey("userAuthorized")
        private val driverIsAuthorized = booleanPreferencesKey("driverAuthorized")
        private val isNotificationsIsAvailable = booleanPreferencesKey("isNotificationsIsAvailable")
    }

    val getAppLanguage: Flow<String>
        get() = context.dataStore.data.map {
            it[appLanguage] ?: russianLanguage
        }

    suspend fun setAppLanguage(value: String) {
        context.dataStore.edit { it[appLanguage] = value }
    }

    val getUserIsAuthorized: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[userIsAuthorized] ?: isAuthorized
        }

    suspend fun setUserIsAuthorized(value: Boolean) {
        context.dataStore.edit { it[userIsAuthorized] = value }
    }

    val getDriverIsAuthorized: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[driverIsAuthorized] ?: isAuthorized
        }

    suspend fun setDriverIsAuthorized(value: Boolean) {
        context.dataStore.edit { it[driverIsAuthorized] = value }
    }

    val getIsNotificationsIsAvailable: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[isNotificationsIsAvailable] ?: notificationsIsAvailable
        }

    suspend fun setNotificationAvailable(value: Boolean) {
        context.dataStore.edit { it[isNotificationsIsAvailable] = value }
    }

//    fun setAppLanguage(lang: Language) {
//        editor.putString(APP_LANGUAGE, lang.name).apply()
//        LocaleKeeper.currentLocale = Locale(lang.name.lowercase())
//    }
//
//    fun getAppLanguage(): Language {
//        return Language.valueOf(sp.getString(APP_LANGUAGE, Language.RU.name)!!)
//    }
}