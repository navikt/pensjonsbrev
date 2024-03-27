package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.format
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Expression<IntBroek?>.formatBroek(): Expression<String> = this.format(IntBroekFormatter)

object IntBroekFormatter : LocalizedFormatter<IntBroek?>() {
    override fun apply(broek: IntBroek?, second: Language): String {
        if (broek == null) {
            return ""
        }

        return "${broek.teller}/${broek.nevner}"
    }
}

fun Expression<LocalDate>.formatMaanedAar(): Expression<String> = this.format(MaanedAarFormatter)

object MaanedAarFormatter : LocalizedFormatter<LocalDate>() {
    override fun apply(date: LocalDate, second: Language): String {
       return date.format(DateTimeFormatter.ofPattern("MMMM yyyy", second.locale()))
    }
}