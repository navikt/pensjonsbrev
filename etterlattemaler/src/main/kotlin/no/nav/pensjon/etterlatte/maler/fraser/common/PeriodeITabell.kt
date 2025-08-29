package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        ifNotNull(datoTOM) { datoTOM ->
            text(
                bokmal { +datoFOM.format(true) + " - " + datoTOM.format(true) },
                nynorsk { +datoFOM.format(true) + " - " + datoTOM.format(true) },
                english { +datoFOM.format(true) + " - " + datoTOM.format(true) },
            )
        } orShow {
            text(
                bokmal { +datoFOM.format(true) + " - " },
                nynorsk { +datoFOM.format(true) + " - " },
                english { +datoFOM.format(true) + " - " },
            )
        }
}