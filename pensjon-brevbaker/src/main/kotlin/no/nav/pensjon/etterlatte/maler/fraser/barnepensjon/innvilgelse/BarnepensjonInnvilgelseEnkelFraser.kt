package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.innvilgelse

import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object BarnepensjonInnvilgelseEnkelFraser {

    data class Foerstegangsbehandlingsvedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val beloep: Expression<Kroner>,
        val vedtaksdato: Expression<LocalDate>,
        val erEtterbetalingMerEnnTreMaaneder: Expression<Boolean>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                val formatertVedtaksdato = vedtaksdato.format()
                textExpr(
                    Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du får " + beloep.format() + " kroner hver måned før skatt fra " + formatertVedtaksdato + ". " +
                            "Barnepensjonen utbetales til og med den kalendermåneden du fyller 18 år.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon gis når du er medlem i folketrygden " +
                            "og når avdøde i de siste fem årene før dødsfallet var medlem i folketrygden " +
                            "eller fikk pensjon fra folketrygden.",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(erEtterbetalingMerEnnTreMaaneder) {
                includePhrase(Etterbetaling)
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5 og 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    object Etterbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, § 22-12 og § 22-13.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
