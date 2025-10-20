package no.nav.pensjon.brev.maler.ufoereBrev


import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.inntektBruker
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.periodisert_safe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggSaerkullsbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggSaerkullsbarnSelectors.periodisert_safe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.GjenlevendetilleggSelectors.belop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.endringsbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.gjtEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.sokerMottarApIlaAret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.totalNettoAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.virkningFom
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.INNTEKTSPLANLEGGEREN_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MELDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MINSIDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERE_JOBB_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUforetrygdPGAInntektNesteAr : AutobrevTemplate<EndretUTPgaInntektDtoV2> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_NESTE_AR

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt neste år (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val endretUt = uforetrygd.endringsbelop.notEqualTo(0)
        val harBarnetillegg = barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()
        val fellesbarnPeriodisert = barnetilleggFellesbarn.periodisert_safe.ifNull(false)
        val sarkullsbarnPeriodisert = barnetilleggSaerkullsbarn.periodisert_safe.ifNull(false)

        title {
            showIf(endretUt and not(btfbEndret or btsbEndret)) {
                text(
                    bokmal { +"Vi endrer din utbetaling av uføretrygd fra neste år" },
                    nynorsk { +"Vi endrar din utbetaling av uføretrygd frå neste år" },
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    bokmal { +"Vi endrer din utbetaling av uføretrygd og barnetillegg fra neste år" },
                    nynorsk { +"Vi endrar din utbetaling av uføretrygd og barnetillegg frå neste år" },
                )
            }.orShow {
                text(
                    bokmal { +"Vi endrer din utbetaling av barnetillegg fra neste år" },
                    nynorsk { +"Vi endrar din utbetaling av barnetillegg frå neste år" },
                )
            }
        }

        outline {
            title1 {
                text(
                    bokmal { +"Endring i utbetalingen gjelder fra " + virkningFom.format() },
                    nynorsk { +"Endring i utbetalinga gjeld frå " + virkningFom.format() },
                )
            }
            paragraph {
                table(
                    header = {
                        column(2) {}
                        column(
                            columnSpan = 1,
                            alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
                        ) {
                            text(
                                bokmal { +"Beløp før skatt per måned" },
                                nynorsk { +"Beløp før skatt per månad" },
                            )
                        }
                    }
                )
                {
                    row {
                        cell {
                            text(
                                bokmal { +"Uføretrygd" },
                                nynorsk { +"Uføretrygd" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +uforetrygd.netto.format(CurrencyFormat) + " kroner" },
                                nynorsk { +uforetrygd.netto.format(CurrencyFormat) + " kroner" },
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSB ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg for særkullsbarn" },
                                    nynorsk { +"Barnetillegg for særkullsbarn" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +barnetilleggSB.netto.format(CurrencyFormat) + " kroner" },
                                    nynorsk { +barnetilleggSB.netto.format(CurrencyFormat) + " kroner" }
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg for fellesbarn" },
                                    nynorsk { +"Barnetillegg for fellesbarn" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +barnetilleggFB.netto.format(CurrencyFormat) + " kroner" },
                                    nynorsk { +barnetilleggFB.netto.format(CurrencyFormat) + " kroner" }
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Gjenlevendetillegg" },
                                    nynorsk { +"Gjenlevandetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner" },
                                    nynorsk { +gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner" }
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Sum før skatt" },
                                nynorsk { +"Sum før skatt" },
                                FontType.BOLD
                            )
                        }
                        cell {
                            text(
                                bokmal { +totalNetto.format(CurrencyFormat) + " kroner" },
                                nynorsk { +totalNetto.format(CurrencyFormat) + " kroner" },
                                FontType.BOLD
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Endringen av utbetalingen din i " + virkningFom.year.format() + " påvirker ikke utbetalingen din i " + virkningFom.year.minus(1).format() },
                    nynorsk { +"Endringa av utbetalinga di i " + virkningFom.year.format() + " påverkar ikkje utbetalinga di i " + virkningFom.year.minus(1).format() }
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                    nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad." },
                )
            }


            ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                showIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0) and barnetilleggFB.inntektBruker.greaterThan(0)) {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi din og annen forelders inntekt" },
                            nynorsk { +"Derfor oppjusterer vi di og annen forelders inntekt" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Vi oppjusterer din inntekt og annen forelders inntekt for neste år, fordi du ikke har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." },
                            nynorsk { +"Vi oppjusterer inntekta di og inntekta til den andre forelderen for neste år, fordi du ikkje har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." }
                        )
                    }
                }.orShowIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0)) {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi inntekten til annen forelder" },
                            nynorsk { +"Derfor oppjusterer vi inntekta til annen forelder" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Vi oppjusterer annen forelders inntekt neste år, fordi du ikke har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." },
                            nynorsk { +"Vi oppjusterer inntekta til den andre forelderen for neste år, fordi du ikkje har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." }
                        )
                    }
                }.orShow {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi inntekten din" },
                            nynorsk { +"Derfor oppjusterer vi inntekta di" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." },
                            nynorsk { +"Vi oppjusterer inntekta di for neste år, fordi du ikkje har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "." }
                        )
                    }
                }
            }.orShow {
                title1 {
                    text(
                        bokmal { +"Derfor oppjusterer vi inntekten din" },
                        nynorsk { +"Derfor oppjusterer vi inntekta di" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                            1
                        ).format() + "." },
                        nynorsk { +"Vi oppjusterer inntekta di for neste år, fordi du ikkje har meldt inn inntekt for " + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                            1
                        ).format() + "." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Fikk du innvilget uføretrygd etter januar " + virkningFom.year.minus(1)
                        .format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFom.year.format() + ". " },
                    nynorsk { +"Fekk du innvilga uføretrygd etter januar " + virkningFom.year.minus(1)
                        .format() + ", er inntekta justert opp slik at ho gjeld for heile " + virkningFom.year.format() + ". " }
                )
            }
            paragraph {
                showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) {
                    text(
                        bokmal { +"Den årlige inntekten vi vil bruke for deg i beregning av uføretrygd er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gir deg rett til en årlig utbetaling på " + totalNettoAr.format(CurrencyFormat) + " kroner i uføretrygd og barnetillegg. " },
                        nynorsk { +"Den årlege inntekta vi vil bruke for deg i utrekninga av uføretrygd er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gjev deg rett til ei årleg utbetaling på " + totalNettoAr.format(CurrencyFormat) + " kroner i uføretrygd og barnetillegg. " }
                    )
                }.orShow {
                    text(
                        bokmal { +"Den årlige inntekten vi vil bruke for deg i beregning av uføretrygd er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gir deg rett til en årlig utbetaling på " + totalNettoAr.format(CurrencyFormat) + " kroner i uføretrygd. " },
                        nynorsk { +"Den årlege inntekta vi vil bruke for deg i utrekninga av uføretrygd er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gjev deg rett til ei årleg utbetaling på " + totalNettoAr.format(CurrencyFormat) + " kroner i uføretrygd. " }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Tjener du mer enn din inntektsgrense på " + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                            " kroner per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten din som er høyere enn inntektsgrensen som gir lavere utbetaling av uføretrygd. " },
                    nynorsk { +"Dersom du tener meir enn din inntektsgrense på " + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                            " kroner per år, reduserer vi utbetalinga av uføretrygd. Det er berre den delen av inntekta di som er høgare enn inntektsgrensa, som gir lågare utbetaling av uføretrygd. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Tjener du mer enn " + uforetrygd.inntektstak.format(CurrencyFormat) +
                            " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. " },
                    nynorsk { +"Viss du tener meir enn " + uforetrygd.inntektstak.format(CurrencyFormat) +
                            " kroner per år, får du ikkje utbetalt uføretrygd. Du beheld likevel retten til uføretrygd utan utbetaling, og du kan igjen få utbetaling av uføretrygd dersom inntekta di seinare skulle endre seg. " }
                )
            }
            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                showIf(barnetilleggFellesbarn.inntektAnnenForelder.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal { +"Den årlige inntekten vi vil bruke for annen forelder er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Dette påvirker bare utbetalingen av barnetillegget. Inntil én ganger folketrygdens grunnbeløp er holdt utenfor den andre forelderens inntekt." },
                            nynorsk { +"Den årlege inntekta vi vil bruke for den andre forelderen er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Dette påverkar berre utbetalinga av barnetillegget. Inntil éin gong grunnbeløpet i folketrygda er halden utanfor inntekta til den andre forelderen." }
                        )
                    }
                }
            }
            paragraph {
                ifNotNull(barnetilleggFellesbarn) {

                    text(
                        bokmal { +"Forventer du og/eller annen forelder en annen inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL." },
                        nynorsk { +"Forventar du og/eller den andre forelderen ei anna inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL." }
                    )
                }.orShow {
                    text(
                        bokmal { +"Forventer du en annen inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL." },
                        nynorsk { +"Forventar du ei anna inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL." }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Hvis du gjør dette, får du en ny beregning og et nytt brev på $DIN_UFOERETRYGD_URL. " },
                    nynorsk { +"Hvis du gjer dette, får du ei ny berekning og eit nytt brev på $DIN_UFOERETRYGD_URL. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"På $UFOERE_JOBB_URL finner du mer informasjon, og en informasjonsfilm om hvordan du bruker inntektsplanleggeren. Trenger du mer veiledning, kan du gjerne kontakte oss: $KONTAKT_URL" },
                    nynorsk { +"På $UFOERE_JOBB_URL finn du meir informasjon, og ein informasjonsfilm om korleis du bruker inntektsplanleggeren. Treng du meir rettleiing, kan du gjerne kontakte oss: $KONTAKT_URL" }
                )
            }
            showIf(sokerMottarApIlaAret) {
                paragraph {
                    text(
                        bokmal { +"Fordi du får alderspensjon fra " + datoForNormertPensjonsalder.format() + " er inntekten justert ut fra til antall måneder du får uføretrygd." },
                        nynorsk { +"Fordi du får alderspensjon frå " + datoForNormertPensjonsalder.format() + ", er inntekta justert ut frå talet på månader du får uføretrygd." }
                    )
                }
            }
            showIf(gjtEndret) {
                title1 {
                    text(
                        bokmal { +"Ditt gjenlevendetillegg" },
                        nynorsk { +"Ditt gjenlevandetillegg" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får gjenlevendetillegg i uføretrygden din. Oppjusteringen av din inntekt påvirker utbetalingen av ditt gjenlevendetillegg. Gjenlevendetillegget endres med samme prosent som uføretrygden." },
                        nynorsk { +"Du får gjenlevandetillegg i uføretrygda di. Oppjusteringa i di inntekt påverkar utbetalinga av gjenlevandetillegg. Gjenlevandetillegget endrast med same prosent som uføretrygda." }
                    )
                }
            }
            showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) {
                title1 {
                    text(
                        bokmal { +"Barnetillegg" },
                        nynorsk { +"Barnetillegg" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. " },
                        nynorsk { +"Inntekta di verkar inn på kor mykje du får utbetalt i barnetillegg. Uføretrygda di blir teken med som inntekt. For fellesbarn bruker vi inntekta til begge foreldre når vi reknar ut storleiken på barnetillegget. " }
                    )
                }

                showIf((btfbEndret and fellesbarnPeriodisert)
                        or (btsbEndret and sarkullsbarnPeriodisert)
                        or (harBarnetillegg and sokerMottarApIlaAret)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Fordi du har barnetillegg som opphører i løpet av neste år, er inntektene og fribeløp for neste år justert slik at det kun gjelder for perioden du mottar barnetillegg." },
                            nynorsk { +"Fordi du har barnetillegg som fell bort i løpet av neste år, er inntektane og fribeløpet for neste år justerte slik at dei berre gjeld for perioden du får barnetillegg." }
                        )
                    }
                }

                showIf(btfbEndret and btsbEndret) {
                    ifNotNull(barnetilleggSaerkullsbarn, barnetilleggFellesbarn) { barnetilleggSB, barnetilleggFB ->
                        title2 {
                            text(
                                bokmal { +"Dine barnetillegg" },
                                nynorsk { +"Dine barnetillegg" }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder." },
                                nynorsk { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet." }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på " + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. Barnetillegg for " +
                                        "fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                        " kroner. Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format(CurrencyFormat) + " kroner per måned fra neste år." },
                                nynorsk { +"Barnetillegg for særkullsbarn, er rekna ut med utgangspunkt i inntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. Barnetillegget for " +
                                        "fellesbarn, er i tillegg rekna ut frå inntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                        " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format(CurrencyFormat) + " kroner per månad frå neste år." }
                            )
                        }
                    }
                }.orShow {
                    showIf(btfbEndret) {
                        ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for fellesbarn" },
                                    nynorsk { +"Barnetillegg for fellesbarn" }
                                )
                            }
                            paragraph {
                                text(
                                    bokmal { +"Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. Vi har endret barnetillegget ut fra din personinntekt på " +
                                            barnetilleggFB.inntektBruker.format(CurrencyFormat) + " kroner og personinntekten til barnets andre forelder på " + barnetilleggFB.inntektAnnenForelder.format(
                                        CurrencyFormat
                                    ) +
                                            " kroner. Du får derfor en utbetaling av barnetillegg på kroner " + barnetilleggFB.netto.format(
                                        CurrencyFormat
                                    ) + " kroner per måned fra neste år." },
                                    nynorsk { +"Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. Vi har endra barnetillegget ut frå personinntekta di på " +
                                            barnetilleggFB.inntektBruker.format(CurrencyFormat) + " kroner og personinntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format(
                                        CurrencyFormat
                                    ) +
                                            " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggFB.netto.format(
                                        CurrencyFormat
                                    ) + " kroner per månad frå neste år." }
                                )
                            }
                        }
                    }
                    showIf(btsbEndret) {
                        ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for særkullsbarn" },
                                    nynorsk { +"Barnetillegg for særkullsbarn" }
                                )
                            }
                            paragraph {
                                text(
                                    bokmal { +"Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. " +
                                            "Vi har endret barnetillegget ut fra din personinntekt på " + barnetilleggSB.inntektBruktIAvkortning.format(
                                        CurrencyFormat
                                    ) +
                                            " kroner. Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.format(
                                        CurrencyFormat
                                    ) + " kroner per måned fra neste år." },
                                    nynorsk { +"Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. " +
                                            "Vi har endra barnetillegget ut frå personinntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format(
                                        CurrencyFormat
                                    ) +
                                            " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.format(
                                        CurrencyFormat
                                    ) + " kroner per månad frå neste år." }
                                )
                            }
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du finner fullstendige beregninger i vedlegget «Opplysninger om beregningen»." },
                    nynorsk { +"Du finn fullstendige utrekningar i vedlegget «Opplysningar om utrekninga»." }
                )
            }
            paragraph {
                showIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and (btfbEndret or btsbEndret)
                            and gjtEndret //UT, BT og GJT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12." }
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and (btfbEndret or btsbEndret) //UT og BT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12." }
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and gjtEndret //UT og GJT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12." }
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0) //UT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12." }
                    )
                }.orShow { //BT
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-15 til 12-16 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-15 til 12-16 og 22-12." }
                    )
                }

            }
            title1 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Endringer i inntekt og din situasjon kan påvirke hvor mye du får utbetalt fra oss. Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling. " },
                    nynorsk { +"Endringar i inntekt og situasjonen din kan påverke kor mykje du får utbetalt frå oss. Difor er det viktig at du seier frå så snart det skjer ei endring, slik at vi kan rekne ut rett utbetaling. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan melde inn forventet inntekt i Inntektsplanleggeren på $INNTEKTSPLANLEGGEREN_URL" },
                    nynorsk { +"Du kan melde inn forventa inntekt i inntektsplanleggjaren på $INNTEKTSPLANLEGGEREN_URL" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Alle andre endringer kan du melde inn på $MELDE_URL" },
                    nynorsk { +"Alle andre endringar kan meldast inn på $MELDE_URL" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Les mer om dette i vedlegget «Dine rettigheter og plikter»." },
                    nynorsk { +"Les meir om dette i vedlegget «Dine rettar og plikter»." }
                )
            }
            title1 {
                text(
                    bokmal { +"Etteroppgjør" },
                    nynorsk { +"Etteroppgjer" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før." },
                    nynorsk { +"Kvart år sjekkar vi inntektsopplysningane i skatteoppgjeret ditt for å sjå om du har fått utbetalt rett beløp frå oss året før. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør." },
                    nynorsk { +"Viser skatteoppgjeret at du har hatt ei anna inntekt enn den inntekta vi brukte då vi rekna ut utbetalingane dine, vil vi gjere ei ny utrekning. Dette vert kalla etteroppgjer." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake." },
                    nynorsk { +"Dersom du har fått for lite utbetalt, får du ei etterbetaling frå oss. Har du fått for mykje utbetalt, må du betale tilbake." }
                )
            }
            title1 {
                text(
                    bokmal { +"Inntekter som ikke skal gi lavere utbetaling av uføretrygden" },
                    nynorsk { +"Inntekter som ikkje skal gje lågare utbetaling av uføretrygda" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved:" },
                    nynorsk { +"Det gjeld dersom du har fått utbetalt erstatning for inntektstap ved:" }
                )
                list {
                    item {
                        text(
                            bokmal { +"Skade " },
                            nynorsk { +"Skade " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Yrkesskade " },
                            nynorsk { +"Yrkesskade " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Pasientskade " },
                            nynorsk { +"Pasientskade " }
                        )
                    }
                }
                text(
                    bokmal { +"Skadeerstatningsloven § 3-1, Yrkesskadeforsikringsloven § 13, Pasientskadeloven § 4 første ledd." },
                    nynorsk { +"Skadeerstatningslova § 3-1, Yrkesskadeforsikringslova § 13, Pasientskadeloven § 4 første ledd." },
                    FontType.ITALIC
                )
            }
            paragraph {
                text(
                    bokmal { +"Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: " },
                    nynorsk { +"Dette kan gjelde inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes: " },
                )
                list {
                    item {
                        text(
                            bokmal { +"Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd. " },
                            nynorsk { +"Utbetalte feriepengar. Oppteninga må ha skjedd før du fekk innvilga uføretrygd. " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten " },
                            nynorsk { +"Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Produksjonstillegg og andre overføringer til gårdbrukere " },
                            nynorsk { +"Produksjonstillegg og andre overføringar til gardsbrukarar " }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i 2024 eller senere." },
                    nynorsk { +"Dette kan også gjelde store etterbetalingar og pengestøtte frå Nav, dersom pengestøtta er pensjonsgjevande og etterbetalinga har skjedd i 2024 eller seinare." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din." },
                    nynorsk { +"Dersom du sender oss dokumentasjon som viser at du har slik inntekt, kan vi gjere ei ny utrekning av uføretrygda di." },
                )
            }
            ifNotNull(barnetilleggFellesbarn) {
                paragraph {
                    text(
                        bokmal { +"Hva kan holdes utenfor personinntekten til den andre forelderen? " },
                        nynorsk { +"Kva kan haldast utanfor personinntekta til den andre forelderen? " },
                    )

                    list {
                        item {
                            text(
                                bokmal { +"Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav" },
                                nynorsk { +"Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav" }
                            )
                        }
                    }
                    text(
                        bokmal { +"Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. " },
                        nynorsk { +"Dersom vi mottar dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjere ei ny berekning. " },
                    )

                }
            }
            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL." },
                    nynorsk { +"Dersom du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Dine rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL." }
                )
            }

            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))


            title1 {
                text(
                    bokmal { +"Sjekk utbetalingene dine" },
                    nynorsk { +"Sjekk utbetalingane dine" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på $MINSIDE_URL. Her kan du også endre kontonummeret ditt." },
                    nynorsk { +"Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste virkedag før denne datoen. Du kan sjå alle utbetalingane du har motteke på $MINSIDE_URL. Her kan du også endre kontonummeret ditt." }
                )
            }
            title1 {
                text(
                    bokmal { +"Skattekort" },
                    nynorsk { +"Skattekort" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt. " },
                    nynorsk { +"Uføretrygd blir skattlagt som lønsinntekt. Du treng ikkje levere skattekort ditt til oss, då vi får skatteopplysningane dine elektronisk frå Skatteetaten. Du bør likevel sjekke at skattekortet ditt stemmer. Ved behov kan du endre skattekortet på skatteetaten.no. Du kan også få hjelp frå Skatteetaten dersom du har spørsmål om skatt. " }
                )
            }
            showIf(not(brukerBorINorge)) {
                title1 {
                    text(
                        bokmal { +"Skatt for deg som bor i utlandet " },
                        nynorsk { +"Skatt for deg som bur i utlandet " }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor." },
                        nynorsk { +"Bur du i utlandet og betaler kildeskatt, finn du meir informasjon om kildeskatt på skatteetaten.no. Dersom du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur." }
                    )
                }
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }

        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
