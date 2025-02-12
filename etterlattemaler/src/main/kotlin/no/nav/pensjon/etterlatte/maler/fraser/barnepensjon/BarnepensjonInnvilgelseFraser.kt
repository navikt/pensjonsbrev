package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

object BarnepensjonInnvilgelseFraser {
    data class Foerstegangsbehandlingsvedtak(
        val avdoed: Expression<Avdoed?>,
        val virkningsdato: Expression<LocalDate>,
        val sisteBeregningsperiodeDatoFom: Expression<LocalDate>,
        val sisteBeregningsperiodeBeloep: Expression<Kroner>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val erGjenoppretting: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>,
        val erSluttbehandling: Expression<Boolean>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erSluttbehandling) {
                includePhrase(BarnepensjonFellesFraser.DuHarTidligereAvslagViHarFaattNyeOplysninger)
            }

            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertNyesteUtbetalingsperiodeDatoFom = sisteBeregningsperiodeDatoFom.format()
                val formatertBeloep = sisteBeregningsperiodeBeloep.format()

                showIf(erGjenoppretting) {
                    ifNotNull(avdoed) { avdoed ->
                        val avdoedNavn = avdoed.navn
                        val formatertDoedsdato = avdoed.doedsdato.format()

                        textExpr(
                            Language.Bokmal to "Vi viser til forhåndsvarsel om ny barnepensjon. Du er innvilget barnepensjon på nytt fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                            Language.Nynorsk to "Vi viser til førehandsvarselet om ny barnepensjon. Du er innvilga ny barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                            Language.English to "We refer to the advance notice about a new children’s pension scheme. You have been granted a children's pension again from ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on " + formatertDoedsdato + ". ",
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to "Vi viser til forhåndsvarsel om ny barnepensjon. Du er innvilget barnepensjon på nytt fra ".expr() + formatertVirkningsdato + ". ",
                            Language.Nynorsk to "Vi viser til førehandsvarselet om ny barnepensjon. Du er innvilga ny barnepensjon frå og med ".expr() + formatertVirkningsdato + ". ",
                            Language.English to "We refer to the advance notice about a new children’s pension scheme. You have been granted a children's pension again from ".expr() + formatertVirkningsdato + ". ",
                        )
                    }
                }.orShow {
                    ifNotNull(avdoed) { avdoed ->
                        val avdoedNavn = avdoed.navn
                        val formatertDoedsdato = avdoed.doedsdato.format()

                        textExpr(
                            Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                            Language.Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                            Language.English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on " + formatertDoedsdato + ". ",
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + ". ",
                            Language.Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + ". ",
                            Language.English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + ". ",
                        )
                    }
                }

                showIf(harUtbetaling) {
                    showIf(harFlereUtbetalingsperioder) {
                        textExpr(
                            Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ".",
                            Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom + ".",
                            Language.English to "You will receive NOK ".expr() + formatertBeloep + " each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom + ".",
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.",
                            Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt.",
                            Language.English to "You will receive NOK ".expr() + formatertBeloep + " each month before tax.",
                        )
                    }
                }.orShow {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du" +
                            " mottar i uføretrygd fra Nav.",
                        Language.Nynorsk to "Du får ikkje utbetalt barnepensjon, då denne har blitt redusert med" +
                            " utgangspunkt i uføretrygda du får frå Nav.",
                        Language.English to "You will not receive payments from the children’s pension because they" +
                            " have been reduced according to what you already receive in disability benefits from Nav.",
                    )
                }
            }
            paragraph {
                showIf(harUtbetaling and harFlereUtbetalingsperioder) {
                    text(
                        Language.Bokmal to "Se beløp for tidligere perioder og hvordan vi har beregnet pensjonen din " +
                            "i vedlegget “Beregning av barnepensjon”.",
                        Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå beløp for tidlegare periodar " +
                            "og korleis vi har rekna ut pensjonen.",
                        Language.English to "You can see amounts for previous periods and how we calculated your pension " +
                            "in the attachment, Calculation of Children’s Pension.",
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”.",
                        Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå korleis vi har rekna ut pensjonen din.",
                        Language.English to "You can find more information about how we have calculated your" +
                            " children's pension in the attachment, Calculating the Children's Pension.",
                    )
                }
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon gis på bakgrunn av at",
                    Language.Nynorsk to "Du kan få barnepensjon når",
                    Language.English to "The children’s pension has been granted because",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "du har mistet ene forelderen din",
                            Language.Nynorsk to "du har mista ein forelder",
                            Language.English to "you have lost a parent",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er under 20 år",
                            Language.Nynorsk to "du er under 20 år",
                            Language.English to "you are under the age of 20",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er medlem i folketrygden",
                            Language.Nynorsk to "du er medlem i folketrygda",
                            Language.English to "you are a member of the national insurance scheme",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "avdøde i de siste fem årene før dødsfallet var medlem i folketrygden " +
                                "eller fikk pensjon fra folketrygden",
                            Language.Nynorsk to "avdøyde var medlem i folketrygda eller fekk pensjon/uføretrygd frå " +
                                "folketrygda dei siste fem åra før sin død",
                            Language.English to "your parent was a member of the national insurance scheme in the " +
                                "last five years before his or her died, or received a pension or disability pension from the scheme",
                        )
                    }
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                        "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13.",
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova " +
                        "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13.",
                    Language.English to "This decision has been made pursuant to the provisions regarding " +
                        "children's pensions in the National Insurance Act – " +
                        "sections 18-2, 18-3, 18-4, 18-5, 22-12 and 22-13.",
                )
            }
        }
    }
}
