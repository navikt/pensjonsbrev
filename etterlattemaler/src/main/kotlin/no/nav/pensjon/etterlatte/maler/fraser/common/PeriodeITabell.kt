package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        ifNotNull(datoTOM) { datoTOM ->
            textExpr(
                Language.Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true),
                Language.Nynorsk to datoFOM.format(true) + " - " + datoTOM.format(true),
                Language.English to datoFOM.format(true) + " - " + datoTOM.format(true),
            )
        } orShow {
            textExpr(
                Language.Bokmal to datoFOM.format(true) + " - ",
                Language.Nynorsk to datoFOM.format(true) + " - ",
                Language.English to datoFOM.format(true) + " - ",
            )
        }
}
