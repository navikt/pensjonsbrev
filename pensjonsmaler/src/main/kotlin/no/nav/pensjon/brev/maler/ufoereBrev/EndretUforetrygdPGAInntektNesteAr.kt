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
                    Nynorsk to "".expr() + virkningFom.format(),
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
                                Nynorsk to "",
                            )
                        }
                    }
                )
                {
                    row {
                        cell {
                            text(
                                Bokmal to "Uføretrygd",
                                Nynorsk to ""
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
                                    Nynorsk to "",
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
                                    Nynorsk to "",
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
                                    Nynorsk to "",
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
                                Nynorsk to "",
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
                    Nynorsk to "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                    Nynorsk to "",
                )
            }


            ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                showIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0) and barnetilleggFB.inntektBruker.greaterThan(0)) {
                    title1 {
                        text(
                            Bokmal to "Derfor oppjusterer vi din og annen forelders inntekt",
                            Nynorsk to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi oppjusterer din inntekt og annen forelders inntekt for neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + ".",
                            Nynorsk to "".expr()
                        )
                    }
                }.orShowIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0)) {
                    title1 {
                        text(
                            Bokmal to "Derfor oppjusterer vi inntekten til annen forelder",
                            Nynorsk to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi oppjusterer annen forelders inntekt neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                                1
                            ).format() + ".",
                            Nynorsk to "".expr()
                        )
                    }
                }
            }.orShow {
                title1 {
                    text(
                        Bokmal to "Derfor oppjusterer vi inntekten din",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for ".expr() + virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden pr 1. mai " + virkningFom.year.minus(
                            1
                        ).format() + ".",
                        Nynorsk to "".expr()
                    )
                }
            }

            paragraph {
                textExpr(
                    Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + virkningFom.year.minus(1)
                        .format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFom.year.format() + ". ",
                    Nynorsk to "".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Den årlige inntekten vi vil bruke er".expr() + uforetrygd.inntektBruktIAvkortning.format(
                        CurrencyFormat
                    ) +
                            " kroner, det gir deg rett til en årlig utbetaling på " + totalNettoAr.format(
                        CurrencyFormat
                    ) + " kroner. ",
                    Nynorsk to "".expr()
                )
            }
            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                showIf(barnetilleggFellesbarn.inntektAnnenForelder.greaterThan(0)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Den årlige inntekten vi vil bruke for annen forelder er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Dette påvirker bare utbetalingen av barnetillegget.",
                            Nynorsk to "".expr()
                        )
                    }
                }
            }
            paragraph {
                ifNotNull(barnetilleggFellesbarn) {

                    text(
                        Bokmal to "Forventer du og/eller annen forelder en annen inntekt i 2025 er det viktig at du melder inn ny forventet inntekt på nav.no/inntektsplanleggeren.",
                        Nynorsk to ""
                    )
                }.orShow {
                    text(
                        Bokmal to "Forventer du en annen inntekt i 2025 er det viktig at du melder inn ny forventet inntekt på nav.no/inntektsplanleggeren.",
                        Nynorsk to ""
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Hvis du gjør dette, får du en ny beregning og et nytt brev på nav.no/dinuføretrygd. ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "På nav.no/uføre-jobb  finner du mer informasjon, og en informasjonsfilm om hvordan du bruker inntektsplanleggeren. Trenger du mer veiledning, kan du gjerne kontakte oss: nav.no/kontaktoss",
                    Nynorsk to ""
                )
            }
            showIf(sokerMottarApIlaAret) {
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du får alderspensjon fra ".expr() + datoForNormertPensjonsalder.format() + " er inntekten justert ut fra til antall måneder du får uføretrygd.",
                        Nynorsk to "".expr()
                    )
                }
            }
            ifNotNull(gjenlevendetillegg) {
                title1 {
                    text(
                        Bokmal to "Ditt gjenlevendetillegg",
                        Nynorsk to ""
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du får gjenlevendetillegg i uføretrygden din. Oppjusteringen av din inntekt påvirker utbetalingen av ditt gjenlevendetillegg. Gjenlevendetillegget endres med samme prosent som uføretrygden.",
                        Nynorsk to ""
                    )
                }
            }
            showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) {
                title1 {
                    text(
                        Bokmal to "Barnetillegg",
                        Nynorsk to ""
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn, bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. ",
                        Nynorsk to ""
                    )
                }
                ifNotNull(barnetilleggSaerkullsbarn, barnetilleggFellesbarn) { barnetilleggSB, barnetilleggFB ->
                    title2 {
                        text(
                            Bokmal to "Dine barnetillegg",
                            Nynorsk to ""
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder.",
                            Nynorsk to ""
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på ".expr() + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. Barnetillegg for " +
                                    "fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                    " kroner. Du får derfor en utbetaling av barnetillegg på kroner " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format(CurrencyFormat) + " kroner per måned fra neste år.", //TODO Endre i mapping
                            Nynorsk to "".expr()
                        )
                    }
                }.orShow {
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFB ->
                        title2 {
                            text(
                                Bokmal to "Barnetillegg for fellesbarn",
                                Nynorsk to ""
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. Vi har endret barnetillegget ut fra din personinntekt på ".expr() +
                                barnetilleggFB.inntektBruker.format(CurrencyFormat) + " kroner og personinntekten til barnets andre forelder på " + barnetilleggFB.inntektAnnenForelder.format(CurrencyFormat) +
                                        " kroner. Du får derfor en utbetaling av barnetillegg på kroner" + barnetilleggFB.netto.format(CurrencyFormat) +" kroner per måned fra neste år.",
                                Nynorsk to "".expr()
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSB ->
                        title2 {
                            text(
                                Bokmal to "Barnetillegg for særkullsbarn",
                                Nynorsk to ""
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. ".expr() +
                                        "Vi har endret barnetillegget ut fra din personinntekt på " + barnetilleggSB.inntektBruktIAvkortning.format(CurrencyFormat) +
                                        " kroner. Du får derfor en utbetaling av barnetillegg på kroner 2310 kroner per måned fra neste år.",
                                Nynorsk to "".expr()
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Du finner fullstendige beregninger i vedlegget «Slik er uføretrygden din beregnet».",
                    Nynorsk to ""
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
                        Nynorsk to ""
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and (barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) //UT og BT
                ) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12.",
                        Nynorsk to ""
                    )
                }.orShowIf(
                    uforetrygd.endringsbelop.notEqualTo(0)
                            and gjenlevendetillegg.notNull() //UT og GJT
                ) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12.",
                        Nynorsk to ""
                    )
                }.orShow { //Bare UT
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to ""
                    )
                }

            }
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Endringer i inntekt og din situasjon kan påvirke hvor mye du får utbetalt fra oss. Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling. ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan melde inn forventet inntekt i Inntektsplanleggeren på nav.no/inntektsplanleggeren",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Alle andre endringer kan du melde inn på nav.no/uforetrygd#melde/inntektsplanleggeren",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Les mer om dette i vedlegget \"Dine rettigheter og plikter”.",
                    Nynorsk to ""
                )
            }
            title1 {
                text(
                    Bokmal to "Etteroppgjør",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før.",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør.",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake.",
                    Nynorsk to ""
                )
            }
            title1 {
                text(
                    Bokmal to "Inntekter som ikke skal gi lavere utbetaling av uføretrygden",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved:",
                    Nynorsk to ""
                )
                list {
                    item {
                        text(
                            Bokmal to "Skade ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskade ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskade  ",
                            Nynorsk to ""
                        )
                    }
                }
                text(
                    Bokmal to "Skadeerstatningsloven § 3-1, Yrkesskadeforsikringsloven § 13, Pasientskadeloven § 4 første ledd.",
                    Nynorsk to "",
                    FontType.ITALIC
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to "",
                )
                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd. ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere ",
                            Nynorsk to ""
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                    Nynorsk to "",
                )
            }
            ifNotNull(barnetilleggFellesbarn) {
                paragraph {
                    text(
                        Bokmal to "Hva kan holdes utenfor personinntekten til den andre forelderen? ",
                        Nynorsk to "",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav",
                                Nynorsk to ""
                            )
                        }
                    }
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. ",
                        Nynorsk to "",
                    )

                }
            }
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på nav.no/klage.",
                    Nynorsk to ""
                )
            }

            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))


            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på nav.no/minside. Her kan du også endre kontonummeret ditt.",
                    Nynorsk to ""
                )
            }
            title1 {
                text(
                    Bokmal to "Skattekort",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt. ",
                    Nynorsk to ""
                )
            }
            showIf(not(brukerBorINorge)) {
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet ",
                        Nynorsk to ""
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Nynorsk to ""
                    )
                }
            }
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/uforetrygd. På nav.no/kontakt kan du chatte eller skrive til oss. Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 33, hverdager kl. 09:00-15:00. ",
                    Nynorsk to ""
                )
            }
        }

        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
