package kz.busjol.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object NumberFormatter {

    const val DECIMAL_SEPARATOR: Char = ','
    const val GROUPING_SEPARATOR: Char = ' '
    //    val DECIMAL_SEPARATORS: Regex = "[.,]".toRegex()
    const val DOT: Char = '.'
    val regexCurrency = Regex("[^\\d.]")

    val formatter = DecimalFormat("###,###,###,###.##", DecimalFormatSymbols().apply {
        groupingSeparator = GROUPING_SEPARATOR
        decimalSeparator = DECIMAL_SEPARATOR
    })

    @JvmStatic
    fun format(value: Number): String {
        return formatter.format(value)
    }

    @JvmStatic
    fun format(value: BigDecimal): String {
        return formatter.format(value.toDouble())
    }

    @JvmStatic
    fun formatWithCurrency(value: Number): String {
        return formatWithCurrency(value, "₸")
    }

    fun formatWithCurrency(value: Number, currencySign: String?): String {
        return format(value) + " ${currencySign ?: "₸"}"
    }

    @JvmStatic
    fun formatWithCurrency(balance: Money?): String {
        return balance.orNew().let {
            format(it.amount) + " ${it.getCurrencySign()}"
        }
    }

}