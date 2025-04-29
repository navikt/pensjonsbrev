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
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoPerAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoAkkumulert
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.UforetrygdSelectors.nettoRestbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.virkningFom

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
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import java.awt.Font

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntektV2 : AutobrevTemplate<EndretUTPgaInntektDtoV2> {

    // PE_UT_05_100
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
                    Bokmal to "Vi endrer utbetalingen av uføretrygden din",
                    Nynorsk to "",
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    Bokmal to "Vi endrer utbetalingen av uføretrygden og barnetillegget ditt",
                    Nynorsk to "",
                )
            }.orShow {
                text(
                    Bokmal to "Vi endrer utbetalingen av barnetillegget ditt",
                    Nynorsk to "",
                )
            }
        }

        outline {
            paragraph {
                text(
                    Bokmal to "Du får dette automatiske brevet fordi vi har fått opplysninger om endring i inntekt. Vi kan ha fått opplysningene fra deg eller automatisk fra Skatteetaten. ",
                    Nynorsk to "",
                )
            }
            paragraph {
                showIf(endretUt and not(btfbEndret)) {
                    ifNotNull(forventetInntekt) { forventetInntekt ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner. ",
                            Nynorsk to "".expr(),
                        )
                    }
                }.orShowIf(endretUt and btfbEndret) {
                    ifNotNull(forventetInntekt, barnetilleggFellesbarn) { forventetInntekt, barnetilleggFellesbarn ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for deg er ".expr() + forventetInntekt.format(CurrencyFormat) + " kroner " +
                                    "og barnets andre forelder er " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. Inntekten til den andre forelderen påvirker bare størrelsen på barnetillegg. ",
                            Nynorsk to "".expr(),
                        )
                    }
                }.orShowIf(not(endretUt) and btfbEndret) {
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        textExpr(
                            Bokmal to "Ny forventet inntekt for barnets andre forelder er ".expr() + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " 350 000 kroner. Inntekten til den andre forelderen påvirker bare størrelsen på barnetillegg. ",
                            Nynorsk to "".expr(),
                        )
                    }
                }

            }

            paragraph {
                text(
                    Bokmal to "Hvis du mener inntekten vi har brukt i denne beregningen er feil, kan du selv legge inn ny forventet inntekt på nav.no/inntektsplanleggeren eller kontakte oss på 55 55 33 33. ",
                    Nynorsk to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjør du ingen endring, er det beregningen i dette brevet som gjelder. ",
                    Nynorsk to "",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Ny utbetaling gjelder fra ".expr() + virkningFom.format() + ". ",
                    Nynorsk to "".expr()
                )
            }

            paragraph {
                showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull() or gjenlevendetillegg.notNull()) {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalNetto.format(CurrencyFormat) + " kroner i uføretrygd og tillegg per måned før skatt. ",
                        Nynorsk to "".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalNetto.format(CurrencyFormat) + " kroner i uføretrygd per måned før skatt. ",
                        Nynorsk to "".expr()
                    )
                }
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
                                    Bokmal to "Barnetillegg for barn som bor med én forelder ",
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
                                FontType.BOLD
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
            paragraph {
                text(
                    Bokmal to "Vi endrer din utbetaling fordi vi har fått opplysninger om endring i inntekt. ",
                    Nynorsk to "",
                )
            }

            showIf(uforetrygd.endringsbelop.notEqualTo(0)) {
                title2 {
                    text(
                        Bokmal to "Endring i utbetaling av uføretrygd",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Uføretrygden din endres fordi vi har fått opplysninger om endringer i din inntekt. ".expr() +
                                "Den årlige inntekten vi har brukt i beregningen er " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) +
                                " kroner. Det gir deg rett til en årlig utbetaling av uføretrygd på " + uforetrygd.nettoPerAr.format(CurrencyFormat) + ". ",
                        Nynorsk to "".expr()
                    )
                }

                showIf(uforetrygd.nettoRestbelop.equalTo(0)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fordi du allerede har fått utbetalt ".expr() + uforetrygd.nettoAkkumulert.format(CurrencyFormat) +
                                    " i år, vil du ikke få utbetalt uføretrygd resten av året. ",
                            Nynorsk to "".expr()
                        )
                    }
                }

                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn din inntektsgrense på ".expr() + uforetrygd.inntektsgrense.format(CurrencyFormat) +
                                " kroner per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten din som er høyere enn inntektsgrensen som gir lavere utbetaling av uføretrygd. ",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Tjener du mer enn ".expr() + uforetrygd.inntektstak.format(CurrencyFormat) +
                                " kroner per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. ",
                        Nynorsk to "".expr()
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Har du fått utbetalt for mye eller for lite uføretrygd i løpet av året, vil dette komme med i etteroppgjøret neste år. " +
                                "Har du fått utbetalt for mye må du betale tilbake. Har du fått utbetalt for lite, får du en etterbetaling av oss. ",
                        Nynorsk to ""
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
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Vi har fått informasjon om endring i din inntekt, og dette påvirker utbetalingen av ditt gjenlevendetillegg. " +
                                "Vi endrer gjenlevendetillegget ditt med samme prosent som vi endrer uføretrygden din med. ",
                        Nynorsk to "",
                    )
                }
            }

            showIf(barnetilleggSaerkullsbarn.notNull() or barnetilleggFellesbarn.notNull()) {
                title2 {
                    text(
                        Bokmal to "Endring i utbetaling av barnetillegg ",
                        Nynorsk to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. " +
                                "Uføretrygden din regnes med som inntekt. For barn som bor med begge sine foreldre, bruker vi begge foreldrenes inntekt når vi beregner størrelsen på barnetillegget. " +
                                "Inntektene og fribeløpet for beregning av barnetillegg er justert slik at det kun gjelder for den perioden du mottar barnetillegg. ",
                        Nynorsk to "",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Har du fått utbetalt for mye eller for lite barnetillegg i løpet av året, kan dette komme med i etteroppgjøret neste år. " +
                                "Har du fått utbetalt for mye må du betale tilbake. Har du fått utbetalt for lite, får du en etterbetaling av oss. ",
                        Nynorsk to "",

                    )
                }
            }

            showIf(barnetilleggFellesbarn.notNull() and barnetilleggSaerkullsbarn.isNull()) {
                ifNotNull(barnetilleggFellesbarn) { btfb ->
                    title2 {
                        text(
                            Bokmal to "Barnetillegg for barn som bor med begge foreldre ",
                            Nynorsk to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har endret barnetillegget ut fra din personinntekt på ".expr() + btfb.inntektBruker.format(CurrencyFormat) +
                                    " og personinntekten til den andre forelderen på " + btfb.inntektAnnenForelder.format(CurrencyFormat) + " kroner. " +
                                    "Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får derfor en utbetaling av barnetillegg på " + btfb.netto.format(CurrencyFormat) +
                                    " kroner per måned fra neste måned. ",
                            Nynorsk to "".expr()
                        )
                    }
                }
            }.orShowIf(barnetilleggFellesbarn.isNull() and barnetilleggSaerkullsbarn.notNull()) {
                ifNotNull(barnetilleggSaerkullsbarn) { btsb ->
                    title2 {
                        text(
                            Bokmal to "Barnetillegg for barn som bor med én forelder ",
                            Nynorsk to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har endret barnetillegget ut fra din personinntekt på ".expr() + btsb.inntektBruktIAvkortning.format(CurrencyFormat) +
                                    " kroner. Når vi beregner nytt barnetillegg, tar vi hensyn til hvor mye du har fått utbetalt i barnetillegg hittil i år. " +
                                    "Du får derfor en utbetaling av barnetillegg på " + btsb.netto.format(CurrencyFormat) + " kroner per måned fra neste måned. ",
                            Nynorsk to "".expr()
                        )
                    }
                }
            }.orShow {
                ifNotNull(barnetilleggFellesbarn, barnetilleggSaerkullsbarn) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    title2 {
                        text(
                            Bokmal to "Dine barnetillegg ",
                            Nynorsk to "",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Du får barnetillegg for barn som bor med en forelder, og barn som bor med begge foreldrene. ".expr() +
                                    "Barnetillegg for barn som bor med en forelder er beregnet ut fra din inntekt på " + uforetrygd.inntektBruktIAvkortning.format(CurrencyFormat) + " kroner. " +
                                    "Barnetillegg for barn som bor med begge foreldre er i tillegg beregnet ut fra den andre forelderen sin inntekt på " + barnetilleggFellesbarn.inntektAnnenForelder.format(CurrencyFormat) + " kroner. ",
                            Nynorsk to "".expr()
                        )
                    }
                }
            }

            showIf(endretUt and (btfbEndret or btsbEndret) and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12. ",
                        Nynorsk to ""
                    )
                }
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12. ",
                        Nynorsk to ""
                    )
                }
            }.orShowIf(endretUt and gjenlevendetillegg.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12. ",
                        Nynorsk to ""
                    )
                }
            }.orShowIf(btfbEndret or btsbEndret) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 til 12-16, og 22-12. ",
                        Nynorsk to ""
                    )
                }
            }.orShow { // kun endret ut
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12. ",
                        Nynorsk to ""
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer ",
                    Nynorsk to "",
                )
            }

            paragraph {
                showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull() or gjenlevendetillegg.notNull()) {
                    text(
                        Bokmal to "Endringer i inntekt og din situasjon kan påvirke hvor mye uføretrygd og tillegg du får utbetalt. " +
                                "Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling.  ",
                        Nynorsk to ""
                    )
                }.orShow {
                    text(
                        Bokmal to "Endringer i inntekt og din situasjon kan påvirke hvor mye uføretrygd du får utbetalt. " +
                                "Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling.  ",
                        Nynorsk to ""
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Har du ingen endring i inntekten din til neste år, bør du likevel melde inn forventet inntekt. " +
                            "Melder du ikke fra om forventet inntekt til neste år, vil vi bruke inntekten du har oppgitt i år og justere den ved årsskiftet. ",
                    Nynorsk to ""
                )
                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                    text(
                        Bokmal to "Dette gjelder også annen foreldres inntekt fordi du får barnetillegg og bor med barnets andre forelder. ",
                        Nynorsk to ""
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde inn forventet inntekt i Inntektsplanleggeren på nav.no/inntektsplanleggeren. ",
                    Nynorsk to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Alle andre endringer kan du melde inn på nav.no/uforetrygd#melde ",
                    Nynorsk to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Les mer om dette i vedlegget \"Dine rettigheter og plikter\". ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Etteroppgjør ",
                    Nynorsk to "",
                )
            }

            paragraph {
                showIf(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull() or gjenlevendetillegg.notNull()) {
                    text(
                        Bokmal to "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp i uføretrygd og tillegg året før. ",
                        Nynorsk to ""
                    )
                }.orShow {
                    text(
                        Bokmal to "Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp i uføretrygd året før. ",
                        Nynorsk to ""
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, vil vi gjøre en ny beregning. Dette kalles etteroppgjør. ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake. ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Inntekter som ikke skal gi lavere utbetaling av uføretrygden ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved: ",
                    Nynorsk to ""
                )
                // TODO
                list {
                    item {
                        text(
                            Bokmal to "Skade ",
                            Nynorsk to ""
                        )
                        text(
                            Bokmal to "(Skadeerstatningsloven § 3-1)",
                            Nynorsk to "",
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskade ",
                            Nynorsk to ""
                        )
                        text(
                            Bokmal to "(Yrkesskadeforsikringsloven § 13)",
                            Nynorsk to "",
                            FontType.ITALIC
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskade ",
                            Nynorsk to ""
                        )
                        text(
                            Bokmal to "(Pasientskadeloven § 4 første ledd)",
                            Nynorsk to "",
                            FontType.ITALIC
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to ""
                )
                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd.  ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten  ",
                            Nynorsk to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere  ",
                            Nynorsk to ""
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i 2024 eller senere. ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din. ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til å klage ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. " +
                            "I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på nav.no/klage. ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til innsyn ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram. ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                            "Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt. ",
                    Nynorsk to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Skattekort ",
                    Nynorsk to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, vi får skatteopplysningene dine elektronisk fra Skatteetaten. " +
                            "Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt.  ",
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
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. " +
                                "Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor. ",
                        Nynorsk to ""
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
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
