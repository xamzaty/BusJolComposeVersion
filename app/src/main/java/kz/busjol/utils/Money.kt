package kz.busjol.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Money(
    var amount: Double,
    var currency: String? = "KZT"
) : Parcelable {
    fun getCurrencySign(): String = Companion.getCurrencySign(currency)

    fun isKzt(): Boolean = currency == "KZT"
    fun isRub(): Boolean = currency == "RUB"

    fun formatWithCurrency(): String {
        return NumberFormatter.formatWithCurrency(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (amount != other.amount) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + (currency?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun getCurrencySign(currencyCode: String?): String {
            return when (currencyCode) {
                "AUD" -> "$"
                "AZN" -> "₼"
                "BYN" -> "р."
                "DKK" -> "kr."
                "KZT", null -> "₸"
                "SAR" -> ".ر.س"
                "SGD" -> "$"
                "XAU" -> "XAU"
                "USD" -> "$"
                "EUR" -> "€"
                "RUB" -> "₽"
                "KGS" -> "С̲"
                "GBP" -> "£"
                "CNY" -> "¥"
                "CHF" -> "₣"
                "HKD" -> "$"
                "GEL" -> "ლ." //Грузинский лари
                "AED" -> "د.إ" //Дирхам ОАЭ
                "INR" -> "₹" //Индийская рупия
                "CAD" -> "$" //Канадский доллар
                "MYR" -> "RM" //Малайзийский ринггит
                "PLN" -> "zł" //Польский злотый
                "THB" -> "฿" //Тайский бат
                "TRY" -> "₺" //Турецкая лира
                "UZS" -> "so’m" //узбекских сумов
                "UAH" -> "₴" //Украинская гривна
                "CZK" -> "Kč" //Чешская крона
                "SEK" -> "kr" //Шведская крона
                "KRW" -> "₩" //южнокорейских вон
                "JPY" -> "¥" //Японская иен
                else -> ""
            }
        }
    }

}

fun Money?.orNew(): Money = this ?: Money(0.0)


