package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

const val NON_BREAKING_SPACE = '\u00A0'

abstract class LocalizedFormatter<in T>(doc: Documentation? = null) : BinaryOperation<T, Language, String>(doc) {
    object ShortDateFormat : LocalizedFormatter<LocalDate>() {
        override fun stableHashCode(): Int = "ShortDateFormat".hashCode()
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.SHORT)).replace(' ', NON_BREAKING_SPACE) //space to non braking space
    }

    object DateFormat : LocalizedFormatter<LocalDate>() {
        override fun stableHashCode(): Int = "DateFormat".hashCode()
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.LONG)).replace(' ', NON_BREAKING_SPACE) //space to non braking space
    }

    object MonthYearFormatter : LocalizedFormatter<LocalDate>() {
        override fun apply(date: LocalDate, second: Language): String {
            return date.format(DateTimeFormatter.ofPattern("MMMM yyyy", second.locale()))
        }

        override fun stableHashCode(): Int =  StableHash.of("MaanedAarFormatter").hashCode()
    }

    class DoubleFormat(private val scale: Int) : LocalizedFormatter<Double>() {
        override fun stableHashCode(): Int = "DoubleFormat($scale)".hashCode()
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.${scale}f", first)
    }

    object IntFormat : LocalizedFormatter<Int>() {
        override fun stableHashCode(): Int = "IntFormat".hashCode()
        override fun apply(first: Int, second: Language): String =
            String.format(second.locale(), "%d", first)
    }

    object CurrencyFormat : LocalizedFormatter<Int>() {
        override fun stableHashCode(): Int = "CurrencyFormat".hashCode()
        override fun apply(first: Int, second: Language): String =
            NumberFormat.getNumberInstance(second.locale())
                .apply { maximumFractionDigits = 0 }
                .format(first)
                .replace(' ', NON_BREAKING_SPACE)
    }

    object TelefonnummerFormat : LocalizedFormatter<Telefonnummer>() {
        override fun stableHashCode(): Int = "TelefonnummerFormat".hashCode()
        override fun apply(first: Telefonnummer, second: Language): String = first.format()
    }

    object CollectionFormat : LocalizedFormatter<Collection<String>>() {
        override fun stableHashCode(): Int = "CollectionFormat".hashCode()
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

private fun ch(): Char = NON_BREAKING_SPACE
