package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRevurderingRedigertbartUtfall
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRevurderingRedigertbartUtfallSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRevurderingRedigertbartUtfallSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

class OmstillingsstoenadRevurderingFraser {
    data class RevurderingVedtak(
        val erEndret: Expression<Boolean>,
        val beregning: Expression<OmstillingsstoenadBeregningRevurderingRedigertbartUtfall>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>
    ):
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertNyesteUtbetalingsperiodeDatoFom = beregning.sisteBeregningsperiode.datoFOM.format()
            val formatertVirkningsdato = beregning.virkningsdato.format()
            val formatertBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep.format()
            val sanksjon = beregning.sisteBeregningsperiode.sanksjon

            showIf(erEndret and sanksjon.not()) {
                showIf(harUtbetaling) {
                    showIf(harFlereUtbetalingsperioder) {
                        paragraph {
                            text(
                                bokmal { +"Omstillingsstønaden din er endret fra " +
                                    formatertVirkningsdato + ". Du får " + formatertBeloep + " hver måned " +
                                    "før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se beløp for " +
                                    "tidligere perioder og hvordan vi har beregnet omstillingsstønaden i " +
                                    "vedlegg «Beregning av omstillingsstønad»." },
                                nynorsk { +"Omstillingsstønaden din har blitt endra frå " +
                                    formatertVirkningsdato + ". Du får " + formatertBeloep + " kvar " +
                                    "månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom +
                                    ". Du finn meir informasjon om beløp for tidlegare periodar i vedlegget " +
                                    "«Utrekning av omstillingsstønad»." },
                                english { +"Your adjustment allowance will change on " +
                                    formatertVirkningsdato + ". You will receive " + formatertBeloep +
                                    " each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom +
                                    ". Read more about amounts for previous periods and how we have calculated " +
                                    "your adjustment allowance in the attachment: Calculation of " +
                                    "adjustment allowance." }
                            )
                        }
                    }.orShow {
                        paragraph {
                            text(
                                bokmal { +"Omstillingsstønaden din er endret fra " +
                                    formatertVirkningsdato + "." },
                                nynorsk { +"Omstillingsstønaden din har blitt endra frå " +
                                    formatertVirkningsdato + "." },
                                english { +"Your adjustment allowance will change on " +
                                    formatertVirkningsdato + "." }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Du får " + formatertBeloep + " hver måned " +
                                    "før skatt. " },
                                nynorsk { +"Du får " + formatertBeloep + " kvar " +
                                    "månad før skatt." },
                                english { +"You will receive " + formatertBeloep +
                                    " each month before tax." }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                    "«Beregning av omstillingsstønad»." },
                                nynorsk { +"Du kan sjå i vedlegget «Utrekning av " +
                                    "omstillingsstønad» korleis vi har rekna ut omstillingsstønaden din." },
                                english { +"Read more about how we calculated your adjustment " +
                                    "allowance in the attachment: Calculation of adjustment allowance." },
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Omstillingsstønaden din er endret fra " +
                                formatertVirkningsdato + "." },
                            nynorsk { +"Omstillingsstønaden din er endret fra " +
                                formatertVirkningsdato + "." },
                            english { +"Your adjustment allowance will change on " +
                                formatertVirkningsdato + "." }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du får ikke utbetalt stønad. " },
                            nynorsk { +"Du får ikkje utbetalt stønad. " },
                            english { +"You will not receive adjustment allowance because your " +
                                "income is higher than the limit for receiving adjustment allowance. " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                "«Beregning av omstillingsstønad»." },
                            nynorsk { +"Du kan sjå i vedlegget «Utrekning av omstillingsstønad» " +
                                "korleis vi har rekna ut omstillingsstønaden din." },
                            english { +"Read more about how we calculated your adjustment allowance in " +
                                "the attachment: Calculation of adjustment allowance." },
                        )
                    }
                }

            }.orShow {
                showIf(sanksjon) {
                    paragraph {
                        text(
                            bokmal { +"Omstillingsstønaden din er stanset fra " + formatertVirkningsdato + "." },
                            nynorsk { +"Omstillingsstønaden din er stansa frå " + formatertVirkningsdato + "." },
                            english { +"Your adjustment allowance has been stopped from " + formatertVirkningsdato + "." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Omstillingsstønaden din er vurdert på nytt. " },
                            nynorsk { +"Omstillingsstønaden din er vurdert på nytt. " },
                            english { +"We have re-evaluated your adjustment allowance. " }
                        )
                        showIf(harUtbetaling) {
                            text(
                                bokmal { +"Du får fortsatt " + formatertBeloep + " per " +
                                    "måned før skatt." },
                                nynorsk { +"Du får framleis " + formatertBeloep + " per månad før skatt." },
                                english { +"You will continue to receive " + formatertBeloep + " per month before tax." }
                            )
                        }.orShow {
                            text(
                                bokmal { +"Du får fortsatt ikke utbetalt stønad." },
                                nynorsk { +"Du får framleis ikkje utbetalt stønad." },
                                english { +"You will still not receive adjustment allowances because " +
                                    "your income is higher than the limit for receiving adjustment allowance." }
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                            "«Beregning av omstillingsstønad»." },
                        nynorsk { +"Du kan sjå i vedlegget «Utrekning av omstillingsstønad» " +
                            "korleis vi har rekna ut omstillingsstønaden din." },
                        english { +"Read more about how we calculated your adjustment allowance in " +
                            "the attachment: Calculation of adjustment allowance." },
                    )
                }
            }
        }
    }

    data class UtfallRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val feilutbetaling: Expression<FeilutbetalingType?>,
        val inntekt: Expression<Kroner>,
        val inntektsAar: Expression<Int>,
        val mottattInntektendringAutomatisk: Expression<LocalDate?>
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(mottattInntektendringAutomatisk) { mottattInntektendring ->
                paragraph {
                    text(
                        bokmal { +"Omstillingsstønaden din skal reduseres basert på inntekten du forventer å ha hvert kalenderår. Kun inntekt i måneder med innvilget omstillingsstønad blir medregnet." },
                        nynorsk { +"Omstillingsstønaden din skal reduserast basert på inntekta du forventar å ha kvart kalenderår. Berre inntekt i månader med innvilga omstillingsstønad blir teken med." },
                        english { +"Your adjustment allowance will be reduced based on the income you expect to earn each calendar year. Only income in months with granted adjustment allowance is included in the calculation." }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du har " + mottattInntektendring.format() + " meldt om endring i inntekten din for " + inntektsAar.format() + "." },
                        nynorsk { +"Du har " + mottattInntektendring.format() + " meldt om endring i inntekta di for " + inntektsAar.format() + "." },
                        english { +"You have " + mottattInntektendring.format() + " reported a change in your income for " + inntektsAar.format() + "." },
                    )
                }
                showIf(inntekt.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal { +"Vi har lagt til grunn at du har " + inntekt.format() + " som forventet inntekt i innvilgede måneder i " + inntektsAar.format() + "." },
                            nynorsk { +"Vi har lagt til grunn at du har " + inntekt.format() + " som forventa inntekt i innvilga månader i " + inntektsAar.format() + "." },
                            english { +"We have assumed an expected income of " + inntekt.format() + " for months with granted adjustment allowance in " + inntektsAar.format() + "." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vi har lagt til grunn at du ikke har inntekt som omstillingsstønaden skal reduseres etter i " + inntektsAar.format() + "." },
                            nynorsk { +"Vi har lagt til grunn at du ikkje har inntekt som omstillingsstønaden skal reduserast etter i " + inntektsAar.format() + "." },
                            english { +"We have assumed that you will not have any income that would reduce the adjustment allowance in " + inntektsAar.format() + "." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ 17-9, 22-12 og 22-13." },
                        nynorsk { +"Vedtaket er gjort etter føresegnene om omstillingsstønad i folketrygdlova §§ 17-9, 22-12 og 22-13." },
                        english { +"The decision has been made pursuant to the regulations on adjustment allowance in the National Insurance Act Sections 17-9, 22-12 and 22-13." }
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"(utfall jamfør tekstbibliotek)" },
                        nynorsk { +"(utfall jamfør tekstbibliotek)" },
                        english { +"(utfall jamfør tekstbibliotek)" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ <riktig paragrafhenvisning>" + ifElse(
                            etterbetaling,
                            ", 22-12 og 22-13.",
                            " og 22-12."
                        ) },
                        nynorsk { +"Vedtaket er gjort etter føresegnene om omstillingsstønad i folketrygdlova §§ <riktig paragrafhenvisning>" + ifElse(
                            etterbetaling,
                            ", 22-12 og 22-13.",
                            " og 22-12."
                        ) },
                        english { +"This decision has been made pursuant to the provisions regarding adjustment allowance in the National Insurance Act – sections <riktig paragrafhenvisning>" + ifElse(
                            etterbetaling,
                            ", 22-12 and 22-13.",
                            " and 22-12."
                        ) },
                    )
                }
            }

            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_4RG_UTEN_VARSEL)) {
                includePhrase(FeilutbetalingUnder4RGUtenVarselRevurdering)
            }
        }
    }

    data class UtbetalingMedEtterbetaling(
        val erEtterbetaling: Expression<Boolean>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            includePhrase(OmstillingsstoenadFellesFraser.Utbetaling)

            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        bokmal { +"Du får etterbetalt stønad. Vanligvis vil du få denne i løpet av " +
                            "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                            "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { +"Du får etterbetalt stønad. Vanlegvis vil du få denne i løpet av " +
                            "tre veker. Dersom Skatteetaten eller andre ordningar har krav i etterbetalinga, " +
                            "kan ho bli forseinka. Frådrag i etterbetalinga vil gå fram av utbetalingsmeldinga. " },
                        english { +"You will receive a back payment on your allowance. You will usually " +
                            "receive this back payment within three weeks. If the Norwegian Tax Administration " +
                            "or other schemes are entitled to the back payment, the payment to you may be " +
                            "delayed. Deductions from the back payment will be stated in the disbursement notice.  " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                            "etterbetalingen tidligere år trekker Nav skatt etter Skatteetatens standardsatser. " +
                            "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}." },
                        nynorsk { +"Det blir normalt sett bli trekt skatt av etterbetaling. Dersom " +
                            "etterbetalinga gjeld tidlegare år, vil Nav trekkje skatt etter standardsatsane til " +
                            "Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}." },
                        english { +"Tax is usually deducted from back payments. If the back payment " +
                            "applies to previous years, Nav will deduct the tax at the Tax Administration's " +
                            "standard rates. You can read more about the rates here: " +
                            "${Constants.SKATTETREKK_ETTERBETALING_URL}. " },
                    )
                }
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselRevurdering: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er redusert tilbake i tid, har du fått for " +
                        "mye utbetalt. Du skal ikke betale tilbake noe, fordi vilkårene for tilbakekreving i " +
                        "folketrygdloven § 22-15 ikke er oppfylt." },
                    nynorsk { +"Ettersom stønaden din blei redusert tilbake i tid, har du fått " +
                        "for mykje utbetalt. Du skal ikkje betala noko tilbake, fordi vilkåra for tilbakekrevjing i " +
                        "folketrygdlova § 22-15 ikkje er oppfylt." },
                    english { +"Because your allowance has been reduced retroactively, you " +
                        "received more than you were owed. No repayment will be demanded of you because the conditions " +
                        "for recovery under the National Insurance Act, section 22-15, are not met." },
                )
            }
        }
    }

    object FeilutbetalingUtenVarselRevurdering: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er redusert tilbake i tid, har du fått for mye " +
                        "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                        "du mottar fra Nav. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                        "eget brev om mulig tilbakekreving av for mye utbetalt omstillingsstønad." },
                    nynorsk { +"Ettersom stønaden din blei redusert tilbake i tid, har du fått for " +
                        "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av " +
                        "ei anna yting du får frå Nav. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                        "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt omstillingsstønad." },
                    english { +"Because your allowance has been reduced retroactively, you received " +
                        "more than you were owed. The incorrect paid amount will be deducted from the back payment " +
                        "of other benefits you receive from Nav. If the incorrect payment is greater than the " +
                        "overpayment, you will receive a separate letter about the possible repayment of " +
                        "overpaid adjustment allowance." },
                )
            }
        }
    }

    object FeilutbetalingMedVarselRevurdering: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er redusert tilbake i tid, har du fått for mye " +
                        "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake " +
                        "omstillingsstønad»." },
                    nynorsk { +"Ettersom stønaden din blei redusert tilbake i tid, har du fått for " +
                        "mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                        "tilbake omstillingsstønad»." },
                    english { +"Because your allowance has been reduced retroactively, you received " +
                        "more than you were owed. See the attachment Advance notice – we are assessing whether " +
                        "you must repay adjustment allowance." },
                )
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselOpphoer: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                        "utbetalt. Du skal ikke betale noe tilbake, fordi vilkårene for tilbakekreving i " +
                        "folketrygdloven § 22-15 ikke er oppfylt." },
                    nynorsk { +"Fordi omstillingsstønaden din blei avvikla tilbake i tid, har du fått " +
                        "for mykje utbetalt. Du skal ikkje betala noko tilbake, fordi vilkåra for tilbakekrevjing " +
                        "i folketrygdlova § 22-15 ikkje er oppfylt." },
                    english { +"You have been overpaid because your adjustment allowance has been " +
                        "terminated retroactively. No repayment will be demanded of you because the conditions for " +
                        "recovery under the National Insurance Act, section 22-15, are not met." },
                )
            }
        }
    }

    object FeilutbetalingUtenVarselOpphoer: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                        "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse du " +
                        "mottar fra Nav. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et eget " +
                        "brev om mulig tilbakekreving av for mye utbetalt omstillingsstønad. " },
                    nynorsk { +"Ettersom stønaden din blei avvikla tilbake i tid, har du fått for " +
                        "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei " +
                        "anna yting du får frå Nav. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                        "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt omstillingsstønad. " },
                    english { +"You have been overpaid because your adjustment allowance has been " +
                        "terminated retroactively. The incorrect paid amount will be deducted from the back " +
                        "payment of other benefits you receive from Nav. If the incorrect payment is greater " +
                        "than the overpayment, you will receive a separate letter about the possible repayment " +
                        "of overpaid adjustment allowance. " },
                )
            }
        }
    }

    object FeilutbetalingMedVarselOpphoer: OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Feilutbetaling" },
                    nynorsk { +"Feilutbetaling" },
                    english { +"Incorrectly paid adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                        "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale " +
                        "tilbake omstillingsstønad»." },
                    nynorsk { +"Ettersom omstillingsstønaden din blei avvikla tilbake i tid, har du " +
                        "fått for mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                        "tilbake omstillingsstønad»." },
                    english { +"You have been overpaid because your adjustment allowance has been " +
                        "terminated retroactively. See the Attachment Advance notice – we are assessing whether " +
                        "you must repay adjustment allowance." },
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val tidligereFamiliepleier: Expression<Boolean>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må være i aktivitet fra seks måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") },
                    nynorsk { +"Du må vere i aktivitet når det har gått seks månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") },
                    english { +"You are obligated to be active starting six months after the " +
                        ifElse(tidligereFamiliepleier, "after care period ended", "death") },
                )
            }
            paragraph {
                text(
                    bokmal { +"Når det er gått seks måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                        " er du pliktig til å være i minst 50 prosent aktivitet for å motta omstillingsstønad. Når det" +
                        " er gått 12 måneder etter dødsfallet, kan det kreves at du er i 100 prosent arbeid eller annen" +
                        " aktivitet med sikte på å komme i arbeid for å motta omstillingsstønad. Les mer om" +
                        " aktivitetsplikt og hva denne innebærer i vedlegget «Informasjon til deg som mottar omstillingsstønad»." },
                    nynorsk { +"For at du skal kunne halde fram med å få omstillingsstønad når det" +
                        " har gått seks månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                        ", må du vere i minst 50 prosent aktivitet. Når det har gått 12 månader etter dødsfallet, kan" +
                        " det krevjast at du er i 100 prosent arbeid eller anna aktivitet med sikte på å kome i arbeid" +
                        " for å få omstillingsstønad. I vedlegget «Informasjon til deg som får omstillingsstønad»" +
                        " kan du lese meir om aktivitetsplikta og kva denne inneber." },
                    english { +"Once six months have passed since the " +
                        ifElse(tidligereFamiliepleier, "care period ended", "death") +
                        ", you are obligated to be active at least 50 percent to receive the adjustment allowance." +
                        " When 12 months have passed since the death, you may be required to work full time, or" +
                        " participate in other activity with the goal of returning to work, in order to receive" +
                        " adjustment allowance. Read more about the activity obligation and what this involves in the" +
                        " attachment: Information for recipients of adjustment allowance." },
                )
            }
        }
    }

}