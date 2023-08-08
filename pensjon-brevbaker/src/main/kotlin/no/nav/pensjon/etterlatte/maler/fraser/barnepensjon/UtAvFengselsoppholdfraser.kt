package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object UtAvFengselsoppholdfraser {
    data class Begrunnelse(
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Barnepensjonen din utbetales igjen fra ${virkningsdato.format()} fordi [fritekst: du er løslatt/din formue ikke lenger er satt under forvaltning]. Barnepensjonen utbetales fra og med måneden [fritekst: du ble løslatt/din formue ikke lenger er under forvaltning]. Du får ${kronebeloep.format()} kroner hver måned.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
