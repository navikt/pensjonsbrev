package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.vedlegg.DinAfpPrivatBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.etterbetalingAfpLivsvarig
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.etterbetalingAfpSum
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.etterbetalingKompensasjonstillegg
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.etterbetalingKronetillegg
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.virkningFom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.etterbetaling.virkningTom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.forholdstallUttak
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.justeringsbeloep
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.kompensasjonstilleggBrutto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.kompensasjonstilleggForholdstall
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.kronetilleggBrutto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.livsvarigBrutto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.opptjening
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.referansebeloep
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.totalPensjon
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggDinAfpPrivatBeregning = createAttachment<LangBokmalNynorskEnglish, DinAfpPrivatBeregningDto>(
    title = {
        text(
            bokmal { +"Slik er din AFP privat beregnet" },
            nynorsk { +"Slik er din AFP privat berekna" },
            english { +"Your private AFP calculation" },
        )
    },
    includeSakspart = false,
) {
    showIf(etterbetaling.isNotEmpty()) {
        forEach(etterbetaling) { etterbetaling ->
            title2 {
                text(
                    bokmal { +"Oversikt over hva du får fra " + etterbetaling.virkningFom.format() },
                    nynorsk { +"Oversikt over kva du får frå " + etterbetaling.virkningFom.format() },
                    english { +"Overview of what you will receive from" + etterbetaling.virkningFom.format()},
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis det har vært endringer i noen av opplysningene som ligger til grunn for beregningen eller pensjonen har vært regulert i perioden, kan dette endre hvor mye du får." },
                    nynorsk { +"Om det har vore endringar i nokre av opplysningane som ligg til grunn for berekninga eller pensjonen har vore regulert i perioden, kan dette endre kor mykje du får." },
                    english { +"If there have been changes to any of the information on which the calculation is based, or if your pension has been adjusted during this period, the amount you receive may change. " },
                )
            }

            paragraph {
                table(
                    header = {
                        column {
                            text(
                                bokmal { +"Din AFP per måned fra " + etterbetaling.virkningFom.format() + " til " + etterbetaling.virkningTom.format() + ":" },
                                nynorsk { +"AFP per månad blir slik fra " + etterbetaling.virkningFom.format() + " til " + etterbetaling.virkningTom.format() + ":" },
                                english { +"Your monthly AFP from fra " + etterbetaling.virkningFom.format() + " to " + etterbetaling.virkningTom.format() + ":" },
                            )
                        }
                        column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                            text(bokmal { +"Beløp per måned" },
                                nynorsk { +"Beløp per månad" },
                                english { +"Amount per month" })
                        }
                    },
                ) {
                    ifNotNull(etterbetaling.etterbetalingAfpLivsvarig) { brutto ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP livsvarig del" },
                                    nynorsk { +"AFP livsvarig del" },
                                    english { +"AFP lifelong amount" },
                                )
                            }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    ifNotNull(etterbetaling.etterbetalingKronetillegg) { brutto ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kronetillegg" },
                                    nynorsk { +"AFP-kronetillegg" },
                                    english { +"AFP fixed amount" },
                                )
                            }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    ifNotNull(etterbetaling.etterbetalingKompensasjonstillegg) { brutto ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                    nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                    english { +"AFP compensatory allowance (tax-free)" },
                                )
                            }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    ifNotNull(etterbetaling.etterbetalingAfpSum) { sum ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Sum AFP før skatt" },
                                    nynorsk { +"Sum AFP før skatt" },
                                    english { +"Total AFP before tax " },
                                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                                )
                            }
                            cell { includePhrase(KronerText(sum, Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
                        }
                    }
                }
            }
        }
    }

    paragraph {
        table(
            header = {
                column {
                    text(
                        bokmal { +"Din AFP per måned blir slik:" },
                        nynorsk { +"AFP per månad blir slik:" },
                        english { +"Your monthly AFP will be as follows: " },
                    )
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(bokmal { +"Beløp per måned" },
                        nynorsk { +"Beløp per månad" },
                        english { +"Amount per month" })
                }
            },
        ) {
            ifNotNull(livsvarigBrutto) { brutto ->
                row {
                    cell {
                        text(
                            bokmal { +"AFP livsvarig del" },
                            nynorsk { +"AFP livsvarig del" },
                            english { +"AFP lifelong amount" },
                        )
                    }
                    cell { includePhrase(KronerText(brutto)) }
                }
            }
            ifNotNull(kronetilleggBrutto) { brutto ->
                row {
                    cell {
                        text(
                            bokmal { +"AFP kronetillegg" },
                            nynorsk { +"AFP-kronetillegg" },
                            english { +"AFP fixed amount" },
                        )
                    }
                    cell { includePhrase(KronerText(brutto)) }
                }
            }
            ifNotNull(kompensasjonstilleggBrutto) { brutto ->
                row {
                    cell {
                        text(
                            bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                            nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                            english { +"AFP compensatory allowance (tax-free)" },
                        )
                    }
                    cell { includePhrase(KronerText(brutto)) }
                }
            }
            row {
                cell {
                    text(
                        bokmal { +"Sum AFP før skatt" },
                        nynorsk { +"Sum AFP før skatt" },
                        english { +"Total AFP before tax" },
                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                    )
                }
                cell { includePhrase(KronerText(totalPensjon, Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
            }
        }
    }

    title2 {
        text(
            bokmal { +"Slik har vi beregnet din AFP" },
            nynorsk { +"Slik har vi berekna AFP-en din" },
            english { +"How we calculated your AFP" },
        )
    }

    paragraph {
        text(
            bokmal { +"AFP livsvarig del: " },
            nynorsk { +"AFP livsvarig del: " },
            english { +"The lifelong component of AFP: " },
            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
        )
        text(
            bokmal { +"Grunnlaget for beregning av AFP er den årlige pensjonsgivende inntekten din opp til 7,1 G (grunnbeløp i folketrygden). Det gis opptjening til og med det året du fyller 61 år. AFP livsvarig del er 0,314 prosent av samlet grunnlag." },
            nynorsk { +"Grunnlaget for berekning av AFP er den årlege pensjonsgivande inntekta di opp til 7,1 G (grunnbeløp i folketrygda). Du får opptening til og med det året du fyller 61 år. AFP Livsvarig del er 0,314 prosent av samla grunnlag." },
            english { +"The basis for calculating AFP is your annual pensionable income up to 7.1 G (the National Insurance basic amount). Pension rights are accrued up to and including the year in which you turn 61. The lifelong component of AFP is 0.314 percent of the total basis." },
        )
    }

    paragraph {
        text(
            bokmal { +"AFP livsvarig del er levealdersjustert. Det er fordi vi lever lenger, og pensjonen skal fordeles over flere år. Hvert årskull får fastsatt et forholdstall. Tallet brukes for å beregne fordelingen av pensjonen din på det som er igjen av forventet levetid for årskullet ditt." },
            nynorsk { +"AFP livsvarig del er levealdersjustert. Det er fordi vi lever lenger, og pensjonen skal fordelast over fleire år. Kvart årskull får fastsett eit forholdstal. Talet blir brukt for å berekne fordelinga av pensjonen din på det som er att av forventa levetid for årskullet ditt." },
            english { +"The lifelong portion of AFP is adjusted for life expectancy. This is because people are living longer, and pension benefits must be distributed over a greater number of years. " +
                    "A factor is determined for each birth cohort. This factor is used to calculate your pension based on the remaining life expectancy of your birth cohort. " },
        )
    }

    ifNotNull(kronetilleggBrutto) { brutto ->
        showIf(brutto.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Kronetillegg: " },
                    nynorsk { +"Kronetillegg: " },
                    english { +"AFP fixed amount: " },
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )
                text(
                    bokmal { +"Du får en utbetalt en høyere andel av AFP-en din fram til du blir 67 år. " +
                            "Dette kronetillegget er normalt 1 600 kroner i måneden og utbetales til og med den måneden du fyller 67 år. Hvis livsvarig del av AFP er lav, kan kronetillegget bli lavere eller ikke bli utbetalt. " +
                            "Ved utbetaling av kronetillegg vil den livsvarige delen av AFP reduseres ved hjelp av et justeringsbeløp. Reduksjonen vil gjelde resten av tiden som pensjonist." },
                    nynorsk { +"Du får en utbetalt ein høgare del av AFP-en din fram til du blir 67 år. " +
                            "Dette kronetillegget er normalt 1 600 kroner i månaden og blir utbetalt til og med den månaden du fyller 67 år. Om livsvarig del av AFP er låg, kan kronetillegget bli lågare eller ikkje bli utbetalt. " +
                            "Ved utbetaling av kronetillegg vil den livsvarige delen av AFP reduserast ved hjelp av eit justeringsbeløp. Reduksjonen vil gjelde resten av tida som pensjonist." },
                    english { +"You will receive a larger share of your AFP until you reach the age of 67. " +
                            "This AFP fixed amount is normally NOK 1,600 per month and is paid up to and including the month in which you turn 67. If the lifelong component of AFP is low, the AFP fixed amount may be lower or may not be paid. " +
                            "When the AFP fixed amount is paid, the lifelong component of AFP is reduced by means of an adjustment amount. This reduction will apply for the rest of your retirement." },
                )
            }
        }
    }

    ifNotNull(kompensasjonstilleggBrutto) { brutto ->
        showIf(brutto.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Kompensasjonstillegg:" },
                    nynorsk { +"Kompensasjonstillegg:" },
                    english { +"AFP compensatory allowance: " },
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )
                text(
                    bokmal { +"Årskullene 1944-1962 får et kompensasjonstillegg til AFP. Dette tillegget kompenserer for at disse årskullene har begrenset mulighet til å få høyere pensjon ved å jobbe lenger. " +
                            "Kompensasjonstillegget fastsettes med utgangspunkt i et referansebeløp, og blir delt på et eget forholdstall for kompensasjonstillegget." },
                    nynorsk { +"Årskulla 1944-1962 får eit kompensasjonstillegg til AFP. Dette tillegget kompenserer for at desse årskulla har avgrensa høve til å få høgare pensjon ved å jobbe lenger. " +
                            "Kompensasjonstillegget blir fastsett med utgangspunkt i eit referansebeløp, og blir delt på eit eige forholdstal for kompensasjonstillegget." },
                    english { +"The 1944 to 1962 birth cohorts receive an AFP compensatory allowance. This allowance compensates for the limited opportunity these cohorts have to increase their pension by working longer. " +
                            "The compensatory allowance is determined based on a reference amount and is divided by a separate factor for the compensatory allowance." },
                )
            }
        }
    }

    paragraph {
        table(
            header = {
                column {
                    text(
                        bokmal { +"Opplysninger brukt i beregningen av din AFP" },
                        nynorsk { +"Opplysningar brukt i berekninga av AFP-en din" },
                        english { +"Information used to calculate your AFP" },
                    )
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        bokmal { +"" },
                        nynorsk { +"" },
                        english { +"" },
                    )
                }
            },
        ) {
            row {
                cell {
                    text(
                        bokmal { +"AFP-opptjening" },
                        nynorsk { +"AFP-opptening" },
                        english { +"AFP accrual" },
                    )
                }
                cell { includePhrase(KronerText(opptjening)) }
            }
            ifNotNull(forholdstallUttak) { forholdstall ->
                row {
                    cell {
                        text(
                            bokmal { +"Forholdstall ved uttak" },
                            nynorsk { +"Forholdstal ved uttak" },
                            english { +"Life expectancy adjustment factor" },
                        )
                    }
                    cell { text(bokmal { + forholdstall.format() }, nynorsk { + forholdstall.format() }, english { + forholdstall.format() }) }
                }
            }
            ifNotNull(justeringsbeloep) { justeringsbeloep ->
                row {
                    cell {
                        text(
                            bokmal { +"Justeringsbeløp" },
                            nynorsk { +"Justeringsbeløp" },
                            english { +"Adjustment amount" },
                        )
                    }
                    cell { includePhrase(KronerText(justeringsbeloep)) }
                }
            }
            ifNotNull(referansebeloep) { referansebelop ->
                row {
                    cell {
                        text(
                            bokmal { +"Referansebeløp" },
                            nynorsk { +"Referansebeløp" },
                            english { +"AFP compensatory allowance reference amount" },
                        )
                    }
                    cell { includePhrase(KronerText(referansebelop)) }
                }
            }
            ifNotNull(kompensasjonstilleggForholdstall) { forholdstall ->
                row {
                    cell {
                        text(
                            bokmal { +"Forholdstall" },
                            nynorsk { +"Forholdstal" },
                            english { +"AFP compensatory allowance factor" },
                        )
                    }
                    cell { text(bokmal { + forholdstall.format() }, nynorsk { + forholdstall.format() }, english { + forholdstall.format() }) }
                }
            }
        }
    }
}
