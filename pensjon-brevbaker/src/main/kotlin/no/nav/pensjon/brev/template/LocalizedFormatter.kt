package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.Year
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.FormatStyle

abstract class LocalizedFormatter<in T>(doc: Documentation? = null) : BinaryOperation<T, Language, String>(doc) {
    object ShortDateFormat : LocalizedFormatter<LocalDate>() {
        override fun stableHashCode(): Int = "ShortDateFormat".hashCode()
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.SHORT)).replace(' ', ' ') //space to non braking space
    }

    object DateFormat : LocalizedFormatter<LocalDate>() {
        override fun stableHashCode(): Int = "DateFormat".hashCode()
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.LONG)).replace(' ', ' ') //space to non braking space
    }

    object DoubleFormat : LocalizedFormatter<Double>() {
        override fun stableHashCode(): Int = "DoubleFormat".hashCode()
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.2f", first)
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
    }

    object TelefonnummerFormat : LocalizedFormatter<Telefonnummer>() {
        override fun stableHashCode(): Int = "TelefonnummerFormat".hashCode()
        override fun apply(first: Telefonnummer, second: Language): String = first.format()
    }

    object AarFormat : LocalizedFormatter<Collection<Year>>() {
        override fun stableHashCode(): Int = "AarFormat".hashCode()
        override fun apply(first: Collection<Year>, second: Language): String =
            formaterListe(first.map { it.value.toString() }, second)
    }

    object CollectionFormat : LocalizedFormatter<Collection<String>>() {
        override fun stableHashCode(): Int = "CollectionFormat".hashCode()
        override fun apply(first: Collection<String>, second: Language): String {
            return formaterListe(first, second)
        }
    }
}

private fun formaterListe(first: Collection<String>, second: Language): String =
    when {
        first.isEmpty() -> ""
        first.size == 1 -> first.first()
        else -> {
            val lastSeparator = when (second) {
                Language.Bokmal -> " og "
                Language.Nynorsk -> " og "
                Language.English -> " and "
            }
            first.take(first.size - 1).joinToString(", ") + lastSeparator + first.last()
        }
    }
