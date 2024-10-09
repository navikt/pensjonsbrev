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
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

class OmstillingsstoenadRevurderingFraser {
    data class RevurderingVedtak(
        val erEndret: Expression<Boolean>,
        val beregning: Expression<OmstillingsstoenadBeregning>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>
    ) :
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
                                textExpr(
                                    Language.Bokmal to "Omstillingsstønaden din er endret fra ".expr() +
                                            formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner hver måned " +
                                            "før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se beløp for " +
                                            "tidligere perioder og hvordan vi har beregnet omstillingsstønaden i " +
                                            "vedlegg «Beregning av omstillingsstønad».",
                                    Language.Nynorsk to "Omstillingsstønaden din har blitt endra frå ".expr() +
                                            formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner kvar " +
                                            "månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom +
                                            ". Du finn meir informasjon om beløp for tidlegare periodar i vedlegget " +
                                            "«Utrekning av omstillingsstønad».",
                                    Language.English to "Your adjustment allowance will change on ".expr() +
                                            formatertVirkningsdato + ". You will receive NOK " + formatertBeloep +
                                            " each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom +
                                            ". Read more about amounts for previous periods and how we have calculated " +
                                            "your adjustment allowance in the attachment: Calculation of " +
                                            "adjustment allowance."
                                )
                            }
                        }.orShow {
                            paragraph {
                                textExpr(
                                    Language.Bokmal to "Omstillingsstønaden din er endret fra ".expr() +
                                            formatertVirkningsdato + ".",
                                    Language.Nynorsk to "Omstillingsstønaden din har blitt endra frå ".expr() +
                                            formatertVirkningsdato + ".",
                                    Language.English to "Your adjustment allowance will change on ".expr() +
                                            formatertVirkningsdato + "."
                                )
                            }
                            paragraph {
                                textExpr(
                                    Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned " +
                                            "før skatt. ",
                                    Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner kvar " +
                                            "månad før skatt.",
                                    Language.English to "You will receive NOK ".expr() + formatertBeloep +
                                            " each month before tax."
                                )
                            }
                            paragraph {
                                text(
                                    Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                            "«Beregning av omstillingsstønad».",
                                    Language.Nynorsk to "Du kan sjå i vedlegget «Utrekning av " +
                                            "omstillingsstønad» korleis vi har rekna ut omstillingsstønaden din.",
                                    Language.English to "Read more about how we calculated your Adjustment " +
                                            "Allowance in the attachment: Calculation of adjustment allowance.",
                                )
                            }
                        }
                    }.orShow {
                        paragraph {
                            textExpr(
                                Language.Bokmal to "Omstillingsstønaden din er endret fra ".expr() +
                                        formatertVirkningsdato + ".",
                                Language.Nynorsk to "Omstillingsstønaden din er endret fra ".expr() +
                                        formatertVirkningsdato + ".",
                                Language.English to "Your adjustment allowance will change on ".expr() +
                                        formatertVirkningsdato + "."
                            )
                        }
                        paragraph {
                            text(
                                Language.Bokmal to "Du får ikke utbetalt stønad. ",
                                Language.Nynorsk to "Du får ikkje utbetalt stønad. ",
                                Language.English to "You will not receive adjustment allowance because your " +
                                        "income is higher than the limit for receiving adjustment allowance. ",
                            )
                        }
                        paragraph {
                            text(
                                Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                        "«Beregning av omstillingsstønad».",
                                Language.Nynorsk to "Du kan sjå i vedlegget «Utrekning av omstillingsstønad» " +
                                        "korleis vi har rekna ut omstillingsstønaden din.",
                                Language.English to "Read more about how we calculated your adjustment allowance in " +
                                        "the attachment: Calculation of adjustment allowance.",
                            )
                        }
                    }

                }.orShow {
                    showIf(sanksjon){
                        paragraph {
                            textExpr(
                                Language.Bokmal to "Omstillingsstønaden din er stanset fra ".expr() + formatertVirkningsdato + ".",
                                Language.Nynorsk to "Omstillingsstønaden din er stansa frå ".expr() + formatertVirkningsdato + ".",
                                Language.English to "Your adjustment allowance has been stopped from ".expr() + formatertVirkningsdato + ".",
                            )
                        }
                    }.orShow {
                        paragraph {
                            text(
                                Language.Bokmal to "Omstillingsstønaden din er vurdert på nytt. ",
                                Language.Nynorsk to "Omstillingsstønaden din er vurdert på nytt. ",
                                Language.English to "We have re-evaluated your adjustment allowance. "
                            )
                            showIf(harUtbetaling) {
                                textExpr(
                                    Language.Bokmal to "Du får fortsatt ".expr() + formatertBeloep + " kroner per " +
                                            "måned før skatt.",
                                    Language.Nynorsk to "Du får framleis ".expr() + formatertBeloep + " kroner per månad før skatt.".expr(),
                                    Language.English to "You will continue to receive NOK ".expr() + formatertBeloep + " per month before tax.".expr()
                                )
                            }.orShow {
                                text(
                                    Language.Bokmal to "Du får fortsatt ikke utbetalt stønad.",
                                    Language.Nynorsk to "Du får framleis ikkje utbetalt stønad.",
                                    Language.English to "You will still not receive adjustment allowances because " +
                                            "your income is higher than the limit for receiving adjustment allowance."
                                )
                            }
                        }
                    }
                    paragraph {
                        text(
                            Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                    "«Beregning av omstillingsstønad».",
                            Language.Nynorsk to "Du kan sjå i vedlegget «Utrekning av omstillingsstønad» " +
                                    "korleis vi har rekna ut omstillingsstønaden din.",
                            Language.English to "Read more about how we calculated your adjustment allowance in " +
                                    "the attachment: Calculation of adjustment allowance.",
                        )
                    }
                }
        }
    }

    data class UtfallRedigerbart(val etterbetaling: Expression<Boolean>, val feilutbetaling: Expression<FeilutbetalingType?>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "(utfall jamfør tekstbibliotek)",
                    Language.Nynorsk to "(utfall jamfør tekstbibliotek)",
                    Language.English to "(utfall jamfør tekstbibliotek)",
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.Nynorsk to "Vedtaket er gjort etter føresegnene om omstillingsstønad i folketrygdlova §§ <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.English to "This decision has been made pursuant to the provisions regarding adjustment allowance in the National Insurance Act – sections <riktig paragrafhenvisning>".expr() + ifElse(etterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
            }

            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_4RG_UTEN_VARSEL)) {
                includePhrase(FeilutbetalingUnder4RGUtenVarselRevurdering)
            }
        }
    }

    data class Utbetaling(
        val erEtterbetaling: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Utbetaling av omstillingsstønad",
                    Language.Nynorsk to "Utbetaling av omstillingsstønad",
                    Language.English to "Payment of adjustment allowance",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Omstillingsstønad blir utbetalt innen den 20. i hver måned. Du finner " +
                            "utbetalingsdatoer på ${Constants.UTBETALING_URL}. Utbetalingen kan bli forsinket hvis " +
                            "den skal samordnes med ytelser du mottar fra NAV eller andre, som for eksempel " +
                            "tjenestepensjonsordninger.",
                    Language.Nynorsk to "Omstillingsstønad blir utbetalt innan den 20. i kvar månad. Du finn " +
                            "utbetalingsdatoane på ${Constants.UTBETALING_URL}. Utbetalinga kan bli forseinka dersom " +
                            "ho skal samordnast med ytingar du får frå NAV eller andre (t.d. tenestepensjonsordningar).",
                    Language.English to "The adjustment allowance is paid on or before the 20th of each month. " +
                            "You can find payout dates online: ${Constants.UTBETALING_URL}. The payment may be delayed " +
                            "if it is to be  coordinated with benefits you receive from NAV or others, such as a " +
                            "pension from an occupational pension scheme.",
                )
            }
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får etterbetalt stønad. Vanligvis vil du få denne i løpet av " +
                                "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                                "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                        Language.Nynorsk to "Du får etterbetalt stønad. Vanlegvis vil du få denne i løpet av " +
                                "tre veker. Dersom Skatteetaten eller andre ordningar har krav i etterbetalinga, " +
                                "kan ho bli forseinka. Frådrag i etterbetalinga vil gå fram av utbetalingsmeldinga. ",
                        Language.English to "You will receive a back payment on your allowance. You will usually " +
                                "receive this back payment within three weeks. If the Norwegian Tax Administration " +
                                "or other schemes are entitled to the back payment, the payment to you may be " +
                                "delayed. Deductions from the back payment will be stated in the disbursement notice.  ",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                                "etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                                "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.Nynorsk to "Det blir normalt sett bli trekt skatt av etterbetaling. Dersom " +
                                "etterbetalinga gjeld tidlegare år, vil NAV trekkje skatt etter standardsatsane til " +
                                "Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.English to "Tax is usually deducted from back payments. If the back payment " +
                                "applies to previous years, NAV will deduct the tax at the Tax Administration's " +
                                "standard rates. You can read more about the rates here: " +
                                "${Constants.SKATTETREKK_ETTERBETALING_URL}. ",
                    )
                }
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselRevurdering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er redusert tilbake i tid, har du fått for " +
                            "mye utbetalt. Beløpet er under den nedre grensen for tilbakekreving som fremgår " +
                            "av folketrygdloven § 22-15 sjette ledd, og kreves derfor ikke tilbakebetalt. ",
                    Language.Nynorsk to "Ettersom stønaden din blei redusert tilbake i tid, har du fått " +
                            "for mykje utbetalt. Beløpet er under den nedre grensa for tilbakekrevjing som går " +
                            "fram av folketrygdlova § 22-15 sjette ledd, og blir difor ikkje kravd tilbakebetalt.",
                    Language.English to "Because your allowance has been reduced retroactively, you " +
                            "received more than you were owed. The amount is below the lower limit for " +
                            "demanding repayment, as stated in Section 22-15(6) of the National Insurance Act, " +
                            "so no repayment will be demanded of you. ",
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
                    Language.English to "Incorrectly paid adjustment allowance",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse " +
                            "du mottar fra NAV. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et " +
                            "eget brev om mulig tilbakekreving av for mye utbetalt omstillingsstønad.",
                    Language.Nynorsk to "Ettersom stønaden din blei redusert tilbake i tid, har du fått for " +
                            "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av " +
                            "ei anna yting du får frå NAV. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                            "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt omstillingsstønad.",
                    Language.English to "Because your allowance has been reduced retroactively, you received " +
                            "more than you were owed. The incorrect paid amount will be deducted from the back payment " +
                            "of other benefits you receive from NAV. If the incorrect payment is greater than the " +
                            "overpayment, you will receive a separate letter about the possible repayment of " +
                            "overpaid adjustment allowance.",
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
                    Language.English to "Incorrectly paid adjustment allowance",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale tilbake " +
                            "omstillingsstønad».",
                    Language.Nynorsk to "Ettersom stønaden din blei redusert tilbake i tid, har du fått for " +
                            "mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                            "tilbake omstillingsstønad».",
                    Language.English to "Because your allowance has been reduced retroactively, you received " +
                            "more than you were owed. See the attachment Advance notice – we are assessing whether " +
                            "you must repay adjustment allowance.",
                )
            }
        }
    }

    object FeilutbetalingUnder4RGUtenVarselOpphoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Beløpet er under den nedre grensen for tilbakekreving som fremgår av " +
                            "folketrygdloven § 22-15 sjette ledd, og kreves derfor ikke tilbakebetalt.",
                    Language.Nynorsk to "Fordi omstillingsstønaden din blei avvikla tilbake i tid, har du fått " +
                            "for mykje utbetalt. Beløpet er under den nedre grensa for tilbakekrevjing som går fram " +
                            "av folketrygdlova § 22-15 sjette ledd, og blir difor ikkje kravd tilbakebetalt. ",
                    Language.English to "You have been overpaid because your Adjustmenst Allowance has been " +
                            "terminated retroactively. The amount is below the lower limit for demanding repayment, " +
                            "as stated in the National Insurance Act - Section 22-15(6). So no repayment will be " +
                            "demanded of you.",
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
                    Language.English to "Incorrectly paid adjustment allowance",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Det feilutbetalte beløpet vil bli trukket i etterbetaling av annen ytelse du " +
                            "mottar fra NAV. Hvis feilutbetalingen er større enn etterbetalingen, vil du få et eget " +
                            "brev om mulig tilbakekreving av for mye utbetalt omstillingsstønad. ",
                    Language.Nynorsk to "Ettersom stønaden din blei avvikla tilbake i tid, har du fått for " +
                            "mykje utbetalt. Det du har fått for mykje utbetalt vil trekkast i etterbetalinga av ei " +
                            "anna yting du får frå NAV. Er beløpet du har fått for mykje meir enn etterbetalinga, " +
                            "får du eiget brev om mogleg tilbakekrevjing av for mykje utbetalt omstillingsstønad. ",
                    Language.English to "You have been overpaid because your Adjustment Allowance has been " +
                            "terminated retroactively. The incorrect paid amount will be deducted from the back " +
                            "payment of other benefits you receive from NAV. If the incorrect payment is greater " +
                            "than the overpayment, you will receive a separate letter about the possible repayment " +
                            "of overpaid adjustment allowance. ",
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
                    Language.English to "Incorrectly paid adjustment allowance",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er opphørt tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel - vi vurderer om du må betale " +
                            "tilbake omstillingsstønad».",
                    Language.Nynorsk to "Ettersom omstillingsstønaden din blei avvikla tilbake i tid, har du " +
                            "fått for mykje utbetalt. Sjå vedlegget «Førehandsvarsel - vi vurderer om du må betale " +
                            "tilbake omstillingsstønad».",
                    Language.English to "You have been overpaid because your adjustment allowance has been " +
                            "terminated retroactively. See the Attachment Advance notice – we are assessing whether " +
                            "you must repay adjustment allowance.",
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                textExpr(
                    Bokmal to "Du må være i aktivitet fra seks måneder etter ".expr() +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet"),
                    Nynorsk to "Du må vere i aktivitet når det har gått seks månader sidan ".expr() +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet"),
                    English to "You are obligated to be active starting six months after the ".expr() +
                            ifElse(tidligereFamiliepleier, "after care period ended", "death"),
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Når det er gått seks måneder etter ".expr() +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                            " er du pliktig til å være i minst 50 prosent aktivitet for å motta omstillingsstønad. Les " +
                            "mer om aktivitetsplikt og hva denne innebærer i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Language.Nynorsk to "For at du skal kunne halde fram med å få omstillingsstønad når det ".expr() +
                            "har gått seks månader sidan " +
                            ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                            ", må du vere i minst 50 prosent aktivitet. I vedlegget «Informasjon til deg som får " +
                            "omstillingsstønad» kan du lese meir om aktivitetsplikta og kva denne inneber.",
                    Language.English to "Once six months have passed since the ".expr() +
                            ifElse(tidligereFamiliepleier, "care period ended", "death") +
                            ", you are obligated to be active at least 50 percent to receive the adjustment allowance. " +
                            "Read more about the activity obligation and what this involves in the attachment: " +
                            "Information for recipients of adjustment allowance.",
                )
            }
        }
    }

}