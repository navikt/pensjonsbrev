package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.apply

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
        override fun apply(first: LocalDate, second: Language): String {
            return first.format(DateTimeFormatter.ofPattern("MMMM yyyy", second.locale()))
        }

        override fun stableHashCode(): Int =  StableHash.of("MaanedAarFormatter").hashCode()
    }

    object YearMonthFormatter : LocalizedFormatter<YearMonth>() {
        override fun apply(first: YearMonth, second: Language): String {
            return MonthYearFormatter.apply(first.atDay(1), second)
        }

        override fun stableHashCode(): Int =  StableHash.of("MaanedAarFormatter").hashCode()
    }

    class DoubleFormat(private val scale: Int) : LocalizedFormatter<Double>() {
        override fun stableHashCode(): Int = "DoubleFormat($scale)".hashCode()
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.${scale.coerceIn(0..16)}f", first)
    }

    object IntFormat : LocalizedFormatter<Int>() {
        override fun stableHashCode(): Int = "IntFormat".hashCode()
        override fun apply(first: Int, second: Language): String =
            String.format(second.locale(), "%d", first)
    }

    object CurrencyFormat : LocalizedFormatter<Int>() {
        override fun stableHashCode(): Int = "CurrencyFormatInt".hashCode()
        override fun apply(first: Int, second: Language): String = CurrencyFormatKroner(denominator = false).apply(Kroner(first), second)
    }

    class CurrencyFormatKroner(val denominator: Boolean = false) : LocalizedFormatter<Kroner>() {
        override fun stableHashCode(): Int = "CurrencyFormat".hashCode()
        override fun apply(first: Kroner, second: Language): String =
            NumberFormat.getNumberInstance(second.locale())
                .apply { maximumFractionDigits = 0 }
                .format(first)
                .replace(' ', NON_BREAKING_SPACE).let {
                    when {
                        !denominator -> it
                        second in setOf(Language.Bokmal, Language.Nynorsk) -> "$it kroner"
                        second == Language.English -> "NOK $it"
                        else -> throw IllegalArgumentException("Uforventa feil, med kroner $first, språk $second og denominator true")
                    }
                }

    }

    object TelefonnummerFormat : LocalizedFormatter<Telefonnummer>() {
        override fun stableHashCode(): Int = "TelefonnummerFormat".hashCode()
        override fun apply(first: Telefonnummer, second: Language): String = first.format()
    }

    object FoedselsnummerFormat : LocalizedFormatter<Foedselsnummer>() {
        override fun stableHashCode(): Int = "FoedselsnummerFormat".hashCode()
        override fun apply(first: Foedselsnummer, second: Language): String = first.format()
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
