package kz.busjol.presentation.driver.scan

import kz.busjol.domain.models.TicketInfo

data class ScanState(
    val isTicketValid: Boolean = false,
    val ticketInfo: TicketInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
