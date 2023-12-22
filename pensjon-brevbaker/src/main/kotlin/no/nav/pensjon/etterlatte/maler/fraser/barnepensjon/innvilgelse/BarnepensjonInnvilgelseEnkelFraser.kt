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
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import java.time.LocalDate

object BarnepensjonInnvilgelseEnkelFraser {

    data class Foerstegangsbehandlingsvedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val beloep: Expression<Kroner>,
        val vedtaksdato: Expression<LocalDate>,
        val erEtterbetaling: Expression<Boolean>,
        val beregningsperioder: Expression<List<Beregningsperiode>>,
        val nyesteUtbetalingsperiodeDatoFom: Expression<LocalDate>,
        val harFlereUtbetalingsperioder: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                val formatertNyesteUtbetalingsperiodeDatoFom = nyesteUtbetalingsperiodeDatoFom.format()

                textExpr(
                    Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )

                showIf(harFlereUtbetalingsperioder) {
                    textExpr(
                        Bokmal to "".expr() + "Du får "+ beloep.format() + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se utbetalingsbeløp for tidligere perioder i vedlegg om etterbetaling.",
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du får ".expr() + beloep.format() + " kroner hver måned før skatt. ",
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjonen utbetales til og med den kalendermåneden du fyller 20 år.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon gis på bakgrunn av at du er medlem i folketrygden og at avdøde i de siste fem årene før dødsfallet var medlem i folketrygden eller fikk pensjon fra folketrygden.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5" + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }
}
