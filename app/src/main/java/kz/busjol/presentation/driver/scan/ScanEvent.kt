package kz.busjol.presentation.driver.scan

sealed interface ScanEvent {
    data class CheckTicket(val qrCode: String): ScanEvent
}