package kz.busjol.data.mappers

import kz.busjol.data.remote.CityDto
import kz.busjol.domain.models.City

fun List<CityDto>.toCityList(): List<City> {
    return this.map {
        City(
            id = it.id,
            name = it.name
        )
    }
}