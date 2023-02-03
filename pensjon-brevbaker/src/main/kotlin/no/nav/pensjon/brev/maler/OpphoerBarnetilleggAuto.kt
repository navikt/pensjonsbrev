package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value_safe
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFratrukketBeloepFraAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnMedOpphoertBarnetillegg
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.createVedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevKode: PE_UT_07_200
@TemplateModelHelpers
object OpphoerBarnetilleggAuto : VedtaksbrevTemplate<OpphoerBarnetilleggAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UT_OPPHOER_BT_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OpphoerBarnetilleggAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
        )
    ) {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        val harBarnetillegg = harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn
        val harOpphoertBarnetilleggForFlereBarn =
            foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)
        title {
            showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                text(
                    Language.Bokmal to "NAV har endret barnetillegget ditt",
                    Language.Nynorsk to "NAV har endra barnetillegget ditt",
                    Language.English to "NAV has changed the child supplement in your disability benefit"
                )
            }.orShow {
                text(
                    Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                    Language.English to "NAV has discontinued the child supplement in your disability benefit"
                )
            }
        }
        outline {
            includePhrase(
                Barnetillegg.VirkningsDatoForOpphoer(
                    foedselsdatoPaaBarnMedOpphoertBarnetillegg = foedselsdatoPaaBarnMedOpphoertBarnetillegg,
                    oensketVirkningsDato = oensketVirkningsDato
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = ufoeretrygd.utbetaltPerMaaned,
                    ufoeretrygd = ufoeretrygd.harUtbetalingsgrad,
                    ektefelle = ufoeretrygd.ektefelletilleggUtbeltalt_safe.notNull(),
                    gjenlevende = ufoeretrygd.gjenlevendetilleggUtbetalt_safe.notNull(),
                    fellesbarn = harBarnetilleggFellesbarn,
                    saerkullsbarn = harBarnetilleggSaerkullsbarn,
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(
                Barnetillegg.BarnHarFylt18AAR(
                    opphoertBarnetilleggFlereBarn = foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)
                )
            )

            includePhrase(
                OpphoerBarnetillegg.HjemmelForBarnetilleggIUfoeretrygden(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)

            includePhrase(
                OpphoerBarnetillegg.OensketVirkningsDatoForEndring(
                    oensketVirkningsDato = oensketVirkningsDato,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harOpphoertBarnetilleggForFlereBarn = harOpphoertBarnetilleggForFlereBarn,
                )
            )

            showIf(harBarnetillegg) {
                title1 {
                    text(
                        Language.Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                        Language.Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                        Language.English to "Income will affect your child supplement"
                    )
                }

                ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.InntektHarBetydningForSaerkullsbarnTillegg(
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            sivilstand = sivilstand,
                            faarUtbetaltBarnetilleggSaerkullsbarn = faarUtbetaltBarnetilleggSaerkullsbarn,
                        )
                    )
                }

                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesBarn ->
                    includePhrase(
                        Barnetillegg.InntektHarBetydningForFellesbarnTillegg(
                            faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesBarn.beloepNetto.greaterThan(0),
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            sivilstand = sivilstand
                        )
                    )
                }


                includePhrase(
                    Barnetillegg.BetydningAvInntektEndringer(
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand
                    )
                )

                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                    includePhrase(
                        Barnetillegg.InntektTilAvkortningFellesbarn(
                            harBeloepFratrukketAnnenForelder = barnetilleggFellesbarn.harFratrukketBeloepFraAnnenForelder,
                            faarUtbetaltBarnetilleggFellesBarn = barnetilleggFellesbarn.beloepNetto.greaterThan(0),
                            harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                            fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                            inntektAnnenForelderFellesbarn = barnetilleggFellesbarn.inntektAnnenForelder,
                            inntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                            harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                            grunnbeloep = grunnbeloep,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            sivilstand = sivilstand,
                        )
                    )
                }

                ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.InntektTilAvkortningSaerkullsbarn(
                            beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                            fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                            inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                            harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                        )
                    )
                }

                ifNotNull(
                    barnetilleggFellesbarn,
                    barnetilleggSaerkullsbarn
                ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
                            beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                            beloepBruttoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepBrutto,
                            harFradragSaerkullsbarn = barnetilleggSaerkullsbarn.harFradrag,
                            fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                            harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                            harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                            harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                            beloepBruttoFellesbarn = barnetilleggFellesbarn.beloepBrutto,
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNetto,
                            fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                            harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                            harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                            inntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                            sivilstand = sivilstand,
                        )
                    )
                }

                includePhrase(
                    Barnetillegg.BarnetilleggIkkeUtbetalt(
                        fellesInnvilget = barnetilleggFellesbarn.notNull(),
                        fellesUtbetalt = barnetilleggFellesbarn.beloepNetto_safe.value_safe.ifNull(0).greaterThan(0),
                        harFlereFellesBarn = barnetilleggFellesbarn.gjelderFlereBarn_safe.ifNull(false),
                        harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn_safe.ifNull(false),
                        inntektstakFellesbarn = barnetilleggFellesbarn.inntektstak_safe.ifNull(Kroner(0)),
                        inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstak_safe.ifNull(Kroner(0)),
                        saerkullInnvilget = barnetilleggSaerkullsbarn.notNull(),
                        saerkullUtbetalt = barnetilleggSaerkullsbarn.beloepNetto_safe.value_safe.ifNull(0)
                            .greaterThan(0),
                    )
                )

                ifNotNull(
                    barnetilleggFellesbarn,
                    barnetilleggSaerkullsbarn
                ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.InnvilgetOgIkkeUtbetalt(
                            fellesInnvilget = barnetilleggFellesbarn.notNull(),
                            fellesUtbetalt = barnetilleggFellesbarn.beloepNetto_safe.value_safe.ifNull(0)
                                .greaterThan(0),
                            harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                            harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                            inntektstakFellesbarn = barnetilleggFellesbarn.inntektstak,
                            inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstak,
                            saerkullInnvilget = barnetilleggSaerkullsbarn.notNull(),
                            saerkullUtbetalt = barnetilleggSaerkullsbarn.beloepNetto_safe.value_safe.ifNull(0)
                                .greaterThan(0),
                        )
                    )
                }

                includePhrase(
                    Barnetillegg.HenvisningTilVedleggOpplysningerOmBeregning(
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn
                    )
                )
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
            includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorInorge))
        }
        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
            ), opplysningerBruktIBeregningUT
        )
    }
}



