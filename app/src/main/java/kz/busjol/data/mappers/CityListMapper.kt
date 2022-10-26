package kz.busjol.data.mappers

import kz.busjol.data.remote.CityDto
import kz.busjol.domain.models.City

fun List<CityDto>.toCityList(): List<City> {
    return this.map { from ->
        City(
            id = from.id,
            name = from.name
        )
    }
}