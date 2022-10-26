package kz.busjol.presentation.passenger.journey

import kz.busjol.domain.models.Journey

data class JourneyState(
    val journeyList: List<Journey>? = null
)
