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
                    text(
                        bokmal { +"Barnepensjonen din er endret fra " + formatertVirkningsdato + "." },
                        nynorsk { +"Barnepensjonen din er endra frå " + formatertVirkningsdato + "." },
                        english { +"Your children’s pension will change on " + formatertVirkningsdato + "." },
                    )
                }
                showIf(harUtbetaling) {
                    showIf(harFlereUtbetalingsperioder) {
                        paragraph {
                            text(
                                bokmal { +"Du får " + formatertBeloep + " hver måned før " +
                                        "skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se beløp for " +
                                        "tidligere perioder og hvordan vi har beregnet barnepensjonen din i vedlegg " +
                                        "«Beregning av barnepensjon»." },
                                nynorsk { +"Du får " + formatertBeloep + " per månad før " +
                                        "skatt frå " + formatertNyesteUtbetalingsperiodeDatoFom + ". I " +
                                        "vedlegget «Utrekning av barnepensjon» kan du sjå beløp for tidlegare " +
                                        "periodar og korleis vi har rekna ut barnepensjonen din." },
                                english { +"You will receive " + formatertBeloep + " each " +
                                        "month before tax, starting on " + formatertNyesteUtbetalingsperiodeDatoFom +
                                        ". You can see amounts for previous periods and how we calculated your " +
                                        "children's pension in the attachment, Calculation of Children’s Pension." }
                            )
                        }

                    }.orShow {
                        paragraph {
                            text(
                                bokmal { +"Du får " + formatertBeloep + " hver måned før " +
                                        "skatt." },
                                nynorsk { +"Du får " + formatertBeloep + " per månad før " +
                                        "skatt." },
                                english { +"You will receive " + formatertBeloep + " each " +
                                        "month before tax. " }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                        "«Beregning av barnepensjon»." },
                                nynorsk { +"I vedlegget «Utrekning av barnepensjon» kan du sjå korleis " +
                                        "vi har rekna ut barnepensjonen din." },
                                english { +"You can find more information about how we have calculated " +
                                        "your children's pension in the attachment, Calculation of Children's " +
                                        "Pension." }
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Du får ikke utbetalt pensjon." },
                            nynorsk { +"Du får ikkje utbetalt pensjon." },
                            english { +"You will not receive the pension." }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                    "«Beregning av barnepensjon»." },
                            nynorsk { +"I vedlegget «Utrekning av barnepensjon» kan du sjå korleis " +
                                    "vi har rekna ut barnepensjonen din." },
                            english { +"You can find more information about how we have calculated " +
                                    "your children's pension in the attachment, Calculation of Children's " +
                                    "Pension." }
                        )
                    }
                }

            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Barnepensjonen din er vurdert på nytt. " },
                        nynorsk { +"Barnepensjonen din er vurdert på nytt. " },
                        english { +"We have re-evaluated your children’s pension. " }
                    )
                    showIf(harUtbetaling) {
                        text(
                            bokmal { +"Du får fortsatt " + formatertBeloep + " per måned " +
                                    "før skatt." },
                            nynorsk { +"Du får framleis " + formatertBeloep + " per månad " +
                                    "før skatt." },
                            english { +"You will continue to receive " + formatertBeloep +
                                    " per month before tax. " }
                        )
                    }.orShow {
                        text(
                            bokmal { +"Du får fortsatt ikke utbetalt pensjon." },
                            nynorsk { +"Du får framleis ikkje utbetalt pensjon." },
                            english { +"You will still not receive the pension." }
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Se hvordan vi har beregnet barnepensjonen din i vedlegget " +
                                "«Beregning av barnepensjon»." },
                        nynorsk { +"I vedlegget «Utrekning av barnepensjon» kan du sjå korleis vi " +
                                "har rekna ut barnepensjonen din." },
                        english { +"You can find more information about how we have calculated " +
                                "your children's pension in the attachment, Calculation of Children's " +
                                "Pension." }
                    )
                }
            }
        }
    }

    data class UtfallRedigerbart(
        val erEtterbetaling: Expression<Boolean>,
        val feilutbetaling: Expression<FeilutbetalingType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph { text(
                bokmal { +"(utfall jamfør tekstbibliotek)" },
                nynorsk { +"(utfall jamfør tekstbibliotek)" },
                english { +"(utfall jamfør tekstbibliotek)" },
            ) }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ <riktig paragrafhenvisning>" + ifElse(erEtterbetaling, ", 22-12 og § 22-13.", " og 22-12.") },
                    nynorsk { +"Vedtaket er gjort etter føresegnene om barnepensjon i folketrygdlova §§ <riktig paragrafhenvisning>" + ifElse(erEtterbetaling, ", 22-12 og § 22-13.", " og 22-12.") },
                    english { +"This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections <riktig paragrafhenvisning>" + ifElse(erEtterbetaling, ", 22-12 and 22-13.", " and 22-12.") },
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
                    bokmal { +"Fordi pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Du skal ikke betale noe tilbake, fordi vilkårene for tilbakekreving i " +
                            "folketrygdloven § 22-15 ikke er oppfylt. " },
                    nynorsk { +"Fordi pensjonen din blei avvikla tilbake i tid, har du fått for " +
                            "mykje utbetalt. Du skal ikkje betala noko tilbake, fordi vilkåra for tilbakekrevjing i " +
                            "folketrygdlova § 22-15 ikkje er oppfylt." },
                    english { +"You have been overpaid because your pension has been terminated " +
                            "retroactively. No repayment will be demanded of you because the conditions for recovery " +
                            "under the National Insurance Act, section 22-15, are not met." },
                )
            }
        }
    }

    object FeilutbetalingUtenVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Siden pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                            "du mottar fra Nav. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                            "eget brev om mulig tilbakekreving av for mye utbetalt barnepensjon. " },
                    nynorsk { +"Ettersom pensjonen din blei avvikla tilbake i tid, har du fått for " +
                            "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei " +
                            "anna yting du får frå Nav. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                            "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt barnepensjon. " },
                    english { +"You have been overpaid because your pension has been terminated " +
                            "retroactively. The incorrect paid amount will be deducted from the back payment of " +
                            "another benefit you receive from Nav. If the incorrect payment is greater than the " +
                            "back payment, you will receive a separate letter about the possible repayment of " +
                            "overpaid child pension. " },
                )
            }
        }
    }

    object FeilutbetalingMedVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fordi pensjonen din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake pensjon»." },
                    nynorsk { +"Ettersom pensjonen din blei avvikla tilbake i tid, har du fått " +
                            "for mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                            "tilbake pensjon»." },
                    english { +"Because you stopped receiving a pension at some time in the past, " +
                            "you received more than you were owed. See the attachment Advance notice – we are " +
                            "assessing whether you must repay children’s pension." },
                )
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Fordi pensjonen din er redusert tilbake i tid, har du fått for " +
                            "mye utbetalt. Du skal ikke betale noe tilbake, fordi vilkårene for tilbakekreving i " +
                            "folketrygdloven § 22-15 ikke er oppfylt." },
                    nynorsk { +"Ettersom pensjonen din blei redusert tilbake i tid, har du fått " +
                            "for mykje utbetalt. Du skal ikkje betala noko tilbake, fordi vilkåra for tilbakekrevjing " +
                            "i folketrygdlova § 22-15 ikkje er oppfylt." },
                    english { +"Because your pension has been reduced retroactively, you " +
                            "received more than you were owed. No repayment will be demanded of you because the " +
                            "conditions for recovery under the National Insurance Act, section 22-15, are not met." },
                )
            }
        }
    }

    object FeilutbetalingUtenVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Siden pensjonen din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                            "du mottar fra Nav. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                            "eget brev om mulig tilbakekreving av for mye utbetalt barnepensjon. " },
                    nynorsk { +"Fordi pensjonen din er redusert tilbake i tid, har du fått utbetalt " +
                            "for mykje. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei anna " +
                            "yting du får frå Nav. Er beløpet du har fått for mykje meir enn etterbetalinga, får du " +
                            "eiget brev om mogleg tilbakekrevjing av for mykje utbetalt barnepensjon. " },
                    english { +"Because your pension has been reduced retroactively, you received " +
                            "more than you were owed. The incorrect paid amount will be deducted from the back " +
                            "payment of another benefit you receive from Nav. If the incorrect payment is greater " +
                            "than the back payment, you will receive a separate letter about the possible repayment " +
                            "of overpaid child pension." },
                )
            }
        }
    }

    object FeilutbetalingMedVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Siden pensjonen din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake " +
                            "barnepensjon»." },
                    nynorsk { +"Fordi pensjonen din er redusert tilbake i tid, har du fått utbetalt " +
                            "for mykje. Sjå vedlegget «Førehandsvarsel – vi vurderer om du må betale tilbake " +
                            "barnepensjon»." },
                    english { +"Because your pension has been reduced retroactively, you received " +
                            "more than you were owed. See the attachment Advance notice – we are assessing whether " +
                            "you must repay children’s pension." },
                )
            }
        }
    }
}