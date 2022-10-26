package kz.busjol.ext

fun List<Any>.filterValueFromList(text: String, filteredList: List<Any>): List<Any> {
    return if (text.isEmpty()) this
    else filteredList
}