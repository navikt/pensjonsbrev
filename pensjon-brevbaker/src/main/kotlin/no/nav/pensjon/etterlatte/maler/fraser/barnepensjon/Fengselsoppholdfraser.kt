package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import java.time.LocalDate

object Fengselsoppholdfraser {
    data class Opphold(
        val virkningsdato: Expression<LocalDate>,
        val fraDato: Expression<LocalDate>,
        val tilDato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                textExpr(
                    Bokmal to "Barnepensjonen din er stanset fra ".expr() + formatertVirkningsdato + " fordi du er [fritekst: under straffegjennomføring/din formue er satt under forvaltning].",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjonen utbetales ikke fra og med andre måned etter at soning tar til. Dette gjelder også dersom formuen er satt under forvaltning. Vi har fått melding om at du [fritekst: er under straffegjennomføring/din formue er satt under forvaltning] fra ${fraDato.format()}.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom straffegjennomføring:",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "En foreløpig dato for løslatelse er angitt til ${tilDato.format()}. Barnepensjonen blir utbetalt igjen når NAV mottar skriftlig dokumentasjon på formell løslatelse.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom formue er satt under forvaltning:",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det er antatt at forvaltningen av formuen din opphever ${tilDato.format()}. Barnepensjonen blir utbetalt igjen når NAV mottar det formelle vedtaket om at forvaltningen er opphørt.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § 18-9 og § 22-12.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
