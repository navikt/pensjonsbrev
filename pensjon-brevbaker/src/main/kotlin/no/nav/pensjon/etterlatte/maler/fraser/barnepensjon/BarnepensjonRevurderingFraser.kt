package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType

object BarnepensjonRevurderingFraser {

    data class RevurderingVedtak(
        val erEndret: Expression<Boolean>,
        val beregning: Expression<BarnepensjonBeregning>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertNyesteUtbetalingsperiodeDatoFom = beregning.sisteBeregningsperiode.datoFOM.format()
            val formatertVirkningsdato = beregning.virkningsdato.format()
            val formatertBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep.format()

            showIf(erEndret) {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din er endret fra ".expr() + formatertVirkningsdato + ".",
                        Language.Nynorsk to "Barnepensjonen din er endra frå ".expr() + formatertVirkningsdato + ".",
                        Language.English to "Your children’s pension will change on ".expr() + formatertVirkningsdato + ".",
                    )
                }
                showIf(harUtbetaling) {
                    showIf(harFlereUtbetalingsperioder) {
                        paragraph {
                            textExpr(
                                Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før " +
                                        "skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se beløp for " +
                                        "tidligere perioder og hvordan vi har beregnet barnepensjonen din i vedlegg " +
                                        "«Beregning av barnepensjon».",
                                Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før " +
                                        "skatt frå " + formatertNyesteUtbetalingsperiodeDatoFom + ". I " +
                                        "vedlegget «Utrekning av barnepensjon» kan du sjå beløp for tidlegare " +
                                        "periodar og korleis vi har rekna ut barnepensjonen din.",
                                Language.English to "You will receive NOK ".expr() + formatertBeloep + " each " +
                                        "month before tax, starting on " + formatertNyesteUtbetalingsperiodeDatoFom +
                                        ". You can see amounts for previous periods and how we calculated your " +
                                        "children's pension in the attachment, Calculation of Children’s Pension."
                            )
                        }

                    }.orShow {
                        paragraph {
                            textExpr(
                                Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før " +
                                        "skatt.",
                                Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før " +
                                        "skatt.",
                                Language.English to "You will receive NOK ".expr() + formatertBeloep + " each " +
                                        "month before tax. "
                            )
                        }
                        paragraph {
                            text(
                                Language.Bokmal to "Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                        "«Beregning av barnepensjon».",
                                Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå korleis " +
                                        "vi har rekna ut barnepensjonen din.",
                                Language.English to "You can find more information about how we have calculated " +
                                        "your children's pension in the attachment, Calculation of Children's " +
                                        "Pension."
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            Language.Bokmal to "Du får ikke utbetalt pensjon.",
                            Language.Nynorsk to "Du får ikkje utbetalt pensjon.",
                            Language.English to "You will not receive the pension."
                        )
                    }
                    paragraph {
                        text(
                            Language.Bokmal to "Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                    "«Beregning av barnepensjon».",
                            Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå korleis " +
                                    "vi har rekna ut barnepensjonen din.",
                            Language.English to "You can find more information about how we have calculated " +
                                    "your children's pension in the attachment, Calculation of Children's " +
                                    "Pension."
                        )
                    }
                }

            }.orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjonen din er vurdert på nytt. ",
                        Language.Nynorsk to "Barnepensjonen din er vurdert på nytt. ",
                        Language.English to "We have re-evaluated your children’s pension. "
                    )
                    showIf(harUtbetaling) {
                        textExpr(
                            Language.Bokmal to "Du får fortsatt ".expr() + formatertBeloep + " kroner per måned " +
                                    "før skatt.",
                            Language.Nynorsk to "Du får framleis ".expr() + formatertBeloep + " kroner per månad " +
                                    "før skatt.",
                            Language.English to "You will continue to receive NOK ".expr() + formatertBeloep +
                                    " per month before tax. "
                        )
                    }.orShow {
                        text(
                            Language.Bokmal to "Du får fortsatt ikke utbetalt pensjon.",
                            Language.Nynorsk to "Du får framleis ikkje utbetalt pensjon.",
                            Language.English to "You will still not receive the pension."
                        )
                    }
                }
                paragraph {
                    text(
                        Language.Bokmal to "Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                "«Beregning av barnepensjon».",
                        Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå korleis vi " +
                                "har rekna ut barnepensjonen din.",
                        Language.English to "You can find more information about how we have calculated " +
                                "your children's pension in the attachment, Calculation of Children's " +
                                "Pension."
                    )
                }
            }
        }
    }

    data class UtfallRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val feilutbetaling: Expression<FeilutbetalingType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph { text(
                Language.Bokmal to "(utfall jamfør tekstbibliotek)",
                Language.Nynorsk to "(utfall jamfør tekstbibliotek)",
                Language.English to "(utfall jamfør tekstbibliotek)",
            ) }

            paragraph {
                textExpr(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 og § 22-13.", " og 22-12."),
                    Language.Nynorsk to "Vedtaket er gjort etter føresegnene om barnepensjon i folketrygdlova §§ <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 og § 22-13.", " og 22-12."),
                    Language.English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
            }

            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_4RG_UTEN_VARSEL)) {
                includePhrase(FeilutbetalingUnder4RGUtenVarselRevurdering)
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Fordi pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Beløpet er under den nedre grensen for tilbakekreving som fremgår av " +
                            "folketrygdloven § 22-15 sjette ledd, og kreves derfor ikke tilbakebetalt.",
                    Language.Nynorsk to "Fordi pensjonen din blei avvikla tilbake i tid, har du fått for " +
                            "mykje utbetalt. Beløpet er under den nedre grensa for tilbakekrevjing som går fram av " +
                            "folketrygdlova § 22-15 sjette ledd, og blir difor ikkje kravd tilbakebetalt.",
                    Language.English to "You have been overpaid because your pension has been terminated " +
                            "retroactively. The amount is below the lower limit for demanding repayment, as stated " +
                            "in the National Insurance Act - Section 22-15(6). So no repayment will be demanded of you.",
                )
            }
        }
    }

    object FeilutbetalingUtenVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "Feilutbetaling",
                    Language.English to "Incorrectly paid pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Siden pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                            "du mottar fra NAV. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                            "eget brev om mulig tilbakekreving av for mye utbetalt barnepensjon. ",
                    Language.Nynorsk to "Ettersom pensjonen din blei avvikla tilbake i tid, har du fått for " +
                            "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei " +
                            "anna yting du får frå NAV. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                            "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt barnepensjon. ",
                    Language.English to "You have been overpaid because your pension has been terminated " +
                            "retroactively. The incorrect paid amount will be deducted from the back payment of " +
                            "another benefit you receive from NAV. If the incorrect payment is greater than the " +
                            "back payment, you will receive a separate letter about the possible repayment of " +
                            "overpaid child pension. ",
                )
            }
        }
    }

    object FeilutbetalingMedVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "Feilutbetaling",
                    Language.English to "Incorrectly paid pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake pensjon».",
                    Language.Nynorsk to "Ettersom pensjonen din blei avvikla tilbake i tid, har du fått " +
                            "for mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                            "tilbake pensjon».",
                    Language.English to "Because you stopped receiving a pension at some time in the past, " +
                            "you received more than you were owed. See the attachment Advance notice – we are " +
                            "assessing whether you must repay children’s pension.",
                )
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Fordi pensjonen din er redusert tilbake i tid, har du fått for " +
                            "mye utbetalt. Beløpet er under den nedre grensen for tilbakekreving som fremgår " +
                            "av folketrygdloven § 22-15 sjette ledd, og kreves derfor ikke tilbakebetalt.",
                    Language.Nynorsk to "Ettersom pensjonen din blei redusert tilbake i tid, har du fått " +
                            "for mykje utbetalt. Beløpet er under den nedre grensa for tilbakekrevjing som går " +
                            "fram av folketrygdlova § 22-15 sjette ledd, og blir difor ikkje kravd tilbakebetalt.",
                    Language.English to "Because your pension has been reduced retroactively, you " +
                            "received more than you were owed. The amount is below the lower limit for " +
                            "demanding repayment, as stated in Section 22-15(6) of the National Insurance Act, " +
                            "so no repayment will be demanded of you.",
                )
            }
        }
    }

    object FeilutbetalingUtenVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "Feilutbetaling",
                    Language.English to "Incorrectly paid pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Siden pensjonen din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                            "du mottar fra NAV. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                            "eget brev om mulig tilbakekreving av for mye utbetalt barnepensjon. ",
                    Language.Nynorsk to "Fordi pensjonen din er redusert tilbake i tid, har du fått utbetalt " +
                            "for mykje. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei anna " +
                            "yting du får frå NAV. Er beløpet du har fått for mykje meir enn etterbetalinga, får du " +
                            "eiget brev om mogleg tilbakekrevjing av for mykje utbetalt barnepensjon. ",
                    Language.English to "Because your pension has been reduced retroactively, you received " +
                            "more than you were owed. The incorrect paid amount will be deducted from the back " +
                            "payment of another benefit you receive from NAV. If the incorrect payment is greater " +
                            "than the back payment, you will receive a separate letter about the possible repayment " +
                            "of overpaid child pension.",
                )
            }
        }
    }

    object FeilutbetalingMedVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "Feilutbetaling",
                    Language.English to "Incorrectly paid pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Siden pensjonen din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake " +
                            "barnepensjon».",
                    Language.Nynorsk to "Fordi pensjonen din er redusert tilbake i tid, har du fått utbetalt " +
                            "for mykje. Sjå vedlegget «Førehandsvarsel – vi vurderer om du må betale tilbake " +
                            "barnepensjon».",
                    Language.English to "Because your pension has been reduced retroactively, you received " +
                            "more than you were owed. See the attachment Advance notice – we are assessing whether " +
                            "you must repay children’s pension.",
                )
            }
        }
    }
}