package kz.busjol.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kz.busjol.R
import kz.busjol.presentation.destinations.*

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    SearchJourney(SearchJourneyScreenDestination, R.drawable.search_category, R.string.search_category),
    MyTickets(MyTicketsScreenDestination, R.drawable.my_tickets_category, R.string.my_tickets_category),
    Contacts(ContactsScreenDestination, R.drawable.contacts_category, R.string.contacts_category),
    Profile(ProfileScreenDestination, R.drawable.profile_category, R.string.profile_category),
    DriverMain(DriverMainScreenDestination, R.drawable.profile_category, R.string.driver_main_category),
    Scan(ScanScreenDestination, R.drawable.profile_category, R.string.scan_category)
}