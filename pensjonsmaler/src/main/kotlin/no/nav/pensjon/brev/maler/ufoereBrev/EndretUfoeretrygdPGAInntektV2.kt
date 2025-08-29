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
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.totalNettoInnevarendeAr
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
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.INNTEKTSPLANLEGGEREN_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MELDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
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
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.text
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
                    bokmal { + "Vi endrer utbetalingen av uføretrygden du får " },
                    nynorsk { + "Vi endrar utbetalinga av uføretrygda du får " },
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    bokmal { + "Vi endrer utbetalingen av uføretrygden og barnetillegget du får " },
                    nynorsk { + "Vi endrar utbetalinga av uføretrygda og barnetillegget du får " },
                )
            }.orShow {
                text(
                    bokmal { + "Vi endrer utbetalingen av barnetillegget du får " },
                    nynorsk { + "Vi endrar utbetalinga av barnetillegget du får " },
                )
            }
        }

        outline {
            title1 {
                text(
                    bokmal { + "Din månedlige utbetaling fra " + virkningFom.format() },
                    nynorsk { + "Di månadlege utbetaling frå " + virkningFom.format() },
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
                                bokmal { + "Beløp før skatt per måned " },
                                nynorsk { + "Beløp før skatt per månad " },
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "Uføretrygd" },
                                nynorsk { + "Uføretrygd" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + uforetrygd.netto.format(CurrencyFormat) + " kroner " },
                                nynorsk { + uforetrygd.netto.format(CurrencyFormat) + " kroner " },
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Barnetillegg for særkullsbarn " },
                                    nynorsk { + "Barnetillegg for særkullsbarn " },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + barnetilleggSaerkullsbarn.netto.format(CurrencyFormat) + " kroner " },
                                    nynorsk { + barnetilleggSaerkullsbarn.netto.format(CurrencyFormat) + " kroner " },
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Barnetillegg for fellesbarn " },
                                    nynorsk { + "Barnetillegg for fellesbarn " },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + barnetilleggFellesbarn.netto.format(CurrencyFormat) + " kroner " },
                                    nynorsk { + barnetilleggFellesbarn.netto.format(CurrencyFormat) + " kroner " },
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Gjenlevendetillegg " },
                                    nynorsk { + "Gjenlevandetillegg " },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner " },
                                    nynorsk { + gjenlevendetillegg.belop.format(CurrencyFormat) + " kroner " },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Sum før skatt " },
                                nynorsk { + "Sum før skatt " },
                                FontType.BOLD
                            )
                        }
                        cell {
                            text(
                                bokmal { + totalNetto.format(CurrencyFormat) + " kroner " },
                                nynorsk { + totalNetto.format(CurrencyFormat) + " kroner " },
                                FontType.BOLD
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { + "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. " },
                    nynorsk { + "Uføretrygda blir framleis utbetalt seinast den 20. kvar månad. " },
                )
            }

            title1 {
                text(
                    bokmal { + "Derfor endres utbetalingen " },
                    nynorsk { + "Difor blir utbetalinga endra " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Vi endrer din utbetaling fordi vi har fått opplysninger om endring i inntekt. Vi har fått opplysningene fra deg eller fra Skatteetaten. " },
                    nynorsk { + "Vi endrar utbetalinga di fordi vi har fått opplysningar om endring i inntekt. Vi har fått opplysningane frå deg eller frå Skatteetaten. " },
                )
            }

            paragraph {
                showIf(endretUt and not(btfbEndret)) {
                    ifNotNull(forventetInntekt) { forventetInntekt ->
                        text(
                            bokmal { + "Ny forventet inntekt for deg er " + forventetInntekt.format(CurrencyFormat) + " kroner. " },
                            nynorsk { + "Ny forventa inntekt for deg er " + forventetInntekt.format(CurrencyFormat) + " kroner. " },
                        )
                    }
                }.orShowIf(endretUt and btfbEndret) {
                    ifNotNull(forventetInntekt, barnetilleggFellesbarn) { forventetInntekt, barnetilleggFellesbarn ->
                        text(
                            bokmal { + "Ny forventet inntekt for deg er " + forventetInntekt.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegg for fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntil én ganger folketrygdens grunnbeløp er holdt utenfor den andre forelderens inntekt. " },
                            nynorsk { + "Ny forventa inntekt for deg er " + forventetInntekt.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegg for fellesbarn er i tillegg rekna ut frå inntekta til den andre forelderen, som er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntil éin gong grunnbeløpet i folketrygda er halden utanfor inntekta til den andre forelderen. " },
                        )
                    }
                }.orShowIf(not(endretUt) and btfbEndret) {
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        text(
                            bokmal { + "Barnetillegg for fellesbarn er beregnet ut fra den andre forelderens inntekt på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntil én ganger folketrygdens grunnbeløp er holdt utenfor den andre forelderens inntekt. " },
                            nynorsk { + "Barnetillegg for fellesbarn er i tillegg rekna ut frå inntekta til den andre forelderen, som er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Inntil éin gong grunnbeløpet i folketrygda er halden utanfor inntekta til den andre forelderen. " },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { + "Hvis du mener inntekten vi har brukt i denne beregningen er feil, kan du selv legge inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL eller kontakte oss på $NAV_KONTAKTSENTER_TELEFON. " },
                    nynorsk { + "Dersom du meiner at vi har brukt feil inntekt i denne utrekninga, kan du leggje inn forventa inntekt sjølv på $INNTEKTSPLANLEGGEREN_URL eller kontakte oss på $NAV_KONTAKTSENTER_TELEFON. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Gjør du ingen endring, er det beregningen i dette brevet som gjelder. " },
                    nynorsk { + "Dersom du ikkje gjer nokon endring, er det utrekninga i dette brevet som gjeld. " },
                )
            }

            showIf(datoForNormertPensjonsalder.year.equalTo(virkningFom.year)) {
                paragraph {
                    text(
                        bokmal { + "Fordi du får alderspensjon fra " + datoForNormertPensjonsalder.format() + ", er inntekten justert ut fra antall måneder du får uføretrygd. " },
                        nynorsk { + "Ettersom du får alderspensjon frå " + datoForNormertPensjonsalder.format() + ", er inntekta justert ut frå kor mange månader du får uføretrygd. " }
                    )
                }
            }

            showIf(uforetrygd.endringsbelop.notEqualTo(0)) {
                title1 {
                    text(
                        bokmal { + "Endring i utbetaling av uføretrygd " },
                        nynorsk { + "Endring i utbetaling av uføretrygd " },
                    )
                }
                paragraph {
                    ifNotNull(uforetrygd.totalNettoInnevarendeAr) { totalNettoInnevarendeAr ->
                        text(
                            bokmal { + "Den årlige inntekten vi har brukt i beregningen er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Det gir deg rett til en årlig utbetaling av uføretrygd på " + totalNettoInnevarendeAr.format(CurrencyFormat) + " kroner. " },
                            nynorsk { + "Den årlege inntekta vi har lagt til grunn i utrekninga, er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Det gir deg rett til ei årleg utbetaling av uføretrygd på " + totalNettoInnevarendeAr.format(CurrencyFormat) + " kroner. " },
                        )
                    }
                }

                showIf(uforetrygd.nettoRestbelop.equalTo(0)) {
                    paragraph {
                        text(
                            bokmal { + "Fordi du allerede har fått utbetalt " + uforetrygd.nettoAkkumulert.format(CurrencyFormat) +
                                    " kroner i uføretrygd i år, vil du ikke få utbetalt uføretrygd resten av året. " },
                            nynorsk { + "Då du allereie har fått utbetalt " + uforetrygd.nettoAkkumulert.format(CurrencyFormat) +
                                    " kroner i uføretrygd i år, vil du ikkje få utbetalt uføretrygd resten av året. " },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { + "Tjener du mer enn din inntektsgrense på " + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                                " kroner per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten din som er høyere enn inntektsgrensen som gir lavere utbetaling av uføretrygd. " },
                        nynorsk { + "Dersom du tener meir enn din inntektsgrense på " + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                                " kroner per år, reduserer vi utbetalinga av uføretrygd. Det er berre den delen av inntekta di som er høgare enn inntektsgrensa, som gir lågare utbetaling av uføretrygd. " }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Tjener du mer enn " + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. " },
                        nynorsk { + "Viss du tener meir enn " + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikkje utbetalt uføretrygd. Du beheld likevel retten til uføretrygd utan utbetaling, og du kan igjen få utbetaling av uføretrygd dersom inntekta di seinare skulle endre seg. " }
                    )
                }

                ifNotNull(gjenlevendetillegg) {
                    title1 {
                        text(
                            bokmal { + "Ditt gjenlevendetillegg " },
                            nynorsk { + "Ditt gjenlevandetillegg " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Du får gjenlevendetillegg i uføretrygden din. Endringen i din inntekt påvirker utbetalingen av gjenlevendetillegget. Gjenlevendetillegget endres med samme prosent som uføretrygden. " },
                            nynorsk { + "Du får gjenlevandetillegg i uføretrygda di. Endringa i di inntekt påverkar utbetalinga av gjenlevandetillegg. Gjenlevandetillegget endrast med same prosent som uføretrygda. " },
                        )
                    }
                }

            }

            showIf(barnetilleggSaerkullsbarn.notNull() or barnetilleggFellesbarn.notNull()) {
                title1 {
                    text(
                        bokmal { + "Barnetillegg " },
                        nynorsk { + "Barnetillegg " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn, bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. " +
                                "Inntektene og fribeløpet for beregning av barnetillegg er justert slik at det kun gjelder for den perioden du mottar barnetillegg" },
                        nynorsk { + "Inntekta di verkar inn på kor mykje du får utbetalt i barnetillegg. Uføretrygda di blir teken med som inntekt. For fellesbarn bruker vi inntekta til begge foreldre når vi reknar ut storleiken på barnetillegget. " +
                                "Inntektene og fribeløpet for utrekning av barnetillegg er justert slik at det berre gjeld for den perioden du får barnetillegg. " },
                    )
                }


            }

            showIf(btfbEndret and not(btsbEndret) and barnetilleggFellesbarn.notNull()) {
                ifNotNull(barnetilleggFellesbarn) { btfb ->
                    title2 {
                        text(
                            bokmal { + "Barnetillegg for fellesbarn " },
                            nynorsk { + "Barnetillegg for fellesbarn " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. " +
                                    "Vi har endret barnetillegget ut fra din personinntekt på " + btfb.inntektBruker.format(CurrencyFormat) +
                                    " kroner og personinntekten til barnets andre forelder på " + btfb.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får derfor en utbetaling av barnetillegg på " + btfb.netto.format(CurrencyFormat) + " kroner per måned fra neste måned. " },
                            nynorsk { + "Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. " +
                                    "Vi har endra barnetillegget ut frå personinntekta di på " + btfb.inntektBruker.format(CurrencyFormat) +
                                    " kroner og personinntekta til den andre forelderen på " + btfb.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får difor ei utbetaling av barnetillegg på " + btfb.netto.format(CurrencyFormat) + " kroner per månad frå neste månad. " }
                        )
                    }
                }
            }.orShowIf(btsbEndret and not(btfbEndret) and barnetilleggSaerkullsbarn.notNull()) {
                ifNotNull(barnetilleggSaerkullsbarn) { btsb ->
                    title2 {
                        text(
                            bokmal { + "Barnetillegg for særkullsbarn " },
                            nynorsk { + "Barnetillegg for særkullsbarn " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. " +
                                    "Vi har endret barnetillegget ut fra din personinntekt på " + btsb.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får derfor en utbetaling av barnetillegg på " + btsb.netto.format(CurrencyFormat) + " kroner per måned fra neste måned. " },
                            nynorsk { + "Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. " +
                                    "Vi har endra barnetillegget ut frå personinntekta di på " + btsb.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får difor ei utbetaling av barnetillegg på " + btsb.netto.format(CurrencyFormat) + " kroner per månad frå neste månad. " }
                        )
                    }
                }
            }.orShowIf(btfbEndret and btsbEndret) {
                ifNotNull(barnetilleggFellesbarn, barnetilleggSaerkullsbarn) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    title2 {
                        text(
                            bokmal { + "Dine barnetillegg " },
                            nynorsk { + "Dine barnetillegg " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder. " },
                            nynorsk { + "Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet. " }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegg for fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får derfor en utbetaling av barnetillegg på " + barnetilleggFellesbarn.netto.plus(barnetilleggSaerkullsbarn.netto).format(CurrencyFormat) + " kroner per måned fra neste måned. " },
                            nynorsk { + "Barnetillegg for særkullsbarn, er rekna ut med utgangspunkt i inntekta di på " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegget for fellesbarn, er i tillegg rekna ut frå inntekta til den andre forelderen på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi reknar ut nytt barnetillegg, tek vi omsyn til kor mykje du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får difor ei utbetaling av barnetillegg på " + barnetilleggFellesbarn.netto.plus(barnetilleggSaerkullsbarn.netto).format(CurrencyFormat) + " kroner per månad frå neste månad." }
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { + "Du finner fullstendige beregninger i vedlegget " },
                    nynorsk { + "Du finn dei fullstendige utrekningane i vedlegget " }
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(bokmal { + "." }, nynorsk { + "." })
            }

            showIf(endretUt and (btfbEndret or btsbEndret) and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12. " }
                    )
                }
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12. " }
                    )
                }
            }.orShowIf(endretUt and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12. " }
                    )
                }
            }.orShowIf(btfbEndret or btsbEndret) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 til 12-16, og 22-12. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15 til 12-16, og 22-12. " }
                    )
                }
            }.orShow { // kun endret ut
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12. " }
                    )
                }
            }

            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer " },
                    nynorsk { + "Du må melde frå om endringar " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Endringer i inntekt og din situasjon kan påvirke hvor mye du får utbetalt fra oss. Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling. " },
                    nynorsk { + "Endringar i inntekt og situasjonen din kan påverke kor mykje du får utbetalt frå oss. Difor er det viktig at du seier frå så snart det skjer ei endring, slik at vi kan rekne ut rett utbetaling. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Har du ingen endring i inntekten din til neste år, bør du likevel melde inn forventet inntekt. Melder du ikke fra om forventet inntekt til neste år, vil vi bruke inntekten du har oppgitt i år og justere den ved årsskiftet. " },
                    nynorsk { + "Har du inga endring i inntekta di til neste år, bør du likevel melde inn forventa inntekt. Meldar du ikkje frå om forventa inntekt til neste år, vil vi bruke inntekta du har oppgitt i år og justere ho ved årsskiftet. " }
                )
                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                    text(
                        bokmal { + "Dette gjelder også annen foreldres inntekt fordi du får barnetillegg og bor med barnets andre forelder. " },
                        nynorsk { + "Dette gjeld òg inntekta til den andre forelderen fordi du får barnetillegg og bur saman med den andre forelderen til barnet. " }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Du kan melde inn forventet inntekt i Inntektsplanleggeren på $INNTEKTSPLANLEGGEREN_URL. " },
                    nynorsk { + "Du kan melde inn forventa inntekt i inntektsplanleggjaren på $INNTEKTSPLANLEGGEREN_URL. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Alle andre endringer kan du melde inn på $MELDE_URL. " },
                    nynorsk { + "Alle andre endringar kan meldast inn på $MELDE_URL. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Les mer om dette i vedlegget " },
                    nynorsk { + "Les meir om dette i vedlegget " }
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(bokmal { + "." }, nynorsk { + "." })
            }

            title1 {
                text(
                    bokmal { + "Etteroppgjør " },
                    nynorsk { + "Etteroppgjer " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før. Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør. " },
                    nynorsk { + "Kvart år sjekkar vi inntektsopplysningane i skatteoppgjeret ditt for å sjå om du fekk utbetalt rett beløp frå oss året før. Viser skatteoppgjeret at du har hatt ei anna inntekt enn den inntekta vi brukte då vi rekna ut utbetalingane dine, vil vi gjere ei ny utrekning. Dette vert kalla etteroppgjer. " }
                )
            }

            paragraph {
                text(
                    bokmal { + "Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake. " },
                    nynorsk { + "Dersom du har fått for lite utbetalt, får du ei etterbetaling frå oss. Har du fått for mykje utbetalt, må du betale tilbake. " }
                )
            }

            title1 {
                text(
                    bokmal { + "Inntekter som ikke skal gi lavere utbetaling av uføretrygden " },
                    nynorsk { + "Inntekter som ikkje skal gi lågare utbetaling av uføretrygd " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved: " },
                    nynorsk { + "Dette gjeld dersom du har fått utbetalt erstatning for inntektstap ved: " }
                )
                list {
                    item {
                        text(
                            bokmal { + "Skade " },
                            nynorsk { +  "Skade " }
                        )
                        text(
                            bokmal { +  "(Skadeerstatningsloven § 3-1) " },
                            nynorsk { +  "(Skadeerstatningslova § 3-1) " },
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Yrkesskade " },
                            nynorsk { +  "Yrkesskade " }
                        )
                        text(
                            bokmal { +  "(Yrkesskadeforsikringsloven § 13) " },
                            nynorsk { +  "(Yrkesskadeforsikringslova § 13) " },
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Pasientskade " },
                            nynorsk { +  "Pasientskade " }
                        )
                        text(
                            bokmal { +  "(Pasientskadeloven § 4 første ledd) " },
                            nynorsk { +  "(Pasientskadelova § 4 første ledd) " },
                            FontType.ITALIC
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +  "Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: " },
                    nynorsk { +  "Dette kan gjelde inntekt frå arbeid eller verksemd som var heilt avslutta før du fekk innvilga uføretrygd, til dømes: " }
                )
                list {
                    item {
                        text(
                            bokmal { +  "Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd " },
                            nynorsk { +  "Utbetalte feriepengar. Oppteninga må ha skjedd før du fekk innvilga uføretrygd " }
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten " },
                            nynorsk { +  "Inntekter frå sal av produksjonsmiddel i samband med avvikling av verksemda " }
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Produksjonstillegg og andre overføringer til gårdbrukere " },
                            nynorsk { +  "Produksjonstillegg og andre overføringar til gardbrukarar " }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +  "Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i 2024 eller senere. " },
                    nynorsk { +  "Dette kan også gjelde store etterbetalingar og pengestøtte frå Nav dersom pengestøtta er pensjonsgivande og etterbetalinga blei gjort i 2024 eller seinare.  " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din. " },
                    nynorsk { +  "Dersom du sender oss dokumentasjon som viser at du har ei slik inntekt, kan vi rekne ut uføretrygda di på nytt. " }
                )
            }

            showIf(barnetilleggFellesbarn.notNull()) {
                paragraph {
                    text(
                        bokmal { +  "Hva kan holdes utenfor personinntekten til den andre forelderen? " },
                        nynorsk { +  "Kva kan haldast utanfor personinntekta til den andre forelderen? " }
                    )
                    list {
                        item {
                            text (
                                bokmal { +  "Erstatningsoppgjør for inntektstap dersom den andre forelderen får uføretrygd eller alderspensjon fra Nav " },
                                nynorsk { +  "Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav " }
                            )
                        }
                    }
                    text(
                        bokmal { +  "Dersom vi får dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. " },
                        nynorsk { +  "Dersom vi mottek dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjera ei ny berekning. " }
                    )
                }
            }

            title1 {
                text(
                    bokmal { +  "Du har rett til å klage " },
                    nynorsk { +  "Du har rett til å klage " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. " +
                            "I vedlegget " },
                    nynorsk { +  "Dersom du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. " +
                            "I vedlegget " }
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { +  " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL. " },
                    nynorsk { +  " kan du lese meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL. " }
                )
            }

            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))

            title1 {
                text(
                    bokmal { +  "Sjekk utbetalingene dine " },
                    nynorsk { +  "Sjekk utbetalingane dine " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                            "Du kan se alle utbetalingene du har mottatt på $DITT_NAV. Her kan du også endre kontonummeret ditt. " },
                    nynorsk { +  "Du får utbetalt uføretrygd den 20. kvar månad, eller seinast siste verkedag før denne datoen. " +
                            "Du kan sjå alle utbetalingane du har fått, på $DITT_NAV. Her kan du også endre kontonummeret ditt. " }
                )
            }

            title1 {
                text(
                    bokmal { +  "Skattekort " },
                    nynorsk { +  "Skattekort " }
                )
            }
            paragraph {
                text(
                    bokmal { +  "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. " +
                            "Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt. " },
                    nynorsk { +  "Uføretrygd blir skattlagt som lønsinntekt. Du treng ikkje levere skattekort ditt til oss, då vi får skatteopplysningane dine elektronisk frå Skatteetaten. " +
                            "Du bør likevel sjekke at skattekortet ditt stemmer. Ved behov kan du endre skattekortet på skatteetaten.no. Du kan også få hjelp frå Skatteetaten dersom du har spørsmål om skatt. " }
                )
            }

            showIf(not(brukerBorINorge)) {
                title1 {
                    text(
                        bokmal { +  "Skatt for deg som bor i utlandet " },
                        nynorsk { +  "Skatt for deg som bur i utlandet  " }
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. " +
                                "Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor. " },
                        nynorsk { +  "Bur du i utlandet og betaler kildeskatt, finn du meir informasjon om kildeskatt på skatteetaten.no. " +
                                "Dersom du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur. " }
                    )
                }
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON))
        }


        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
