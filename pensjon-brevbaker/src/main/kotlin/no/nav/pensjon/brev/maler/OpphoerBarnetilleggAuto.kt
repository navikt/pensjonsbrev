package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.brukerBorMed
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.brukersIntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFratrukketBeloepFraAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.samletInntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.brukerBorMed
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
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
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.createVedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.KronerSelectors.value_safe
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// BrevKode: PE_UT_07_200
@TemplateModelHelpers
object OpphoerBarnetilleggAuto : AutobrevTemplate<OpphoerBarnetilleggAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_OPPHOER_BT_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OpphoerBarnetilleggAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV
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
                    Language.Bokmal to "Nav har endret barnetillegget ditt",
                    Language.Nynorsk to "Nav har endra barnetillegget ditt",
                    Language.English to "Nav has changed the child supplement in your disability benefit"
                )
            }.orShow {
                text(
                    Language.Bokmal to "Nav har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "Nav har stansa barnetillegget ditt",
                    Language.English to "Nav has discontinued the child supplement in your disability benefit"
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
                    fellesbarn = barnetilleggFellesbarn.beloepNetto_safe.ifNull(Kroner(0)).greaterThan(0),
                    saerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto_safe.ifNull(Kroner(0)).greaterThan(0),
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(ufoeretrygd.utbetaltPerMaaned.greaterThan(0)))
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
                            borMedSivilstand = barnetilleggSaerkullsbarn.brukerBorMed,
                            faarUtbetaltBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto.greaterThan(0),
                        )
                    )
                }

                ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesBarn ->
                    includePhrase(
                        Barnetillegg.InntektHarBetydningForFellesbarnTillegg(
                            faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesBarn.beloepNetto.greaterThan(0),
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            borMedSivilstand = barnetilleggFellesBarn.brukerBorMed,
                            barnetilleggSaerkullsbarnGjelderFlereBarn = barnetilleggSaerkullsbarn.gjelderFlereBarn_safe.ifNull(false),
                        )
                    )
                }


                includePhrase(
                    Barnetillegg.BetydningAvInntektEndringer(
                        barnetilleggFellesbarn = barnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
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
                            brukersInntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.brukersIntektBruktIAvkortning,
                            harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                            grunnbeloep = grunnbeloep,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            borMedSivilstand = barnetilleggFellesbarn.brukerBorMed,
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
                            samletInntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.samletInntektBruktIAvkortning,
                            borMed = barnetilleggFellesbarn.brukerBorMed,
                        )
                    )
                }.orIfNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                    includePhrase(
                        Barnetillegg.InntektTilAvkortningSaerkullsbarn(
                            beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                            fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                            inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                            harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
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
            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorInorge))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
            ), opplysningerBruktIBeregningUT,
            harBarnetillegg
        )
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}



