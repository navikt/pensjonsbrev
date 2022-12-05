package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFratrukketBeloepFraAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnMedOpphoertBarnetillegg
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.UfoeretrygdBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.UfoeretrygdFelles
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
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

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.OPPHOER_BARNETILLEGG_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OpphoerBarnetilleggAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
        )
    ) {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        title {
            showIf(not(harBarnetilleggFellesbarn) and not(harBarnetilleggSaerkullsbarn))
            {
                text(
                    Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                    Language.English to "NAV has discontinued the child supplement in your disability benefit"
                )
            }.orShowIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                text(
                    Language.Bokmal to "NAV har endret barnetillegget ditt",
                    Language.Nynorsk to "NAV har endra barnetillegget ditt",
                    Language.English to "NAV has changed the child supplement in your disability benefit"
                )
            }
        }
        outline {
            includePhrase(
                UfoeretrygdBarnetillegg.VirkningsDatoForOpphoer(
                    foedselsdatoPaaBarnetilleggOpphoert = foedselsdatoPaaBarnMedOpphoertBarnetillegg,
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

            includePhrase(UfoeretrygdFelles.UtbetalingsdatoUfoeretrygd)
            includePhrase(UfoeretrygdFelles.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(UfoeretrygdBarnetillegg.BarnHarFylt18AAR)

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
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            includePhrase(
                UfoeretrygdBarnetillegg.BetydningAvInntektOverskrift(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            ifNotNull(
                barnetilleggSaerkullsbarn,
            ) { barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektHarBetydningForSaerkullsbarnTillegg(
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand,
                        faarUtbetaltBarnetilleggSaerkullsbarn =
                        barnetilleggSaerkullsbarn.beloepNetto.greaterThan(0),
                    )
                )
            }

            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesBarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektHarBetydningForFellesbarnTillegg(
                        faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesBarn.beloepNetto.greaterThan(0),
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand
                    )
                )
            }

            includePhrase(
                UfoeretrygdBarnetillegg.BetydningAvInntektEndringer(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    sivilstand = sivilstand
                )
            )

            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektTilAvkortningFellesbarn(
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
                    UfoeretrygdBarnetillegg.InntektTilAvkortningSaerkullsbarn(
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
                    UfoeretrygdBarnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
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

            //TODO
            includePhrase(
                Ufoeretrygd.BarnetilleggIkkeUtbetalt(
                )
            )

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InnvilgetOgIkkeUtbetalt(
                        beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNetto,
                        beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                        inntektstakFellesbarn = barnetilleggFellesbarn.inntektstak,
                        inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstak,
                        harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                        harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                        harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                        harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn
                    )
                )
            }

            includePhrase(
                UfoeretrygdBarnetillegg.HenvisningTilVedleggOpplysningerOmBeregning(
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn
                )
            )
            includePhrase(UfoeretrygdFelles.MeldeFraOmEventuellInntektOverskrift)
            includePhrase(UfoeretrygdFelles.MeldeFraOmEventuellInntekt)
            includePhrase(UfoeretrygdFelles.MeldeFraOmEndringer)
            includePhrase(UfoeretrygdFelles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(UfoeretrygdFelles.Skattekort)
            includePhrase(
                UfoeretrygdFelles.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = brukerBorInorge
                )
            )
        }
        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}



