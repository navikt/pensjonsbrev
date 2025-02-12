package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.dsl.expression.format
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Expression<IntBroek?>.formatBroek(): Expression<String> = this.format(IntBroekFormatter)

object IntBroekFormatter : LocalizedFormatter<IntBroek?>(), StableHash by StableHash.of("IntBroekFormatter") {
    override fun apply(
        broek: IntBroek?,
        second: Language,
    ): String {
        if (broek == null) {
            return ""
        }

        return "${broek.teller}/${broek.nevner}"
    }
}

fun Expression<LocalDate>.formatMaanedAar(): Expression<String> = this.format(MaanedAarFormatter)

object MaanedAarFormatter : LocalizedFormatter<LocalDate>(), StableHash by StableHash.of("MaanedAarFormatter") {
    override fun apply(
        date: LocalDate,
        second: Language,
    ): String {
        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy", second.locale()))
    }
}

fun Expression<LocalDate>.formatAar(): Expression<String> = this.format(AarFormatter)

object AarFormatter : LocalizedFormatter<LocalDate>(), StableHash by StableHash.of("AarFormatter") {
    override fun apply(
        date: LocalDate,
        second: Language,
    ): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy", second.locale()))
    }
}

fun Expression<Number>.formatTall(): Expression<String> = this.format(TallFormatter)

object TallFormatter : LocalizedFormatter<Number>(), StableHash by StableHash.of("TallFormatter") {
    override fun apply(
        first: Number,
        second: Language,
    ): String {
        return NumberFormat.getNumberInstance(second.locale()).format(first)
    }
}
