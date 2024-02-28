package no.nav.pensjon.brev.template

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.FormatStyle

abstract class LocalizedFormatter<in T>(doc: Documentation? = null) : BinaryOperation<T, Language, String>(doc) {
    object ShortDateFormat : LocalizedFormatter<LocalDate>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.SHORT)).replace(' ', ' ') //space to non braking space
    }

    object DateFormat : LocalizedFormatter<LocalDate>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.LONG)).replace(' ', ' ') //space to non braking space
    }

    object DoubleFormat : LocalizedFormatter<Double>() {
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.2f", first)
    }

    object IntFormat : LocalizedFormatter<Int>() {
        override fun apply(first: Int, second: Language): String =
            String.format(second.locale(), "%d", first)
    }

    object CurrencyFormat : LocalizedFormatter<Int>() {
        override fun apply(first: Int, second: Language): String =
            NumberFormat.getNumberInstance(second.locale())
                .apply { maximumFractionDigits = 0 }
                .format(first)
    }

    object CollectionFormat : LocalizedFormatter<Collection<String>>() {
        override fun apply(first: Collection<String>, second: Language): String {
            return if (first.size == 1) {
                first.first()
            } else {
                val lastSeparator = when (second) {
                    Language.Bokmal -> " og "
                    Language.Nynorsk -> " og "
                    Language.English -> " and "
                }
                first.take(first.size - 1).joinToString(", ") + lastSeparator + first.last()
            }
        }

    }
}
