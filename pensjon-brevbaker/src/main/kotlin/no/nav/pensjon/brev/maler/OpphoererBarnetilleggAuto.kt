package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.antallFellesbarnInnvilget_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepFratrukketAnnenForeldersInntekt_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNettoFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fradragFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelderFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortningFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNettoSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnetilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.OpphoererBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.UfoeretrygdBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.UfoeretrygdFelles
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
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

            includePhrase(UfoeretrygdFelles.TBU2223)
            includePhrase(UfoeretrygdFelles.TBU1128)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(UfoeretrygdBarnetillegg.TBU3920)

            includePhrase(
                OpphoererBarnetillegg.TBU4085(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)

            includePhrase(
                OpphoererBarnetillegg.TBU4086(
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

            ifNotNull(
                barnetilleggFellesbarn.beloepFratrukketAnnenForeldersInntekt_safe,
                barnetilleggFellesbarn.beloepNettoFellesbarn_safe,
                barnetilleggFellesbarn.fradragFellesbarn_safe,
                barnetilleggFellesbarn.inntektAnnenForelderFellesbarn_safe,
                barnetilleggFellesbarn.inntektBruktIAvkortningFellesbarn_safe
            ) { beloepFratrukketAnnenForeldersInntekt, beloepNettoFellesbarn, fradragFellesbarn, inntektAnnenForelderFellesbarn, inntektBruktIAvkortningFellesbarn ->

                showIf(harBarnetilleggFellesbarn) {
                    includePhrase(
                        UfoeretrygdBarnetillegg.TBU1284(
                            beloepFratrukketAnnenForeldersInntekt = beloepFratrukketAnnenForeldersInntekt,
                            beloepNettoFellesbarn = beloepNettoFellesbarn,
                            fradragFellesbarn = fradragFellesbarn,
                            inntektAnnenForelderFellesbarn = inntektAnnenForelderFellesbarn,
                            inntektBruktiAvkortningFellesbarn = inntektBruktIAvkortningFellesbarn,
                            grunnbeloepFellesbarn = grunnbeloepFellesbarn,
                            harBarnetilleggSaerkullsbarn,
                            justeringsbeloepFellesbarn = justeringsbeloepFellesbarn,
                            sivilstand = sivilstand
                        )
                    )
                }
            }
            includePhrase(
                UfoeretrygdBarnetillegg.TBU1285(
                    beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                    fribeloepSaerkullsbarn = fribeloepSaerkullsbarn,
                    inntektBruktIAvkortningSaerkullsbarn = inntektBruktIAvkortningSaerkullsbarn,
                    justeringsbeloepSaerkullsbarn = justeringsbeloepSaerkullsbarn
                )
            )
            includePhrase(
                UfoeretrygdBarnetillegg.TBU1286Saerkullsbarn(
                    beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                    fradragSaerkullsbarn = fradragSaerkullsbarn,
                    fradragFellesbarn = fradragFellesbarn,
                    fribeloepSaerkullsbarn = fribeloepSaerkullsbarn,
                    justeringsbeloepSaerkullsbarn = justeringsbeloepSaerkullsbarn,
                    antallSaerkullsbarnInnvilget = antallSaerkullsbarnInnvilget
                )
            )
            includePhrase(
                UfoeretrygdBarnetillegg.TBU1286Fellesbarn(
                    beloepNettoFellesbarn = beloepNettoFellesbarn,
                    fradragSaerkullsbarn = fradragSaerkullsbarn,
                    fradragFellesbarn = fradragFellesbarn,
                    justeringsbeloepFellesbarn = justeringsbeloepFellesbarn,
                    antallFellesbarnInnvilget = antallFellesbarnInnvilget
                )
            )
            includePhrase(
                UfoeretrygdBarnetillegg.TBU1286SearkullsbarnFellesbarn(
                    beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                    beloepNettoFellesbarn = beloepNettoFellesbarn,
                    fradragSaerkullsbarn = fradragSaerkullsbarn,
                    fradragFellesbarn = fradragFellesbarn,
                    fribeloepSaerkullsbarn = fribeloepSaerkullsbarn,
                    fribeloepFellesbarn = fribeloepFellesbarn,
                    justeringsbeloepSaerkullsbarn = justeringsbeloepSaerkullsbarn,
                    justeringsbeloepFellesbarn = justeringsbeloepFellesbarn,
                    antallSaerkullsbarnInnvilget = antallSaerkullsbarnInnvilget,
                    antallFellesbarnInnvilget = antallFellesbarnInnvilget
                )
            )
            includePhrase(
                UfoeretrygdBarnetillegg.TBU2490(
                    beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                    beloepNettoFellesbarn = beloepNettoFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    inntektstakSaerkullsbarn = inntektstakSaerkullsbarn,
                    inntektstakFellesbarn = inntektstakFellesbarn,
                    justeringsbeloepSaerkullsbarn = justeringsbeloepSaerkullsbarn,
                    justeringsbeloepFellesbarn = justeringsbeloepFellesbarn,
                    antallSaerkullsbarnInnvilget = antallSaerkullsbarnInnvilget,
                    antallFellesbarnInnvilget = antallFellesbarnInnvilget
                )
            )
            includePhrase(
                UfoeretrygdBarnetillegg.TBU1288(
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn
                )
            )
            includePhrase(UfoeretrygdFelles.TBU2364)
            includePhrase(UfoeretrygdFelles.TBU2365)
            includePhrase(UfoeretrygdFelles.TBU2212)
            includePhrase(UfoeretrygdFelles.TBU2213)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(UfoeretrygdFelles.TBU1228)
            includePhrase(
                UfoeretrygdFelles.TBU3730(
                    brukerBorInorge = brukerBorInorge
                )
            )

        }
    }
}


