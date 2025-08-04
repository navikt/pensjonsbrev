package no.nav.pensjon.brev.maler.ufoereBrev


import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.inntektBruker
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggFellesbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.BarnetilleggSaerkullsbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.GjenlevendetilleggSelectors.belop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.endringsbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.gjenlevendetillegg
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
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.minus
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUforetrygdPGAInntektNesteAr : AutobrevTemplate<EndretUTPgaInntektDtoV2> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_NESTE_AR

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUTPgaInntektDtoV2::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt neste år (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val endretUt = uforetrygd.endringsbelop.notEqualTo(0)
        title {
            showIf(endretUt and not(btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Vi endrer din utbetaling av uføretrygd fra neste år",
                    Nynorsk to "Vi endrar din utbetaling av uføretrygd frå neste år",
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Vi endrer din utbetaling av uføretrygd og barnetillegg fra neste år",
                    Nynorsk to "Vi endrar din utbetaling av uføretrygd og barnetillegg frå neste år",
                )
            }.orShow {
                text(
                    Bokmal to "Vi endrer din utbetaling av barnetillegg fra neste år",
                    Nynorsk to "Vi endrar din utbetaling av barnetillegg frå neste år",
                )
            }
        }

        outline {
            title1 {
                textExpr(
                    Bokmal to "Endring i utbetalingen gjelder fra ".expr() + virkningFom.format(),
                    Nynorsk to "Endring i utbetalinga gjeld frå ".expr() + virkningFom.format(),
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
                                Bokmal to "Beløp før skatt per måned",
                                Nynorsk to "Beløp før skatt per måned",
                            )
                        }
                    }
                )
                {
                    row {
                        cell {
                            text(
                                Bokmal to "Uføretrygd",
                                Nynorsk to "Uføretrygd"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to uforetrygd.netto.format(CurrencyFormat) + " kroner",
                                Nynorsk to uforetrygd.netto.format(CurrencyFormat) + " kroner",
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSB ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for særkullsbarn",
                                    Nynorsk to "Barnetillegg for særkullsbarn",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggSB.netto.format(CurrencyFormat) + " kroner",
                                    Nynorsk to barnetilleggSB.netto.format(CurrencyFormat) + " kroner"
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for fellesbarn",
                                    Nynorsk to "Barnetillegg for fellesbarn",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggFB.netto.format(CurrencyFormat) + " kroner",
                                    Nynorsk to barnetilleggFB.netto.format(CurrencyFormat) + " kroner"
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Gjenlevendetillegg",
                                    Nynorsk to "Gjenlevendetillegg",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner",
                                    Nynorsk to gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner"
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Sum før skatt",
                                Nynorsk to "Sum før skatt",
                                FontType.BOLD
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to totalNetto.format(CurrencyFormat) + " kroner",
                                Nynorsk to totalNetto.format(CurrencyFormat) + " kroner",
                                FontType.BOLD
                            )
                        }
                    }
                }
            }
            paragraph {
                textExpr(
                    Bokmal to "Endringen av utbetalingen din i ".expr() + virkningFom.year.format() + " påvirker ikke utbetalingen din i " + virkningFom.year.format(),
                    Nynorsk to "Endringa av utbetalinga di i ".expr() + virkningFom.year.format() + " påverkar ikkje utbetalinga di i " + virkningFom.year.format()
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                    Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. kvar månad.",
                )
            }


            ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                showIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0) and barnetilleggFB.inntektBruker.greaterThan(0)) {
                    title1 {
                        text(
                            Bokmal to "Derfor oppjusterer vi din og annen forelders inntekt",
                            Nynorsk to "Derfor oppjusterer vi inntekta di og annen forelder",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi oppjusterer din inntekt og annen forelders inntekt for neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + ".",
                            Nynorsk to "Vi oppjusterer inntekta di og inntekta til den andre forelder for neste år, fordi du ikkje har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "."
                        )
                    }
                }.orShowIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0)) {
                    title1 {
                        text(
                            Bokmal to "Derfor oppjusterer vi inntekten til annen forelder",
                            Nynorsk to "Derfor oppjusterer vi inntekta til annen forelder",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi oppjusterer annen forelders inntekt neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + ".",
                            Nynorsk to "Vi oppjusterer inntekta til den andre forelder for neste år, fordi du ikkje har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + "."
                        )
                    }
                }
            }.orShow {
                title1 {
                    text(
                        Bokmal to "Derfor oppjusterer vi inntekten din",
                        Nynorsk to "Derfor oppjusterer vi inntekta di",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                            1
                        ).format() + ".",
                        Nynorsk to "Vi oppjusterer inntekta di for neste år, fordi du ikkje har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + virkningFom.year.minus(
                            1
                        ).format() + "."
                    )
                }
            }

            paragraph {
                textExpr(
                    Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + virkningFom.year.minus(1)
                        .format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFom.year.format() + ". ",
                    Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + virkningFom.year.minus(1)
                        .format() + ", er inntekta justert opp slik at ho gjeld for heile " + virkningFom.year.format() + ". "
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Den årlige inntekten vi vil bruke er ".expr() + uforetrygd.inntektBruktIAvkortning.format(
                        CurrencyFormat
                    ) +
                            " kroner, det gir deg rett til en årlig utbetaling på " + totalNettoAr.format(
                        CurrencyFormat
                    ) + " kroner. ",
                    Nynorsk to "Den årlege inntekta vi vil bruke for deg er ".expr() + uforetrygd.inntektBruktIAvkortning.format(
                        CurrencyFormat
                    ) +
                            " kroner, og det gjev deg rett til ei årleg utbetaling av uføretrygd på " + totalNettoAr.format(
                        CurrencyFormat
                    ) + " kroner. "
                )
            }
            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                showIf(barnetilleggFellesbarn.inntektAnnenForelder.greaterThan(0)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Den årlige inntekten vi vil bruke for annen forelder er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Dette påvirker bare utbetalingen av barnetillegget.",
                            Nynorsk to "Den årlege inntekta vi vil bruke for den andre forelderen, er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Dette påverkar berre utbetalinga av barnetillegget."
                        )
                    }
                }
            }
            paragraph {
                ifNotNull(barnetilleggFellesbarn) {

                    text(
                        Bokmal to "Forventer du og/eller annen forelder en annen inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL.",
                        Nynorsk to "Forventar du og/eller den andre forelderen ei anna inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL."
                    )
                }.orShow {
                    text(
                        Bokmal to "Forventer du en annen inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL.",
                        Nynorsk to "Forventar du ei anna inntekt i " + virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL."
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Hvis du gjør dette, får du en ny beregning og et nytt brev på $DIN_UFOERETRYGD_URL. ",
                    Nynorsk to "Hvis du gjer dette, får du ei ny berekning og eit nytt brev på $DIN_UFOERETRYGD_URL. "
                )
            }
            paragraph {
                text(
                    Bokmal to "På $UFOERE_JOBB_URL finner du mer informasjon, og en informasjonsfilm om hvordan du bruker inntektsplanleggeren. Trenger du mer veiledning, kan du gjerne kontakte oss: $KONTAKT_URL",
                    Nynorsk to "På $UFOERE_JOBB_URL finn du meir informasjon, og ein informasjonsfilm om korleis du bruker inntektsplanleggeren. Treng du meir rettleiing, kan du gjerne kontakte oss: $KONTAKT_URL"
                )
            }
            showIf(sokerMottarApIlaAret) {
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du får alderspensjon fra ".expr() + datoForNormertPensjonsalder.format() + " er inntekten justert ut fra til antall måneder du får uføretrygd.",
                        Nynorsk to "Fordi du får alderspensjon frå ".expr() + datoForNormertPensjonsalder.format() + ", er inntekta justert ut frå talet på månader du får uføretrygd."
                    )
                }
            }
            ifNotNull(gjenlevendetillegg) {
                title1 {
                    text(
                        Bokmal to "Ditt gjenlevendetillegg",
                        Nynorsk to "Ditt gjenlevandetillegg"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du får gjenlevendetillegg i uføretrygden din. Oppjusteringen av din inntekt påvirker utbetalingen av ditt gjenlevendetillegg. Gjenlevendetillegget endres med samme prosent som uføretrygden.",
                        Nynorsk to "Du får gjenlevandetillegg i uføretrygda di. Oppjusteringa i di inntekt påverkar utbetalinga av gjenlevandetillegg. Gjenlevandetillegget endrast med same prosent som uføretrygda."
                    )
                }
            }
            showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) {
                title1 {
                    text(
                        Bokmal to "Barnetillegg",
                        Nynorsk to "Barnetillegg"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn, bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. ",
                        Nynorsk to "Inntekta di verkar inn på kor mykje du får utbetalt i barnetillegg. Uføretrygda di blir teken med som inntekt. For fellesbarn bruker vi inntekta til begge foreldre når vi reknar ut storleiken på barnetillegget. "
                    )
                }
                ifNotNull(barnetilleggSaerkullsbarn, barnetilleggFellesbarn) { barnetilleggSB, barnetilleggFB ->
                    title2 {
                        text(
                            Bokmal to "Dine barnetillegg",
                            Nynorsk to "Dine barnetillegg"
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder.",
                            Nynorsk to "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet."
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på ".expr() + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. Barnetillegg for " +
                                    "fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                    " kroner. Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format(CurrencyFormat) + " kroner per måned fra neste år.", //TODO Endre i mapping
                            Nynorsk to "Barnetillegg for særkullsbarn, er rekna ut med utgangspunkt i inntekta di på ".expr() + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. Barnetillegget for " +
                                    "fellesbarn, er i tillegg rekna ut frå inntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                    " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format(CurrencyFormat) + " kroner per månad frå neste månad." //TODO Endre i mapping. Pluss måned eller år?
                        )
                    }
                }.orShow {
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                        title2 {
                            text(
                                Bokmal to "Barnetillegg for fellesbarn",
                                Nynorsk to "Barnetillegg for fellesbarn"
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. Vi har endret barnetillegget ut fra din personinntekt på ".expr() +
                                barnetilleggFB.inntektBruker.format(CurrencyFormat) + " kroner og personinntekten til barnets andre forelder på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                        " kroner. Du får derfor en utbetaling av barnetillegg på kroner " + barnetilleggFB.netto.format(CurrencyFormat) + " kroner per måned fra neste år.",
                                Nynorsk to "Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. Vi har endra barnetillegget ut frå personinntekta di på ".expr() +
                                        barnetilleggFB.inntektBruker.format(CurrencyFormat) + " kroner og personinntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                        " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggFB.netto.format(CurrencyFormat) + " kroner per månad frå neste månad." // TODO: måned eller år?
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSB ->
                        title2 {
                            text(
                                Bokmal to "Barnetillegg for særkullsbarn",
                                Nynorsk to "Barnetillegg for særkullsbarn"
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. ".expr() +
                                        "Vi har endret barnetillegget ut fra din personinntekt på " + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) +
                                        " kroner. Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.format(CurrencyFormat) + " kroner per måned fra neste år.",
                                Nynorsk to "Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. ".expr() +
                                        "Vi har endra barnetillegget ut frå personinntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) +
                                        " kroner. Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.format(CurrencyFormat) + " kroner per månad frå neste månad." // TODO: måned eller år?
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Du finner fullstendige beregninger i vedlegget «Slik er uføretrygden din beregnet».",
                    Nynorsk to "Du finn fullstendige utrekningar i vedlegget «Slik er uføretrygda di rekna ut»."
                )
            }
            paragraph {
                showIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and (barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull())
                            and gjenlevendetillegg.notNull() //UT, BT og GJT
                ) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12."
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and (barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) //UT og BT
                ) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12."
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and gjenlevendetillegg.notNull() //UT og GJT
                ) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12."
                    )
                }.orShow { //Bare UT
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12."
                    )
                }

            }
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar"
                )
            }
            paragraph {
                text(
                    Bokmal to "Endringer i inntekt og din situasjon kan påvirke hvor mye du får utbetalt fra oss. Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling. ",
                    Nynorsk to "Endringar i inntekt og situasjonen din kan påverke kor mykje du får utbetalt frå oss. Difor er det viktig at du seier frå så snart det skjer ei endring, slik at vi kan rekne ut rett utbetaling. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan melde inn forventet inntekt i Inntektsplanleggeren på $INNTEKTSPLANLEGGEREN_URL",
                    Nynorsk to "Du kan melde inn forventa inntekt i inntektsplanleggjaren på $INNTEKTSPLANLEGGEREN_URL"
                )
            }
            paragraph {
                text(
                    Bokmal to "Alle andre endringer kan du melde inn på $MELDE_URL",
                    Nynorsk to "Alle andre endringar kan meldast inn på $MELDE_URL"
                )
            }
            paragraph {
                text(
                    Bokmal to "Les mer om dette i vedlegget «Dine rettigheter og plikter».",
                    Nynorsk to "Les meir om dette i vedlegget «Dine rettar og plikter»."
                )
            }
            title1 {
                text(
                    Bokmal to "Etteroppgjør",
                    Nynorsk to "Etteroppgjer"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før.",
                    Nynorsk to "Kvart år sjekkar vi inntektsopplysningane i skatteoppgjeret ditt for å sjå om du har fått utbetalt rett beløp frå oss året før. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør.",
                    Nynorsk to "Viser skatteoppgjeret at du har hatt ei anna inntekt enn den inntekta vi brukte då vi rekna ut utbetalingane dine, vil vi gjere ei ny utrekning. Dette vert kalla etteroppgjer."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake.",
                    Nynorsk to "Dersom du har fått for lite utbetalt, får du ei etterbetaling frå oss. Har du fått for mykje utbetalt, må du betale tilbake."
                )
            }
            title1 {
                text(
                    Bokmal to "Inntekter som ikke skal gi lavere utbetaling av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal gje lågare utbetaling av uføretrygda"
                )
            }
            paragraph {
                text(
                    Bokmal to "Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved:",
                    Nynorsk to "Det gjeld dersom du har fått utbetalt erstatning for inntektstap ved:"
                )
                list {
                    item {
                        text(
                            Bokmal to "Skade ",
                            Nynorsk to "Skade "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskade ",
                            Nynorsk to "Yrkesskade "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskade ",
                            Nynorsk to "Pasientskade "
                        )
                    }
                }
                text(
                    Bokmal to "Skadeerstatningsloven § 3-1, Yrkesskadeforsikringsloven § 13, Pasientskadeloven § 4 første ledd.",
                    Nynorsk to "Skadeerstatningslova § 3-1, Yrkesskadeforsikringslova § 13, Pasientskadeloven § 4 første ledd.",
                    FontType.ITALIC
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to "Dette kan gjelde inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes: ",
                )
                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd. ",
                            Nynorsk to "Utbetalte feriepengar. Oppteninga må ha skjedd før du fekk innvilga uføretrygd. "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten ",
                            Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere ",
                            Nynorsk to "Produksjonstillegg og andre overføringar til gardsbrukarar "
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i " +
                            virkningFom.year.minus(1).format() + " eller senere.",
                    Nynorsk to "Dette kan også gjelde store etterbetalingar og pengestøtte frå Nav, dersom pengestøtta er pensjonsgjevande og etterbetalinga har skjedd i " +
                            virkningFom.year.minus(1).format() + " eller seinare."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                    Nynorsk to "Dersom du sender oss dokumentasjon som viser at du har slik inntekt, kan vi gjere ei ny utrekning av uføretrygda di.",
                )
            }
            ifNotNull(barnetilleggFellesbarn) {
                paragraph {
                    text(
                        Bokmal to "Hva kan holdes utenfor personinntekten til den andre forelderen? ",
                        Nynorsk to "Kva kan haldast utanfor personinntekta til den andre forelderen? ",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav",
                                Nynorsk to "Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav"
                            )
                        }
                    }
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. ",
                        Nynorsk to "Dersom vi mottar dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjere ei ny berekning. ",
                    )

                }
            }
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL.",
                    Nynorsk to "Dersom du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Dine rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL."
                )
            }
            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram./klage.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Dine rettar og plikter» for informasjon om korleis du går fram."
                )
            }

            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))


            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine",
                    Nynorsk to "Sjekk utbetalingane dine"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på $MINSIDE_URL. Her kan du også endre kontonummeret ditt.",
                    Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste virkedag før denne datoen. Du kan sjå alle utbetalingane du har motteke på $MINSIDE_URL. Her kan du også endre kontonummeret ditt."
                )
            }
            title1 {
                text(
                    Bokmal to "Skattekort",
                    Nynorsk to "Skattekort"
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt. ",
                    Nynorsk to "Uføretrygd blir skattlagt som lønsinntekt. Du treng ikkje levere skattekort ditt til oss, då vi får skatteopplysningane dine elektronisk frå Skatteetaten. Du bør likevel sjekke at skattekortet ditt stemmer. Ved behov kan du endre skattekortet på skatteetaten.no. Du kan også få hjelp frå Skatteetaten dersom du har spørsmål om skatt. "
                )
            }
            showIf(not(brukerBorINorge)) {
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet ",
                        Nynorsk to "Skatt for deg som bur i utlandet "
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Nynorsk to "Bur du i utlandet og betaler kildeskatt, finn du meir informasjon om kildeskatt på skatteetaten.no. Dersom du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur."
                    )
                }
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }

        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
