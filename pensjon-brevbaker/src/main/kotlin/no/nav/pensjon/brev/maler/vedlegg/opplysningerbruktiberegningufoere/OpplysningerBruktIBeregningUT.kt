package no.nav.pensjon.brev.maler.vedlegg.opplysningerbruktiberegningufoere


import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.erKonvertert
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


val vedleggOpplysningerBruktIBeregningUT =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val harMinsteytelseSats = minsteytelseGjeldende_sats.ifNull(0.0).greaterThan(0.0)
        val inntektsgrenseErUnderTak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner",
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + " Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner",
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom + " The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + "."
            )
        }

        includePhrase(
            TabellUfoereOpplysninger(
                ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                yrkesskadeGjeldende = yrkesskadeGjeldende,
                inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                inntektEtterUfoereGjeldende_beloepIEU = inntektEtterUfoereGjeldende_beloepIEU,
                ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = barnetilleggGjeldende,
            )
        )
        showIf(harMinsteytelseSats) {
            includePhrase(RettTilMYOverskrift)
        }

        showIf(harMinsteytelseSats) {
            ifNotNull(ungUfoerGjeldende_erUnder20Aar) { erUnder20Aar ->
                showIf(erUnder20Aar) {
                    includePhrase(VedleggBeregnUTInfoMYUngUforUnder20)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMYUngUfor)
                }
            }.orShow {
                showIf(ufoeretrygdGjeldende.erKonvertert) {
                    includePhrase(VedleggBeregnUTInfoMY2)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMY)
                }
            }
        }

        ifNotNull(minsteytelseGjeldende_sats) {
            showIf(harMinsteytelseSats) {
                includePhrase(VedleggBeregnUTDinMY(it))
            }
        }

        showIf(inntektFoerUfoereGjeldende.erSannsynligEndret) {
            includePhrase(VedleggBeregnUTMinsteIFU)
        }

        showIf(
            harMinsteytelseSats
                    and inntektFoerUfoereGjeldende.erSannsynligEndret
                    and inntektsgrenseErUnderTak
        ) {

            includePhrase(SlikFastsettesKompGradOverskrift)
            includePhrase(VedleggBeregnUTKompGrad)

            showIf(ufoeretrygdGjeldende.erKonvertert) {
                includePhrase(VedleggBeregnUTKompGradGjsnttKonvUT)
            }.orShow {
                includePhrase(VedleggBeregnUTKompGradGjsntt)
            }
        }
        // END of minsteytelse

        //TODO -HH OpplysningerBruktIBeregning bør være komplett om denne delen inkluderes
        // START of barnetillegg TODO denne bør eksistere i en frase/fil. Filen er veldig lang.
        ifNotNull(barnetilleggGjeldende) { barnetillegg ->
            val harAnvendtTrygdetidUnder40 = trygdetidsdetaljerGjeldende.anvendtTT.lessThan(40)
            val harTilleggFellesBarn = barnetillegg.notNull()
            val harTilleggSaerkullsbarn = barnetillegg.notNull()
            val barnetilleggFellesbarnErRedusertMotInntekt =
                barnetillegg.fellesbarn_safe.erRedusertMotinntekt_safe.ifNull(false)
            val barnetillegSaerkullsbarnErRedusertMotInntekt =
                barnetillegg.saerkullsbarn_safe.erRedusertMotinntekt_safe.ifNull(false)
            val erRedusertMotInntekt = barnetilleggFellesbarnErRedusertMotInntekt or
                    barnetillegSaerkullsbarnErRedusertMotInntekt
            val harJusteringsbeloepFellesbarn =
                barnetillegg.fellesbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0)).value.greaterThan(0)
            val harJusteringsbeloepSaerkullsbarn =
                barnetillegg.saerkullsbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0)).value.greaterThan(0)
            val harJusteringsbeloep = harJusteringsbeloepFellesbarn or harJusteringsbeloepSaerkullsbarn

            showIf(erRedusertMotInntekt) {
                includePhrase(SlikBeregnBTOverskrift)
                includePhrase(VedleggBeregnUTInnlednBT)
            }

            showIf(harTilleggFellesBarn and not(harTilleggSaerkullsbarn)) {
                includePhrase(FastsetterStoerelsenPaaBTFellesbarn(harAnvendtTrygdetidUnder40))
            }

            showIf(harTilleggSaerkullsbarn and not(harTilleggFellesBarn)) {
                includePhrase(FastsetterStoerelsenPaaBTSaerkullsbarn(harAnvendtTrygdetidUnder40))
            }

            ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { felles, saerkull ->
                includePhrase(
                    FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
                        harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                        harTilleggForFlereFellesbarn = felles.harFlereBarn,
                        harTilleggForFlereSaerkullsbarn = saerkull.harFlereBarn,
                        sivilstand = sivilstand
                    )
                )
            }

            showIf(erRedusertMotInntekt) {
                includePhrase(
                    PeriodisertInntektInnledning(harJusteringsbeloep = harJusteringsbeloep, sivilstand = sivilstand)
                )

                ifNotNull(barnetillegg.fellesbarn_safe){ barnetilleggFellesBarn ->
                    showIf(barnetilleggFellesBarn.erRedusertMotinntekt and not(barnetillegSaerkullsbarnErRedusertMotInntekt)) {
                        includePhrase(
                            PeriodisertInntektFellesbarnA(
                                barnetilleggFellesBarn.avkortningsbeloepAar,
                                barnetilleggFellesBarn.fribeloepEllerInntektErPeriodisert,
                                barnetilleggFellesBarn.justeringsbeloepAar,
                                sivilstand = sivilstand,
                            )
                        )
                    }

                }
                ifNotNull(barnetillegg.saerkullsbarn_safe){ saerkullTillegg ->
                    showIf(barnetillegSaerkullsbarnErRedusertMotInntekt and not(barnetilleggFellesbarnErRedusertMotInntekt)) {
                        includePhrase(
                            PeriodisertInntekSaerkullsbarnA(
                                saerkullTillegg.avkortningsbeloepAar,
                                saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                                saerkullTillegg.justeringsbeloepAar,
                            )
                        )
                    }
                }

                ifNotNull(barnetillegg.fellesbarn_safe){ fellesTillegg ->
                    showIf(fellesTillegg.erRedusertMotinntekt) {
                        includePhrase(
                            PeriodisertInntektFellesbarnB(
                                avkortningsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.avkortningsbeloepAar,
                                fribeloepEllerInntektErPeriodisert_barnetilleggFBGjeldende = fellesTillegg.fribeloepEllerInntektErPeriodisert,
                                harTilleggForFlereFellesbarn = fellesTillegg.harFlereBarn,
                                justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                            )
                        )
                        includePhrase(
                            PeriodisertInntektFellesbarnC(
                                justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                            )
                        )
                    }

                }
                ifNotNull(barnetillegg.saerkullsbarn_safe){ saerkullTillegg ->
                    showIf(barnetillegSaerkullsbarnErRedusertMotInntekt) {
                        includePhrase(
                            PeriodisertInntektSaerkullsbarnB(
                                avkortningsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.avkortningsbeloepAar,
                                fribeloepEllerInntektErPeriodisert_barnetilleggSBGjeldende = saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                                harTilleggForFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                                justeringsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.justeringsbeloepAar,
                                sivilstand = sivilstand,
                                erRedusertMotInntektSaerkullsbarn = barnetillegSaerkullsbarnErRedusertMotInntekt,
                            )
                        )
                        includePhrase(
                            PeriodisertInntektSaerkullsbarnC(
                                justeringsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.justeringsbeloepAar
                            )
                        )
                    }
                }

                ifNotNull(barnetilleggGjeldende.fellesbarn_safe) { fellesTillegg ->

                    showIf(fellesTillegg.justeringsbeloepAar.greaterThan(0)) {
                        includePhrase(VedleggBeregnUTJusterBelopOver0BTFB(fellesTillegg.justeringsbeloepAar))
                    }

                    showIf(fellesTillegg.justeringsbeloepAar.lessThan(0)) {
                        includePhrase(VedleggBeregnUTJusterBelopUnder0BTFB(fellesTillegg.justeringsbeloepAar))
                    }

                    showIf(fellesTillegg.erRedusertMotinntekt) {
                        title1 {
                            text(
                                Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt",
                                Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt",
                                English to "Reduction of child supplement payment for joint children before tax"
                            )
                        }
                        table(
                            header = {
                                column(columnSpan = 2) {
                                    text(
                                        Bokmal to "Beskrivelse",
                                        Nynorsk to "Beskrivelse",
                                        English to "Description",
                                        FontType.BOLD
                                    )
                                }
                                column(alignment = ColumnAlignment.RIGHT) {
                                    text(
                                        Bokmal to "Beløp",
                                        Nynorsk to "Beløp",
                                        English to "Amount",
                                        FontType.BOLD
                                    )
                                }
                            }
                        ) {
                            showIf(fellesTillegg.beloepNetto.greaterThan(0) and
                                    fellesTillegg.justeringsbeloepAar.notEqualTo(0)
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                            Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                            English to "Yearly child supplement before income reduction"
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.beloepAarBrutto))
                                    }
                                }
                            }
                            showIf(fellesTillegg.erRedusertMotinntekt) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                            Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                            English to "Total income applied in calculation of reduction in child supplement is "
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.inntektBruktIAvkortning))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.greaterThan(0) and fellesTillegg.justeringsbeloepAar.notEqualTo(
                                    0
                                )
                            )
                            {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                            Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                            English to "Exemption amount applied in calculation of reduction in child supplement is",
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.fribeloep))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.notEqualTo(0) or (fellesTillegg.beloepNetto.equalTo(0))
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Inntekt over fribeløpet er",
                                            Nynorsk to "Inntekt over fribeløpet er",
                                            English to "Income exceeding the exemption amount is",
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.inntektOverFribeloep))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.notEqualTo(0) and fellesTillegg.justeringsbeloepAar.notEqualTo(
                                    0
                                ) and fellesTillegg.avkortningsbeloepAar.greaterThan(
                                    0
                                )
                            ) {
                                row {
                                    cell {
                                        text( // TODO finn en fornuftig måte å vise regnestykket på
                                            Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløep",
                                            Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                                            English to "- 50 percent of income exceeding the allowance amount"
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.avkortningsbeloepAar))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.justeringsbeloepAar.notEqualTo(0)
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                            Nynorsk to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                            English to "+ Amount which is used to adjust the reduction of child supplement"
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.justeringsbeloepAar))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.notEqualTo(0) or (fellesTillegg.beloepNetto.equalTo(0) and fellesTillegg.beloepAarNetto.notEqualTo(
                                    0
                                ))
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                            Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                            English to "= Yearly child supplement after income reduction",
                                            FontType.BOLD,
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.beloepAarNetto))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.greaterThan(0) or fellesTillegg.beloepNetto.equalTo(0)
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Utbetaling av barnetillegg per måned",
                                            Nynorsk to "Utbetaling av barnetillegg per månad",
                                            English to "Child supplement payment for the remaining months of the year"
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.beloepNetto))
                                    }
                                }
                            }
                            showIf(
                                fellesTillegg.beloepNetto.equalTo(0) and fellesTillegg.justeringsbeloepAar.equalTo(0)
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Grensen for å få utbetalt barnetillegg",
                                            Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                            English to "The income limit for receiving child supplement"
                                        )
                                    }
                                    cell {
                                        includePhrase(KronerText(fellesTillegg.inntektstak))
                                    }
                                }
                            }
                        }
                    }  // TABLE 2 Felles barn - end

                    showIf(harTilleggFellesBarn) {
                        showIf(fellesTillegg.beloepNetto.greaterThan(0)) {
                            includePhrase(
                                MaanedligTilleggFellesbarn(
                                    beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                                    harFlereBarn = fellesTillegg.harFlereBarn,
                                )
                            )
                        }.orShow {
                            includePhrase(
                                FaaIkkeUtbetaltTilleggFellesbarn(
                                    beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                                    justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar,
                                    harFlereBarn = fellesTillegg.harFlereBarn,
                                )
                            )
                        }
                    }
                }

// TABLE 2 Saerkullsbarn - start
                showIf(barnetillegSaerkullsbarnErRedusertMotInntekt) {
                    title1 {
                        text(
                            Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                            Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                            English to "Reduction of child supplement payment for children from a previous relationship before tax"
                        )
                    }

                    table(
                        header = {
                            column(2) {
                                text(
                                    Bokmal to "Beskrivelse",
                                    Nynorsk to "Beskrivelse",
                                    English to "Description",
                                    FontType.BOLD
                                )
                            }
                            column(alignment = ColumnAlignment.RIGHT) {
                                text(
                                    Bokmal to "Beløp",
                                    Nynorsk to "Beløp",
                                    English to "Amount",
                                    FontType.BOLD,
                                )
                            }
                        }
                    ) {
                        showIf(
                            saerkullTillegg.beloepNetto.greaterThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                                0
                            )
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                        Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                        English to "Yearly child supplement before income reduction"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.beloepAarBrutto))
                                }
                            }
                        }
                        showIf(barnetillegSaerkullsbarnErRedusertMotInntekt) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                        Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                        English to "Total income applied in calculation of reduction in child supplement is ",
                                    )
                                }

                                cell {
                                    includePhrase(KronerText(saerkullTillegg.inntektBruktIAvkortning))
                                }
                            }
                        }
                        showIf(
                            saerkullTillegg.beloepNetto.greaterThan(0) or (saerkullTillegg.beloepNetto.lessThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                                0
                            ))
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                        Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                        English to "Exemption amount applied in calculation of reduction in child supplement is",
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.fribeloep))
                                }
                            }
                        }
                        showIf(
                            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                                0
                            ))
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Inntekt over fribeløpet er",
                                        Nynorsk to "Inntekt over fribeløpet er",
                                        English to "Income exceeding the exemption amount is",
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.inntektOverFribeloep))
                                }
                            }
                        }
                        showIf(
                            not(saerkullTillegg.fribeloepEllerInntektErPeriodisert)
                                    and (saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(
                                0
                            ) and saerkullTillegg.beloepAarNetto.notEqualTo(
                                0
                            )))
                                    and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
                        ) {
                            row {
                                cell {
                                    text( // TODO finn en fornuftig måte å vise regnestykket på
                                        Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                                        Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                                        English to "- 50 percent of income exceeding the allowance amount"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.avkortningsbeloepAar))
                                }
                            }
                        }
                        showIf(
                            saerkullTillegg.fribeloepEllerInntektErPeriodisert
                                    and (saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(
                                0
                            ) and saerkullTillegg.beloepAarNetto.notEqualTo(
                                0
                            )))
                                    and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                                        Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                                        English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.avkortningsbeloepAar))
                                }
                            }
                        }
                        showIf(saerkullTillegg.justeringsbeloepAar.notEqualTo(0)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                        Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                                        English to "+ Amount which is used to adjust the reduction of child supplement"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.justeringsbeloepAar))
                                }
                            }
                        }
                        showIf(
                            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.beloepAarNetto.notEqualTo(
                                0
                            ))
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                        Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                        English to "= Yearly child supplement after income reduction",
                                        FontType.BOLD,
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.beloepAarNetto))
                                }
                            }
                        }
                        showIf(
                            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                                0
                            ))
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Utbetaling av barnetillegg per måned",
                                        Nynorsk to "Utbetaling av barnetillegg per månad",
                                        English to "Child supplement payment for the remaining months of the year"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.beloepNetto))
                                }
                            }
                        }
                        showIf(saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.beloepAarNetto.equalTo(0)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Grensen for å få utbetalt barnetillegg",
                                        Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                        English to "The income limit for receiving child supplement"
                                    )
                                }
                                cell {
                                    includePhrase(KronerText(saerkullTillegg.inntektstak))
                                }
                            }
                        }
                    }
                } // TABLE 2 - end


                showIf(saerkullTillegg.beloepNetto.greaterThan(0)) {
                    includePhrase(VedleggBeregnUTredusBTSBPgaInntekt(saerkullTillegg.beloepNetto))
                }.orShowIf(saerkullTillegg.beloepNetto.equalTo(0)) {
                    showIf(saerkullTillegg.justeringsbeloepAar.equalTo(0)) {
                        includePhrase(VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt)
                    } orShow {
                        includePhrase(VedleggBeregnUTJusterBelopIkkeUtbetalt)
                    }
                }
            }
        }
    }

