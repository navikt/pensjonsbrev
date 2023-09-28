package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object UtAvFengselsoppholdfraser {
    data class Begrunnelse(
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din utbetales igjen fra ".expr() + virkningsdato.format() + " fordi [fritekst: du er løslatt/din formue ikke lenger er satt under forvaltning]. Barnepensjonen utbetales fra og med måneden [fritekst: du ble løslatt/din formue ikke lenger er under forvaltning]. Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }
}
