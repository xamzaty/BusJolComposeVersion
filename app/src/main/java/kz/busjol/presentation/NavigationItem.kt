package kz.busjol.presentation

import androidx.annotation.StringRes
import kz.busjol.R

sealed class NavigationItem(var route: String, var icon: Int, @StringRes val title: Int) {
    object SearchJourney : NavigationItem(
        "searchJourney", R.drawable.search_category, R.string.search_category
    )
    object MyTickets : NavigationItem(
        "myTickets", R.drawable.my_tickets_category, R.string.my_tickets_category
    )
    object Contacts : NavigationItem(
        "contacts", R.drawable.contacts_category, R.string.contacts_category
    )
    object Profile : NavigationItem(
        "profile", R.drawable.profile_category, R.string.profile_category
    )
    object DriverMain : NavigationItem(
        "driverMain", R.drawable.contacts_category, R.string.driver_main_category
    )
    object Scanner : NavigationItem(
        "driverMain", R.drawable.contacts_category, R.string.scan_category
    )
}