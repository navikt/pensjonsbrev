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
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import java.time.LocalDate

object BarnepensjonInnvilgelseUfallFraser {

    data class Foerstegangsbehandlingsvedtak(
        val avdoed: Expression<Avdoed>,
        val virkningsdato: Expression<LocalDate>,
        val sisteBeregningsperiodeDatoFom: Expression<LocalDate>,
        val sisteBeregningsperiodeBeloep: Expression<Kroner>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                val formatertNyesteUtbetalingsperiodeDatoFom = sisteBeregningsperiodeDatoFom.format()
                val formatertBeloep = sisteBeregningsperiodeBeloep.format()
                val avdoedNavn = avdoed.navn

                textExpr(
                    Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ".",
                    Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ".",
                    English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + "."
                )

                showIf(harFlereUtbetalingsperioder) {
                    textExpr(
                        Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se utbetalingsbeløp for tidligere perioder i vedlegg om etterbetaling.",
                        Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom + ". Sjå utbetalingsbeløp for tidlegare periodar i vedlegget om etterbetaling.",
                        English to "You will receive ".expr() + formatertBeloep + " kroner each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom + ". See the payment amount for previous periods in the Back Payment Attachment."
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.",
                        Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt.",
                        English to "You will receive NOK ".expr() + formatertBeloep + " each month before tax."
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjonen utbetales til og med den kalendermåneden du fyller 20 år.",
                    Nynorsk to "Barnepensjonen blir utbetalt fram til og med kalendermånaden du fyller 20 år.",
                    English to "The children’s pension is paid up to and including the calendar month in which you turn 20.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon gis på bakgrunn av at du er medlem i folketrygden og at avdøde i de siste fem årene før dødsfallet var medlem i folketrygden eller fikk pensjon fra folketrygden.",
                    Nynorsk to "Barnepensjon blir gitt på bakgrunn av at du er medlem i folketrygda, og at avdøde var medlem i eller fekk pensjon frå folketrygda dei siste fem åra før sin død.",
                    English to "You are eligible for a children's pension because you are a member of the Norwegian National Insurance Scheme, and the deceased has been a member of the National Insurance Scheme in the five years prior to the death or he/she has been receiving a pension from the Scheme.",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                )
            }
        }
    }
}
