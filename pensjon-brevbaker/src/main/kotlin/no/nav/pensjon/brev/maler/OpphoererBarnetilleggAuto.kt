package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.antallFellesbarnInnvilgetSelector
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.antallFellesbarnInnvilget_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepFratrukketAnnenForeldersInntekt_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNettoFellesbarnSelector
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNettoFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloepFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelder_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortningFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNettoSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloepSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortningSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.justeringsbeloepSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnetilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned

import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.UfoeretrygdBarnetillegg
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevKode: PE_UT_07_200
@TemplateModelHelpers
object OpphoererBarnetilleggAuto : VedtaksbrevTemplate<OpphoererBarnetilleggAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.OPPHOER_BARNETILLEGG_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OpphoererBarnetilleggAutoDto::class,
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
                // Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
                // val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BarnetilleggSerkull.BTSBinnvilget
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
                UfoeretrygdBarnetillegg.TBU2290(
                    foedselsdatoPaaBarnetilleggOpphoert = foedselsdatoPaaBarnetilleggOpphoert,
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

            includePhrase(UfoeretrygdBarnetillegg.TBU2223)
            includePhrase(UfoeretrygdBarnetillegg.TBU1128)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(UfoeretrygdBarnetillegg.TBU3920)

            includePhrase(
                OpphoerBarnetillegg.TBU4085(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)

            includePhrase(
                OpphoerBarnetillegg.TBU4086(
                    oensketVirkningsDato = oensketVirkningsDato,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                includePhrase(UfoeretrygdBarnetillegg.TBU3800)
            }


            ifNotNull(
                barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn_safe,
            ) { beloepNettoSaerkullsbarn ->
                showIf(not(harBarnetilleggFellesbarn) and harBarnetilleggSaerkullsbarn) {
                    includePhrase(
                        UfoeretrygdBarnetillegg.TBU2338(
                            beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            sivilstand = sivilstand
                        )
                    )
                }
            }

            ifNotNull(
                barnetilleggFellesbarn.antallFellesbarnInnvilget_safe,
                barnetilleggFellesbarn.beloepNettoFellesbarn_safe
            ) { antallFellesbarnInnvilget, beloepNettoFellesbarn ->
                showIf(harBarnetilleggFellesbarn) {
                    includePhrase(
                        UfoeretrygdBarnetillegg.TBU2339(
                            antallFellesbarnInnvilget = antallFellesbarnInnvilget,
                            beloepNettoFellesbarn = beloepNettoFellesbarn,
                            harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                            harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                            sivilstand = sivilstand
                        )
                    )
                }
            }

            includePhrase(
                UfoeretrygdBarnetillegg.TBU3801(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    sivilstand = sivilstand
                )
            )

            showIf(
                harBarnetilleggFellesbarn
            ) {
                includePhrase(
                    UfoeretrygdBarnetillegg.TBU1284(
                        beloepFratrukketAnnenForeldersInntekt = beloepFratrukketAnnenForeldersInntekt,
                        beloepNettoFellesbarn = beloepNettoFellesbarn,
                        fradragFellesbarn = fradragFellesbarn,
                        inntektAnnenForelderFellesbarn = inntektAnnenForelderFellesbarn,
                        inntektBruktiAvkortningFellesbarn = inntektBruktiAvkortningFellesbarn,
                        grunnbeloepFellesbarn = grunnbeloepFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        justeringsbeloepFellesbarn = justeringsbeloepFellesbarn,
                        sivilstand = sivilstand
                    )
                )
            }
        }
    }
}


/*   ifNotNull(
        barnetilleggFellesbarn.fribeloepFellesbarn_safe,
        barnetilleggFellesbarn.inntektAnnenForelder_safe,
        barnetilleggFellesbarn.inntektBruktIAvkortningFellesbarn_safe,
        barnetilleggFellesbarn.beloepFratrukketAnnenForeldersInntekt_safe,
    ) { fribeloepFellesbarn, inntektAnnenForelder, inntektBruktiAvkortningFellesbarn, beloepFratrukketAnnenForeldersInntekt ->
        showIf(
            harBarnetilleggFellesbarn and not(harBarnetilleggSaerkullsbarn)
        ) {
            includePhrase(
                OpphoerBarnetillegg.TBU1284FB(
                    fribeloepFellesbarn = fribeloepFellesbarn,
                    inntektAnnenForelder = inntektAnnenForelder,
                    inntektBruktiAvkortning = inntektBruktiAvkortningFellesbarn,
                    grunnbeloep = grunnbeloep,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    beloepFratrukketAnnenForeldersInntekt = beloepFratrukketAnnenForeldersInntekt
                )
            )
        }
    }

    ifNotNull(
        barnetilleggSaerkullsbarn.fribeloepSaerkullsbarn_safe,
        barnetilleggSaerkullsbarn.inntektBruktIAvkortningSaerkullsbarn_safe,
        barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn_safe,
        barnetilleggSaerkullsbarn.justeringsbeloepSaerkullsbarn_safe

    ) { fribeloepSaerkullsbarn, inntektBruktIAvkortningSaerkullsbarn, beloepNettoSaerkullsbarn, justeringsbeloepSaerkullsbarn ->
        showIf(
            harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)
        ) {
            includePhrase(
                OpphoerBarnetillegg.TBU1285(
                    fribeloepSaerkullsbarn = fribeloepSaerkullsbarn,
                    inntektBruktiAvkortningSaerkullsbarn = inntektBruktIAvkortningSaerkullsbarn,
                    beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                    justeringsbeloepSaerkullsbarn = justeringsbeloepSaerkullsbarn
                )
            )
        }
    }

    ifNotNull(
        barnetilleggFellesbarn.fribeloep_safe,
        barnetilleggSaerkullsbarn.fribeloep_safe
    ) { fellesbarnFribeloep, saerkullsbarnFribeleop ->
        showIf(
            barnetilleggFellesbarn.fribeloep_safe.equalTo(other = 0)
                and barnetilleggSaerkullsbarn.fribeloep_safe.equalTo(other = 0)
        ) {
            includePhrase(
                OpphoerBarnetillegg.TBU1286(
                    fellesbarnFribeloep = fellesbarnFribeloep,
                    saerkullsbarnFribeloep = saerkullsbarnFribeleop,
                )
            )
        }


            showIf(harBarnetilleggFellesbarn and harBarnetilleggSaerkullsbarn) {
                includePhrase(OpphoerBarnetillegg.TBU2490(
                 beloepNettoFellesbarn =
                 beloepNettoSaerkullsbarn =
                 inntektstakFellesbarn =
            inntektstakSaerkullsbarn =
            justeringsbeloepFellesbarn =
            justeringsbeloepSaerkullsbarn
            ))
            }

        showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
            includePhrase(OpphoerBarnetillegg.TBU1288)
        }

        includePhrase(OpphoerBarnetillegg.TBU2364)
        includePhrase(OpphoerBarnetillegg.TBU2365)
        includePhrase(Felles.MeldEndringerPesys_001)
        includePhrase(Felles.RettTilKlagePesys_001)
        includePhrase(Felles.RettTilInnsynPesys_001) // Endret tekst etter avtale med Ingrid
        includePhrase(Ufoeretrygd.SjekkUtbetalingene)

        includePhrase(OpphoerBarnetillegg.TBU1228)
    }

    showIf(brukerBorInorge) {
        includePhrase(OpphoerBarnetillegg.TBU3730)
    }
}


includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
includeAttachment(vedleggOpplysningerBruktIBeregningUT, opplysningerBruktIBeregningUT)
includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

}
}
*/
