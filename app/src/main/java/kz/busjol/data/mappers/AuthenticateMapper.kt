package kz.busjol.data.mappers

import kz.busjol.data.remote.AuthenticateDto
import kz.busjol.domain.models.Authenticate

fun AuthenticateDto.toAuthenticateBody() = Authenticate(
    id = this.id,
    name = this.name,
    email = this.email,
    phone = this.phone,
    jwtToken = this.jwtToken
)