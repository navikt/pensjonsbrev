package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloep_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloep_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnetilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
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
        val harBarnetilleggFellesBarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        title {
// Hvis bruker ikke mottar noen barnetillegg:  <BTFBinnvilget> = false AND <BTSBinnvilget> = false
// val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTFBinnvilget

            showIf(not(harBarnetilleggFellesBarn) and not(harBarnetilleggSaerkullsbarn))
            {
                text(
                    Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                    Language.English to "NAV has discontinued the child supplement in your disability benefit"
                )
                // Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
                // val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BarnetilleggSerkull.BTSBinnvilget
            }.orShowIf(harBarnetilleggFellesBarn or harBarnetilleggSaerkullsbarn) {
                text(
                    Language.Bokmal to "NAV har endret barnetillegget ditt",
                    Language.Nynorsk to "NAV har endra barnetillegget ditt",
                    Language.English to "NAV has changed the child supplement in your disability benefit"
                )
            }
        }
        outline {
            includePhrase(
                OpphoerBarnetillegg.TBU2290(
                    foedselsdatoPaaBarnetilleggOpphoert = foedselsdatoPaaBarnetilleggOpphoert,
                    oensketVirkningsDato = oensketVirkningsDato
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = ufoeretrygd.notNull(),
                    ufoeretrygd = ufoeretrygd.notNull(),
                    ektefelle = ufoeretrygd.ektefelletilleggUtbeltalt_safe.notNull(),
                    gjenlevende = ufoeretrygd.gjenlevendetilleggUtbetalt_safe.notNull(),
                    fellesbarn = harBarnetilleggFellesBarn,
                    saerkullsbarn = harBarnetilleggSaerkullsbarn,
                )
            )
            includePhrase(OpphoerBarnetillegg.TBU2223)
            includePhrase(OpphoerBarnetillegg.TBU1128)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(OpphoerBarnetillegg.TBU3920)

            includePhrase(
                OpphoerBarnetillegg.TBU4085(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesBarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )


            includePhrase(Ufoeretrygd.VirkningFomOverskrift)
            includePhrase(
                OpphoerBarnetillegg.TBU4086(
                    oensketVirkningsDato = oensketVirkningsDato,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesBarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn
                )
            )

            showIf(harBarnetilleggFellesBarn or harBarnetilleggSaerkullsbarn) {
                includePhrase(OpphoerBarnetillegg.TBU3800)
                includePhrase(OpphoerBarnetillegg.TBU2338)
                includePhrase(OpphoerBarnetillegg.TBU2339)
                includePhrase(OpphoerBarnetillegg.TBU3801)

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

//            includePhrase( TODO ask alexander about making this phrase re-use friendly
//                Ufoeretrygd.BarnetilleggIkkeUtbetalt(
//                    fellesbarn = UngUfoerAutoDto.InnvilgetBarnetillegg(
//                        utbetalt = barnetillegg.fellesbarn_safe.beloepNetto_safe,
//
//                    ),
//                    saerkullsbarn = barnetillegg.saerkullsbarn
//                )
//            )

                    ifNotNull(
                        barnetilleggFellesbarn.inntektstak_safe,
                        barnetilleggSaerkullsbarn.inntektstak_safe,
                    ) { inntektstakFellesbarn, inntektstakSaerkullsbarn ->
                        showIf(
                            barnetilleggFellesbarn.beloepNetto_safe.equalTo(0)
                                and barnetilleggSaerkullsbarn.beloepNetto_safe.equalTo(0)
                        ) {
                            includePhrase(
                                OpphoerBarnetillegg.TBU2490(
                                    fellesbarnInntektstak = inntektstakFellesbarn,
                                    saerkullsbarnInntektstak = inntektstakSaerkullsbarn,
                                )
                            )
                        }
                    }

                    showIf(harBarnetilleggFellesBarn or harBarnetilleggSaerkullsbarn) {
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
        }


        // TBU2290 > hentes fr en liste, kan være flere barn!


    }
}


