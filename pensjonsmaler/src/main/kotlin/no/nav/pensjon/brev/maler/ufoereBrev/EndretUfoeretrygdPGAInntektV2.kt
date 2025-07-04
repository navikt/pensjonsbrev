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
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoAkkumulert
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoPerAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoRestbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.virkningFom
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntektV2 : AutobrevTemplate<EndretUTPgaInntektDtoV2> {

    // PE_UT_05_100
    // Brukes for eksempel når inntektsendring skjer via Inntektsplanlegger og BPEN090
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_V2

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUTPgaInntektDtoV2::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val endretUt = uforetrygd.endringsbelop.notEqualTo(0)
        title {
            showIf(endretUt and not(btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Vi endrer utbetalingen av uføretrygden du får",
                    Nynorsk to "Vi endrar utbetalinga av uføretrygda du får",
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Vi endrer utbetalingen av uføretrygden og barnetillegget du får",
                    Nynorsk to "Vi endrar utbetalinga av uføretrygda og barnetillegget du får",
                )
            }.orShow {
                text(
                    Bokmal to "Vi endrer utbetalingen av barnetillegget du får",
                    Nynorsk to "Vi endrar utbetalinga av barnetillegget du får",
                )
            }
        }

        outline {
            title1 {
                textExpr(
                    Bokmal to "Din månedlige utbetaling fra ".expr() + virkningFom.format(),
                    Nynorsk to "Di månadlege utbetaling frå ".expr() + virkningFom.format(),
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
                                Bokmal to "Beløp før skatt per måned ",
                                Nynorsk to "Beløp før skatt per månad ",
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Uføretrygd",
                                Nynorsk to "Uføretrygd",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to uforetrygd.netto.format(CurrencyFormat) + " kroner ",
                                Nynorsk to uforetrygd.netto.format(CurrencyFormat) + " kroner ",
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for særkullsbarn ",
                                    Nynorsk to "Barnetillegg for særkullsbarn ",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggSaerkullsbarn.netto.format(CurrencyFormat) + " kroner ",
                                    Nynorsk to barnetilleggSaerkullsbarn.netto.format(CurrencyFormat) + " kroner ",
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for fellesbarn ",
                                    Nynorsk to "Barnetillegg for fellesbarn ",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggFellesbarn.netto.format(CurrencyFormat) + " kroner ",
                                    Nynorsk to barnetilleggFellesbarn.netto.format(CurrencyFormat) + " kroner ",
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Gjenlevendetillegg ",
                                    Nynorsk to "Gjenlevandetillegg ",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner ",
                                    Nynorsk to gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner ",
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Sum før skatt ",
                                Nynorsk to "Sum før skatt ",
                                FontType.BOLD
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to totalNetto.format(CurrencyFormat) + " kroner ",
                                Nynorsk to totalNetto.format(CurrencyFormat) + " kroner ",
                                FontType.BOLD
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                    Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. kvar månad. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Derfor endres utbetalingen ",
                    Nynorsk to "Difor blir utbetalinga endra "
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi endrer din utbetaling fordi vi har fått opplysninger om endring i inntekt. Vi har fått opplysningene fra deg eller fra Skatteetaten. ",
                    Nynorsk to "Vi endrar utbetalinga di fordi vi har fått opplysningar om endring i inntekt. Vi har fått opplysningane frå deg eller frå Skatteetaten. ",
                )
            }

            paragraph {
                showIf(endretUt and not(btfbEndret)) {
                    ifNotNull(forventetInntekt) { forventetInntekt ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner. ",
                            Nynorsk to "Ny forventa inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner. ",
                        )
                    }
                }.orShowIf(endretUt and btfbEndret) {
                    ifNotNull(forventetInntekt, barnetilleggFellesbarn) { forventetInntekt, barnetilleggFellesbarn ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner " +
                                    "og barnets andre forelder er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntekten til den andre forelderen påvirker bare størrelsen på barnetillegg. ",
                            Nynorsk to "Ny forventa inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner " +
                                    "og barnet sin andre forelder er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntekta til den andre forelderen påverkar berre storleiken på barnetillegg.",
                        )
                    }
                }.orShowIf(not(endretUt) and btfbEndret) {
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for barnets andre forelder er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntekten til den andre forelderen påvirker bare størrelsen på barnetillegg. ",
                            Nynorsk to "Ny forventa inntekt for barnet sin andre forelder er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntekta til den andre forelderen påverkar berre storleiken på barnetillegg.",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis du mener inntekten vi har brukt i denne beregningen er feil, kan du selv legge inn ny forventet inntekt på nav.no/inntektsplanleggeren eller kontakte oss på 55 55 33 33. ",
                    Nynorsk to "Dersom du meiner at vi har brukt feil inntekt i denne utrekninga, kan du leggje inn forventa inntekt sjølv på nav.no/inntektsplanleggeren eller kontakte oss på 55 55 33 33. ",
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjør du ingen endring, er det beregningen i dette brevet som gjelder. ",
                    Nynorsk to "Dersom du ikkje gjer nokon endring, er det utrekninga i dette brevet som gjeld. ",
                )
            }

            showIf(datoForNormertPensjonsalder.year.equalTo(virkningFom.year)) {
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du får alderspensjon fra ".expr() + datoForNormertPensjonsalder.format() + ", er inntekten justert ut fra antall måneder du får uføretrygd. ",
                        Nynorsk to "Ettersom du får alderspensjon frå ".expr() + datoForNormertPensjonsalder.format() + ", er inntekta justert ut frå kor mange månader du får uføretrygd. "
                    )
                }
            }

            showIf(uforetrygd.endringsbelop.notEqualTo(0)) {
                title1 {
                    text(
                        Bokmal to "Endring i utbetaling av uføretrygd",
                        Nynorsk to "Endring i utbetaling av uføretrygd ",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Den årlige inntekten vi har brukt i beregningen er ".expr() + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gir deg rett til en årlig utbetaling av uføretrygd på " + uforetrygd.nettoPerAr.format(CurrencyFormat) + " kroner. ",
                        Nynorsk to "Den årlege inntekta vi har lagt til grunn i utrekninga, er ".expr() + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gir deg rett til ei årleg utbetaling av uføretrygd på " + uforetrygd.nettoPerAr.format(CurrencyFormat) + " kroner. ",
                    )
                }

                showIf(uforetrygd.nettoRestbelop.equalTo(0)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fordi du allerede har fått utbetalt ".expr() + uforetrygd.nettoAkkumulert.format(CurrencyFormat) +
                                    " kroner i uføretrygd i år, vil du ikke få utbetalt uføretrygd resten av året. ",
                            Nynorsk to "Då du allereie har fått utbetalt ".expr() + uforetrygd.nettoAkkumulert.format(CurrencyFormat) +
                                    " kroner i uføretrygd i år, vil du ikkje få utbetalt uføretrygd resten av året. ",
                        )
                    }
                }

                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn din inntektsgrense på ".expr() + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                                " kroner per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten din som er høyere enn inntektsgrensen som gir lavere utbetaling av uføretrygd. ",
                        Nynorsk to "Dersom du tener meir enn din inntektsgrense på ".expr() + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                                " kroner per år, reduserer vi utbetalinga av uføretrygd. Det er berre den delen av inntekta di som er høgare enn inntektsgrensa, som gir lågare utbetaling av uføretrygd. "
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn ".expr() + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. ",
                        Nynorsk to "Viss du tener meir enn ".expr() + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikkje utbetalt uføretrygd. Du beheld likevel retten til uføretrygd utan utbetaling, og du kan igjen få utbetaling av uføretrygd dersom inntekta di seinare skulle endre seg. "
                    )
                }

                ifNotNull(gjenlevendetillegg) {
                    title1 {
                        text(
                            Bokmal to "Ditt gjenlevendetillegg ",
                            Nynorsk to "Ditt gjenlevandetillegg ",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du får gjenlevendetillegg i uføretrygden din. Endringen i din inntekt påvirker utbetalingen av gjenlevendetillegget. Gjenlevendetillegget endres med samme prosent som uføretrygden. ",
                            Nynorsk to "Du får gjenlevandetillegg i uføretrygda di. Endringa i di inntekt påverkar utbetalinga av gjenlevandetillegg. Gjenlevandetillegget endrast med same prosent som uføretrygda. ",
                        )
                    }
                }

            }

            showIf(barnetilleggSaerkullsbarn.notNull() or barnetilleggFellesbarn.notNull()) {
                title1 {
                    text(
                        Bokmal to "Barnetillegg ",
                        Nynorsk to "Barnetillegg ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn, bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. " +
                                "Inntektene og fribeløpet for beregning av barnetillegg er justert slik at det kun gjelder for den perioden du mottar barnetillegg",
                        Nynorsk to "Inntekta di verkar inn på kor mykje du får utbetalt i barnetillegg. Uføretrygda di blir teken med som inntekt. For fellesbarn bruker vi inntekta til begge foreldre når vi reknar ut storleiken på barnetillegget. " +
                                "Inntektene og fribeløpet for utrekning av barnetillegg er justert slik at det berre gjeld for den perioden du får barnetillegg. ",
                    )
                }


            }

            showIf(btfbEndret and not(btsbEndret) and barnetilleggFellesbarn.notNull()) {
                ifNotNull(barnetilleggFellesbarn) { btfb ->
                    title2 {
                        text(
                            Bokmal to "Barnetillegg for fellesbarn ",
                            Nynorsk to "Barnetillegg for fellesbarn ",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. ".expr() +
                                    "Vi har endret barnetillegget ut fra din personinntekt på " + btfb.inntektBruker.format(CurrencyFormat) +
                                    " kroner og personinntekten til barnets andre forelder på " + btfb.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år." +
                                    "Du får derfor en utbetaling av barnetillegg på " + btfb.netto.format(CurrencyFormat) + " kroner per måned fra neste måned. ",
                            Nynorsk to "Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. ".expr() +
                                    "Vi har endra barnetillegget ut frå personinntekta di på " + btfb.inntektBruker.format(CurrencyFormat) +
                                    " kroner og personinntekta til den andre forelderen på" + btfb.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får difor ei utbetaling av barnetillegg på " + btfb.netto.format(CurrencyFormat) + " kroner per månad frå neste månad. "
                        )
                    }
                }
            }.orShowIf(btsbEndret and not(btfbEndret) and barnetilleggSaerkullsbarn.notNull()) {
                ifNotNull(barnetilleggSaerkullsbarn) { btsb ->
                    title2 {
                        text(
                            Bokmal to "Barnetillegg for særkullsbarn ",
                            Nynorsk to "Barnetillegg for særkullsbarn ",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. ".expr() +
                                    "Vi har endret barnetillegget ut fra din personinntekt på " + btsb.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år." +
                                    "Du får derfor en utbetaling av barnetillegg på " + btsb.netto.format(CurrencyFormat) + " kroner per måned fra neste måned. ",
                            Nynorsk to "Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. ".expr() +
                                    "Vi har endra barnetillegget ut frå personinntekta di på ".expr() + btsb.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får difor ei utbetaling av barnetillegg på " + btsb.netto.format(CurrencyFormat) + " kroner per månad frå neste månad. "
                        )
                    }
                }
            }.orShowIf(btfbEndret and btsbEndret) {
                ifNotNull(barnetilleggFellesbarn, barnetilleggSaerkullsbarn) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    title2 {
                        text(
                            Bokmal to "Dine barnetillegg ",
                            Nynorsk to "Dine barnetillegg ",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder. ",
                            Nynorsk to "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet. "
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på ".expr() + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegg for fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. Du får derfor en utbetaling av barnetillegg på " + barnetilleggFellesbarn.netto.plus(barnetilleggSaerkullsbarn.netto).format(CurrencyFormat) + " kroner per måned fra neste måned. ",
                            Nynorsk to "Barnetillegg for særkullsbarn, er rekna ut med utgangspunkt i inntekta di på ".expr() + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegget for fellesbarn, er i tillegg rekna ut frå inntekta til den andre forelderen på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. \n" +
                                    "Du får difor ei utbetaling av barnetillegg på " + barnetilleggFellesbarn.netto.plus(barnetilleggSaerkullsbarn.netto).format(CurrencyFormat) + " kroner per månad frå neste månad."
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Du finner fullstendige beregninger i vedlegget ",
                    Nynorsk to "Du finn dei fullstendige utrekningane i vedlegget "
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(Bokmal to ".", Nynorsk to ".")
            }

            showIf(endretUt and (btfbEndret or btsbEndret) and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12. "
                    )
                }
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12. "
                    )
                }
            }.orShowIf(endretUt and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12. "
                    )
                }
            }.orShowIf(btfbEndret or btsbEndret) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 til 12-16, og 22-12. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 til 12-16, og 22-12. "
                    )
                }
            }.orShow { // kun endret ut
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12. "
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer ",
                    Nynorsk to "Du må melde frå om endringar ",
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
                    Bokmal to "Har du ingen endring i inntekten din til neste år, bør du likevel melde inn forventet inntekt. Melder du ikke fra om forventet inntekt til neste år, vil vi bruke inntekten du har oppgitt i år og justere den ved årsskiftet. ",
                    Nynorsk to "Har du inga endring i inntekta di til neste år, bør du likevel melde inn forventa inntekt. Meldar du ikkje frå om forventa inntekt til neste år, vil vi bruke inntekta du har oppgitt i år og justere ho ved årsskiftet. "
                )
                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                    text(
                        Bokmal to "Dette gjelder også annen foreldres inntekt fordi du får barnetillegg og bor med barnets andre forelder. ",
                        Nynorsk to "Dette gjeld òg inntekta til den andre forelderen fordi du får barnetillegg og bur saman med den andre forelderen til barnet. "
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde inn forventet inntekt i Inntektsplanleggeren på nav.no/inntektsplanleggeren. ",
                    Nynorsk to "Du kan melde inn forventa inntekt i inntektsplanleggjaren på nav.no/inntektsplanleggeren "
                )
            }

            paragraph {
                text(
                    Bokmal to "Alle andre endringer kan du melde inn på nav.no/uforetrygd#melde. ",
                    Nynorsk to "Alle andre endringar kan meldast inn på nav.no/uforetrygd#melde. "
                )
            }

            paragraph {
                text(
                    Bokmal to "Les mer om dette i vedlegget ",
                    Nynorsk to "Les meir om dette i vedlegget "
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(Bokmal to ".", Nynorsk to ".")
            }

            title1 {
                text(
                    Bokmal to "Etteroppgjør ",
                    Nynorsk to "Etteroppgjer ",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før. Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør. ",
                    Nynorsk to "Kvart år sjekkar vi inntektsopplysningane i skatteoppgjeret ditt for å sjå om du fekk utbetalt rett beløp frå oss året før. Viser skatteoppgjeret at du har hatt ei anna inntekt enn den inntekta vi brukte då vi rekna ut utbetalingane dine, vil vi gjere ei ny utrekning. Dette vert kalla etteroppgjer. "
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake. ",
                    Nynorsk to "Dersom du har fått for lite utbetalt, får du ei etterbetaling frå oss. Har du fått for mykje utbetalt, må du betale tilbake. "
                )
            }

            title1 {
                text(
                    Bokmal to "Inntekter som ikke skal gi lavere utbetaling av uføretrygden ",
                    Nynorsk to "Inntekter som ikkje skal gi lågare utbetaling av uføretrygd"
                )
            }
            paragraph {
                text(
                    Bokmal to "Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved: ",
                    Nynorsk to "Dette gjeld dersom du har fått utbetalt erstatning for inntektstap ved: "
                )
                list {
                    item {
                        text(
                            Bokmal to "Skade ",
                            Nynorsk to "Skade "
                        )
                        text(
                            Bokmal to "(Skadeerstatningsloven § 3-1)",
                            Nynorsk to "(Skadeerstatningslova § 3-1)",
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskade ",
                            Nynorsk to "Yrkesskade "
                        )
                        text(
                            Bokmal to "(Yrkesskadeforsikringsloven § 13)",
                            Nynorsk to "(Yrkesskadeforsikringslova § 13)",
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskade ",
                            Nynorsk to "Pasientskade "
                        )
                        text(
                            Bokmal to "(Pasientskadeloven § 4 første ledd)",
                            Nynorsk to "(Pasientskadelova § 4 første ledd)",
                            FontType.ITALIC
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to "Dette kan gjelde inntekt frå arbeid eller verksemd som var heilt avslutta før du fekk innvilga uføretrygd, til dømes: "
                )
                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd ",
                            Nynorsk to "Utbetalte feriepengar. Oppteninga må ha skjedd før du fekk innvilga uføretrygd "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten ",
                            Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med avvikling av verksemda "
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere ",
                            Nynorsk to "Produksjonstillegg og andre overføringar til gardbrukarar "
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i 2024 eller senere. ",
                    Nynorsk to "Dette kan også gjelde store etterbetalingar og pengestøtte frå Nav dersom pengestøtta er pensjonsgivande og etterbetalinga blei gjort i 2024 eller seinare.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din. ",
                    Nynorsk to "Dersom du sender oss dokumentasjon som viser at du har ei slik inntekt, kan vi rekne ut uføretrygda di på nytt. "
                )
            }

            showIf(barnetilleggFellesbarn.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Hva kan holdes utenfor personinntekten til den andre forelderen? ",
                        Nynorsk to "Kva kan haldast utanfor personinntekta til den andre forelderen? "
                    )
                    list {
                        item {
                            text (
                                Bokmal to "Erstatningsoppgjør for inntektstap dersom den andre forelderen får uføretrygd eller alderspensjon fra Nav ",
                                Nynorsk to "Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav "
                            )
                        }
                    }
                    text(
                        Bokmal to "Dersom vi får dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. ",
                        Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjera ei ny berekning. "
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Du har rett til å klage ",
                    Nynorsk to "Du har rett til å klage "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. " +
                            "I vedlegget ",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. " +
                            "I vedlegget "
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    Bokmal to " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på nav.no/klage.",
                    Nynorsk to " kan du lese meir om korleis du går fram. Du finn skjema og informasjon på nav.no/klage."
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til innsyn ",
                    Nynorsk to "Du har rett på innsyn "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg ",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegget "
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    Bokmal to " for informasjon om hvordan du går fram. ",
                    Nynorsk to " for meir informasjon om korleis du går fram. "
                )
            }

            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine ",
                    Nynorsk to "Sjekk utbetalingane dine "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                            "Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt. ",
                    Nynorsk to "Du får utbetalt uføretrygd den 20. kvar månad, eller seinast siste verkedag før denne datoen. " +
                            "Du kan sjå alle utbetalingane du har fått, på nav.no/dittnav. Her kan du også endre kontonummeret ditt. "
                )
            }

            title1 {
                text(
                    Bokmal to "Skattekort ",
                    Nynorsk to "Skattekort "
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. " +
                            "Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt.  ",
                    Nynorsk to "Uføretrygd blir skattlagt som lønsinntekt. Du treng ikkje levere skattekort ditt til oss, då vi får skatteopplysningane dine elektronisk frå Skatteetaten. " +
                            "Du bør likevel sjekke at skattekortet ditt stemmer. Ved behov kan du endre skattekortet på skatteetaten.no. Du kan også få hjelp frå Skatteetaten dersom du har spørsmål om skatt. "
                )
            }

            showIf(not(brukerBorINorge)) {
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet ",
                        Nynorsk to "Skatt for deg som bur i utlandet  "
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. " +
                                "Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor. ",
                        Nynorsk to "Bur du i utlandet og betaler kildeskatt, finn du meir informasjon om kildeskatt på skatteetaten.no. " +
                                "Dersom du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur. "
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    Nynorsk to "Har du spørsmål? "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/uforetrygd. På nav.no/kontakt kan du chatte eller skrive til oss. Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 33, hverdager kl. 09:00-15:00. ",
                    Nynorsk to "Du finn meir informasjon på nav.no/uforetrygd. På nav.no/kontakt kan du chatte med eller skrive til oss. Dersom du ikkje finn svar på nav.no, kan du ringje oss på telefon 55 55 33 33, kvardagar kl. 09:00–15:00. "
                )
            }
        }


        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
