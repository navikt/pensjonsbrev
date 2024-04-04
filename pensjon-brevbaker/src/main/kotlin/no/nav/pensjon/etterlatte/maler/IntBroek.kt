package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.format

data class IntBroek(val teller: Int, val nevner: Int)

fun Expression<IntBroek?>.formatBroek(): Expression<String> = this.format(IntBroekFormatter)

object IntBroekFormatter : LocalizedFormatter<IntBroek?>() {
    override fun apply(broek: IntBroek?, second: Language): String {
        if (broek == null) {
            return ""
        }

        return "${broek.teller}/${broek.nevner}"
    }

    override fun stableHashCode(): Int = "IntBroekFormatter".hashCode()
}