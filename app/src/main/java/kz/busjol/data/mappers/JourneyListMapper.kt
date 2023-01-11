package kz.busjol.data.mappers

import kz.busjol.data.remote.JourneyDto
import kz.busjol.domain.models.Journey
import kz.busjol.domain.models.JourneyItem

fun List<JourneyDto>.toJourneyList(): List<Journey> {
    return this.map { journeyResponse ->
        Journey(
            journey = journeyItem(journeyResponse),
            departureTime = journeyResponse.departureTime,
            arrivalTime = journeyResponse.arrivalTime,
            amount = journeyResponse.amount,
            numberOfPlaces = journeyResponse.numberOfPlaces,
            numberOfFreePlaces = journeyResponse.numberOfFreePlaces,
            stopName = journeyResponse.stopName,
            cityFrom = journeyResponse.cityFrom,
            cityTo = journeyResponse.cityTo,
            segmentId = journeyResponse.segmentId
        )
    }
}

private fun journeyItem(journeyResponse: JourneyDto) = JourneyItem(
    id = journeyResponse.journey?.id,
    created = journeyResponse.journey?.created,
    status = journeyResponse.journey?.status,
    name = journeyResponse.journey?.name,
    departsOn = journeyResponse.journey?.departsOn,
    routeId = journeyResponse.journey?.routeId,
    carrierId = journeyResponse.journey?.carrierId,
    transportId = journeyResponse.journey?.transportId,
    code = journeyResponse.journey?.code,
    driverId = journeyResponse.journey?.driverId
)