package kz.busjol.presentation.driver.main

import kz.busjol.domain.models.Journey

data class DriverMainState(
    val journeyList: List<Journey>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    var isRefreshing: Boolean = false
)
