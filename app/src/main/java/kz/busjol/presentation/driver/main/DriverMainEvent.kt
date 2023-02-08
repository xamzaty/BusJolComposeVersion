package kz.busjol.presentation.driver.main

sealed interface DriverMainEvent {
    object IsRefreshing : DriverMainEvent
}