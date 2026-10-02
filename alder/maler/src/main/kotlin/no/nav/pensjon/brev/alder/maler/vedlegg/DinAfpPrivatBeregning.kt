package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.vedlegg.DinAfpPrivatBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.dinAfpPrivatBeregningDto.brukerUnder70Aar
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
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggDinAfpPrivatBeregning = createAttachment<LangBokmalNynorskEnglish, DinAfpPrivatBeregningDto>(
    title = {
        text(
            bokmal { +"Din AfP Privat Beregning" },
            nynorsk { +"Din AFP privat berekning" },
            english { +"Your private AFP calculation" },
        )
    },
    includeSakspart = false,
) {
    showIf(brukerUnder70Aar) {
        title2 {
            text(
                bokmal { +"Oversikt over hva du får fra etterbetaling.virkningFom " },
                nynorsk { +"Oversikt over kva du får frå etterbetaling.virkningFom" },
                english { +"" },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis det har vært endringer i noen av opplysningene som ligger til grunn for beregningen eller pensjonen har vært regulert i perioden, kan dette endre hvor mye du får." },
                nynorsk { +"Om det har vore endringar i nokre av opplysningane som ligg til grunn for berekninga eller pensjonen har vore regulert i perioden, kan dette endre kor mykje du får." },
                english { +"" },
            )
        }

        paragraph {
            text(
                bokmal { +"Din AFP per måned fra :" },
                nynorsk { +"AFP per månad blir slik:" },
                english { +"Your monthly contractual pension will be:" },
            )
            table(
                header = {
                    column {
                        text(
                            bokmal { +"Beløp per måned" },
                            nynorsk { +"Beløp per månad" },
                            english { +"Amount per month" },
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                    }
                },
            ) {
                ifNotNull(livsvarigBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP livsvarig del" },
                                nynorsk { +"AFP livsvarig del" },
                                english { +"Contractual pension, lifelong amount" },
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
                                english { +"Contractual pension, NOK supplement" },
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
                                english { +"Contractual pension, compensation supplement (tax-free)" },
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
                            english { +"Total contractual pension before tax" },
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell { includePhrase(KronerText(totalPensjon, Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
                }
            }
        }
    }

    paragraph {
        text(
            bokmal { +"Din AFP per måned blir slik:" },
            nynorsk { +"AFP per månad blir slik:" },
            english { +"Your monthly contractual pension will be:" },
        )
        table(
            header = {
                column {
                    text(
                        bokmal { +"Beløp per måned" },
                        nynorsk { +"Beløp per månad" },
                        english { +"Amount per month" },
                    )
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                }
            },
        ) {
            ifNotNull(livsvarigBrutto) { brutto ->
                row {
                    cell {
                        text(
                            bokmal { +"AFP livsvarig del" },
                            nynorsk { +"AFP livsvarig del" },
                            english { +"Contractual pension, lifelong amount" },
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
                            english { +"Contractual pension, NOK supplement" },
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
                            english { +"Contractual pension, compensation supplement (tax-free)" },
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
                        english { +"Total contractual pension before tax" },
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
            english { +"" },
        )
    }

    paragraph {
        text(
            bokmal { +"AFP livsvarig del:" },
            nynorsk { +"AFP livsvarig del:" },
            english { +"" },
            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
        )
        text(
            bokmal { +"Grunnlaget for beregning av AFP er den årlige pensjonsgivende inntekten din opp til 7,1 G (grunnbeløp i folketrygden). Det gis opptjening til og med det året du fyller 61 år. AFP livsvarig del er 0,314 prosent av samlet grunnlag." },
            nynorsk { +"Grunnlaget for berekning av AFP er den årlege pensjonsgivande inntekta di opp til 7,1 G (grunnbeløp i folketrygda). Du får opptening til og med det året du fyller 61 år. AFP Livsvarig del er 0,314 prosent av samla grunnlag." },
            english { +"" },
        )
    }

    paragraph {
        text(
            bokmal { +"AFP livsvarig del er levealdersjustert. Det er fordi vi lever lenger, og pensjonen skal fordeles over flere år. Hvert årskull får fastsatt et forholdstall. Tallet brukes for å beregne fordelingen av pensjonen din på det som er igjen av forventet levetid for årskullet ditt." },
            nynorsk { +"AFP livsvarig del er levealdersjustert. Det er fordi vi lever lenger, og pensjonen skal fordelast over fleire år. Kvart årskull får fastsett eit forholdstal. Talet blir brukt for å berekne fordelinga av pensjonen din på det som er att av forventa levetid for årskullet ditt." },
            english { +"" },
        )
    }

    ifNotNull(kronetilleggBrutto) { brutto ->
        showIf(brutto.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Kronetillegg:" },
                    nynorsk { +"Kronetillegg:" },
                    english { +"" },
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )
                text(
                    bokmal { +"Du får en utbetalt en høyere andel av AFP-en din fram til du blir 67 år. Dette kronetillegget er normalt 1 600 kroner i måneden og utbetales til og med den måneden du fyller 67 år. Hvis livsvarig del av AFP er lav, kan kronetillegget bli lavere eller ikke bli utbetalt. Ved utbetaling av kronetillegg vil den livsvarige delen av AFP reduseres ved hjelp av et justeringsbeløp. Reduksjonen vil gjelde resten av tiden som pensjonist." },
                    nynorsk { +"Du får en utbetalt ein høgare del av AFP-en din fram til du blir 67 år. Dette kronetillegget er normalt 1 600 kroner i månaden og blir utbetalt til og med den månaden du fyller 67 år. Om livsvarig del av AFP er låg, kan kronetillegget bli lågare eller ikkje bli utbetalt. Ved utbetaling av kronetillegg vil den livsvarige delen av AFP reduserast ved hjelp av eit justeringsbeløp. Reduksjonen vil gjelde resten av tida som pensjonist." },
                    english { +"" },
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
                    english { +"" },
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )
                text(
                    bokmal { +"Årskullene 1944-1962 får et kompensasjonstillegg til AFP. Dette tillegget kompenserer for at disse årskullene har begrenset mulighet til å få høyere pensjon ved å jobbe lenger. Kompensasjonstillegget fastsettes med utgangspunkt i et referansebeløp, og blir delt på et eget forholdstall for kompensasjonstillegget." },
                    nynorsk { +"Årskulla 1944-1962 får eit kompensasjonstillegg til AFP. Dette tillegget kompenserer for at desse årskulla har avgrensa høve til å få høgare pensjon ved å jobbe lenger. Kompensasjonstillegget blir fastsett med utgangspunkt i eit referansebeløp, og blir delt på eit eige forholdstal for kompensasjonstillegget." },
                    english { +"" },
                )
            }
        }
    }

    paragraph {
        text(
            bokmal { +"Opplysninger brukt i beregningen av din AFP" },
            nynorsk { +"Opplysningar brukt i berekninga av AFP-en din" },
            english { +"" },
        )
        table(
            header = {
                column {
                    text(
                        bokmal { +"Beløp per måned" },
                        nynorsk { +"Beløp per månad" },
                        english { +"Amount per month" },
                    )
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                }
            },
        ) {
            row {
                cell {
                    text(
                        bokmal { +"AFP-opptjening" },
                        nynorsk { +"AFP-opptening" },
                        english { +"" },
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
                            english { +"" },
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
                            english { +"" },
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
                            english { +"" },
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
                            english { +"" },
                        )
                    }
                    cell { text(bokmal { + forholdstall.format() }, nynorsk { + forholdstall.format() }, english { + forholdstall.format() }) }
                }
            }
        }
    }
}
