package kz.busjol.utils

import kz.busjol.utils.Regex.isValidEmail
import java.util.regex.Pattern

object Regex {

    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private val PASSWORD_STRENGTH_PATTERN: Pattern = Pattern.compile(
        "^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}\$"
    )

    private val CONTAINS_DIGITS: Pattern = Pattern.compile(
        ".*[a-z].*"
    )

    fun String.isValidEmail() = EMAIL_ADDRESS_PATTERN.matcher(this).matches()

    fun String.isPasswordStrength() = PASSWORD_STRENGTH_PATTERN.matcher(this).matches()

    fun String.containsLetters() = CONTAINS_DIGITS.matcher(this).matches()
}