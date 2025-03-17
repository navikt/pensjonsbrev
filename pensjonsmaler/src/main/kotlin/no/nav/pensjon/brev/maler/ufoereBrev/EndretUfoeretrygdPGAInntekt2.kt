package no.nav.pensjon.brev.maler.ufoereBrev


import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggFellesbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggSaerkullsbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.virkningFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.GjenlevendetilleggSelectors.belop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.endringsbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.netto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntekt2 : AutobrevTemplate<EndretUfoeretrygdPGAInntektDto2> {

    // PE_UT_05_100
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_2

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUfoeretrygdPGAInntektDto2::class,
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
                    Bokmal to "Nav har endret utbetalingen av uføretrygden din",
                    Nynorsk to "Nav har endra utbetalinga av uføretrygda di",
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                    Nynorsk to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                )
            }.orShow {
                text(
                    Bokmal to "Nav har endret utbetalingen av barnetillegget i uføretrygden din",
                    Nynorsk to "Nav har endret utbetalingen av barnetillegget i uføretrygda di",
                )
            }
        }

        // Vi bruker dette som inntektsplanlegger-brevet
        // TODO: om inntekt er for høy, avsnitt om det
        outline {
            paragraph {
                text(
                    Bokmal to "Du får dette automatiske brevet fordi vi har fått opplysninger om endring i inntekt. Vi kan ha fått opplysningene fra deg eller automatisk fra Skatteetaten. ",
                    Nynorsk to "",
                )
            }
            paragraph {
                text(
                    Bokmal to ("Hvis du mener inntekten vi har brukt i denne beregningen er feil, kan du selv legge inn ny forventet inntekt på nav.no/inntektsplanleggeren eller kontakte oss på 55 55 33 33. "),
                    Nynorsk to "",
                )
            }
            paragraph {
                text(
                    Bokmal to ("Gjør du ingen endring, er det beregningen i dette brevet som gjelder. "),
                    Nynorsk to "",
                )
            }

            paragraph {
                showIf(btfbEndret) {
                    textExpr(
                        Bokmal to "Vi har fått nye opplysninger om inntekten til deg eller din EPS. Inntekten til din EPS har kun betydning for størrelsen på barnetillegget ditt. ".expr(), //TODO Fikse EPS
                        Nynorsk to "".expr()
                    )
                }.orShow {
                    text(
                        Bokmal to "Vi har fått nye opplysninger om inntekten din som gjør at din utbetaling endres. ",
                        Nynorsk to ""
                    )
                }
            }

            paragraph {
                textExpr(
                    Bokmal to "Ny utbetaling gjelder fra. ".expr() + virkningFom.format() + "",
                    Nynorsk to "".expr()
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + totalNetto.format(CurrencyFormat) + " kroner i ",
                    Nynorsk to "".expr()
                )
                showIf(uforetrygd.netto.notEqualTo(0)) {
                    text(Bokmal to "uføretrygd ", Nynorsk to "")
                }
                showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull() or gjenlevendetillegg.notNull()) {
                    showIf(uforetrygd.netto.notEqualTo(0)) { text(Bokmal to "og ", Nynorsk to "") }
                    text(Bokmal to "tilleggsytelser ", Nynorsk to "")
                }

                text(
                    Bokmal to "per måned før skatt. ",
                    Nynorsk to "",
                )
            }

            paragraph {
                text(
                    Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                    Nynorsk to "",
                )
            }

            showIf(datoForNormertPensjonsalder.year.equalTo(virkningFom.year)) {
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du får alderspensjon fra ".expr() + datoForNormertPensjonsalder.format() + ", er inntekten justert i forhold til antall måneder du får uføretrygd. ",
                        Nynorsk to "".expr()
                    )
                }
            }

            title1 {
                textExpr(
                    Bokmal to "Din månedlige utbetaling fra ".expr() + virkningFom.format(),
                    Nynorsk to "".expr(),
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
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Uføretrygd",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to uforetrygd.netto.format(CurrencyFormat) + " kr",
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for barn som bor med en forelder ",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggSaerkullsbarn.netto.format(CurrencyFormat) + " kr",
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg for barn som bor med begge foreldrene ",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggFellesbarn.netto.format(CurrencyFormat) + " kr",
                                    Nynorsk to "".expr(),
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
                                    Bokmal to gjenlevendetillegg.belop.format(CurrencyFormat) + " kr",
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Sum før skatt",
                                Nynorsk to "",
                                Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to totalNetto.format(CurrencyFormat) + " kr",
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                }
            }

            title1 {
                text(
                    Bokmal to "Begrunnelse for vedtaket ",
                    Nynorsk to "",
                )
            }
            showIf(totalNetto.greaterThan(0)) {
                title2 {
                    text(
                        Bokmal to "Endring i utbetaling av uføretrygd",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Uføretrygden din endres fordi vi har fått opplysninger om endringer i din inntekt. ".expr() +
                                "Den årlige inntekten vi har brukt i beregningen er " + uforetrygd.inntektBruktIAvkortning.format(
                            CurrencyFormat
                        ) +
                                " kroner, det gir deg rett til en årlig utbetaling av uføretrygd " + uforetrygd.netto.format(
                            CurrencyFormat
                        ) + ". ",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn din inntektsgrense på ".expr() + uforetrygd.inntektsgrense.format(
                            CurrencyFormat
                        ) +
                                " kroner per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten " +
                                "din som er høyere enn inntektsgrensen som gir lavere utbetaling av uføretrygd. ",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn".expr() + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten " +
                                "utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. ",
                        Nynorsk to "".expr()
                    )
                }
            }.orShow {
                title2 {
                    text(
                        Bokmal to "Du har tjent mer enn din inntektsgrense ",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du får ikke utbetalt uføretrygd fordi inntekten din er høyere enn".expr() + uforetrygd.inntektstak.format(
                            CurrencyFormat
                        ) +
                                " kroner, som er 80 prosent av den oppjusterte inntekten du hadde før du ble ufør. Inntekten vi har brukt i " +
                                "beregningen er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + "kroner, du vil derfor ikke få utbetalt uføretrygd resten av året.",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Har du fått utbetalt for mye eller for lite uføretrygd i løpet av året, vil dette komme med i etteroppgjøret neste år. ".expr() +
                                "Har du fått utbetalt for mye må du betale tilbake. Har du fått utbetalt for lite, får du en etterbetaling av oss.",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn ".expr() + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd " +
                                "uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden.",
                        Nynorsk to "".expr()
                    )
                }
            }

            ifNotNull(gjenlevendetillegg) {
                title2 {
                    text(
                        Bokmal to "Ditt gjenlevendetillegg ",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Vi har fått informasjon om endring i din inntekt, " +
                                "og dette påvirker utbetalingen av ditt gjenlevendetillegg. Vi endrer gjenlevendetillegget ditt med samme prosent som vi endrer uføretrygden din med.  ",
                        Nynorsk to "",
                    )
                }
            }

            showIf(barnetilleggSaerkullsbarn.notNull() or barnetilleggFellesbarn.notNull()) {
                title2 {
                    text(
                        Bokmal to "Ditt barnetillegg ",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din har også betydning for hvor mye du får utbetalt i barnetillegg. " +
                                "Uføretrygden din regnes med som inntekt. For barn som bor med begge sine foreldre, bruker " +
                                "vi i tillegg den andre forelders inntekt når vi beregner størrelsen på barnetillegget.",
                        Nynorsk to "",
                    )
                }
            }
        }

            /* outline {
                 includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
                 includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
                 includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
                 includePhrase(Ufoeretrygd.SjekkUtbetalingene)†
                 includePhrase(Ufoeretrygd.Skattekort)
                 includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorINorge))
                 includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
             }

             includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
             includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
             includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
        */
        }
    }
