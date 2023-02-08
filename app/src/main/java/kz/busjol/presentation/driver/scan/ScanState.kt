package kz.busjol.presentation.driver.scan

data class ScanState(
    val isTicketValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
