package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
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
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object BarnepensjonInnvilgelseFraser {

    data class Foerstegangsbehandlingsvedtak(
        val avdoed: Expression<Avdoed>,
        val virkningsdato: Expression<LocalDate>,
        val sisteBeregningsperiodeDatoFom: Expression<LocalDate>,
        val sisteBeregningsperiodeBeloep: Expression<Kroner>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val erGjenoppretting: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                val formatertNyesteUtbetalingsperiodeDatoFom = sisteBeregningsperiodeDatoFom.format()
                val formatertBeloep = sisteBeregningsperiodeBeloep.format()
                val avdoedNavn = avdoed.navn

                showIf(erGjenoppretting) {
                    textExpr(
                        Language.Bokmal to "Vi viser til forhåndsvarsel om ny barnepensjon. Du er innvilget barnepensjon på nytt fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                        Language.Nynorsk to "Vi viser til førehandsvarselet om ny barnepensjon. Du er innvilga ny barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                        Language.English to "We refer to the advance notice about a new children’s pension scheme. You have been granted a children's pension again from ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + ". "
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                        Language.Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                        Language.English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + ". "
                    )
                }

                showIf(harUtbetaling) {
                    showIf(harFlereUtbetalingsperioder) {
                        textExpr(
                            Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se utbetalingsbeløp for tidligere perioder i vedlegg om etterbetaling.",
                            Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom + ". Sjå utbetalingsbeløp for tidlegare periodar i vedlegget om etterbetaling.",
                            Language.English to "You will receive ".expr() + formatertBeloep + " kroner each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom + ". See the payment amount for previous periods in the Back Payment Attachment."
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.",
                            Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt.",
                            Language.English to "You will receive NOK ".expr() + formatertBeloep + " each month before tax."
                        )
                    }
                }.orShow {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du" +
                                " mottar i uføretrygd fra NAV.",
                        Language.Nynorsk to "Du får ikkje utbetalt barnepensjon, då denne har blitt redusert med" +
                                " utgangspunkt i uføretrygda du får frå NAV.",
                        Language.English to "You will not receive payments from the children’s pension because they" +
                                " have been reduced according to what you already receive in disability benefits from NAV.",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon gis på bakgrunn av at du er under 20 år, medlem i folketrygden og at avdøde i de siste fem årene før dødsfallet var medlem i folketrygden eller fikk pensjon fra folketrygden.",
                    Language.Nynorsk to "Barnepensjon blir gitt på bakgrunn av at du er under 20 år, medlem i folketrygda, og at avdøde var medlem i eller fekk pensjon frå folketrygda dei siste fem åra før sin død.",
                    Language.English to "You are eligible for a children's pension because you are under 20 years old, a member of the Norwegian National Insurance Scheme, and the deceased has been a member of the National Insurance Scheme in the five years prior to the death or he/she has been receiving a pension from the Scheme.",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
            }
        }
    }
}
