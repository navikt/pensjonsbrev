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
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
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
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
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

                ifNotNull(barnetillegg.fellesbarn_safe) { barnetilleggFellesBarn ->
                    showIf(
                        barnetilleggFellesBarn.erRedusertMotinntekt and not(
                            barnetillegSaerkullsbarnErRedusertMotInntekt
                        )
                    ) {
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
                ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
                    showIf(
                        barnetillegSaerkullsbarnErRedusertMotInntekt and not(
                            barnetilleggFellesbarnErRedusertMotInntekt
                        )
                    ) {
                        includePhrase(
                            PeriodisertInntekSaerkullsbarnA(
                                saerkullTillegg.avkortningsbeloepAar,
                                saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                                saerkullTillegg.justeringsbeloepAar,
                            )
                        )
                    }
                }

                ifNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->
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
                ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
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
                        includePhrase(
                            OpplysningerOmBarnetilleggTabell(
                                fellesTillegg.avkortningsbeloepAar,
                                fellesTillegg.beloepAarBrutto,
                                fellesTillegg.beloepAarNetto,
                                fellesTillegg.erRedusertMotinntekt,
                                fellesTillegg.fribeloep,
                                fellesTillegg.inntektBruktIAvkortning,
                                fellesTillegg.inntektOverFribeloep,
                                fellesTillegg.inntektstak,
                                fellesTillegg.justeringsbeloepAar,
                                fellesTillegg.beloepNetto,
                            )
                        )
                    }

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

                ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
                    showIf(saerkullTillegg.erRedusertMotinntekt) {
                        includePhrase(
                            OpplysningerOmBarnetilleggTabell(
                                saerkullTillegg.avkortningsbeloepAar,
                                saerkullTillegg.beloepAarBrutto,
                                saerkullTillegg.beloepAarNetto,
                                saerkullTillegg.erRedusertMotinntekt,
                                saerkullTillegg.fribeloep,
                                saerkullTillegg.inntektBruktIAvkortning,
                                saerkullTillegg.inntektOverFribeloep,
                                saerkullTillegg.inntektstak,
                                saerkullTillegg.justeringsbeloepAar,
                                saerkullTillegg.beloepNetto,
                            )
                        )
                    }
                    showIf(saerkullTillegg.beloepNetto.greaterThan(0)) {
                        includePhrase(VedleggBeregnUTredusBTSBPgaInntekt(saerkullTillegg.beloepNetto))
                    }.orShow {
                        showIf(saerkullTillegg.justeringsbeloepAar.equalTo(0)) {
                            includePhrase(VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt)
                        } orShow {
                            includePhrase(VedleggBeregnUTJusterBelopIkkeUtbetalt)
                        }
                    }
                }
            }
        }
    }

