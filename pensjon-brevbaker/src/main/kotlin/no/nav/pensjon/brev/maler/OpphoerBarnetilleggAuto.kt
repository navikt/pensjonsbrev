package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.antallFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepBruttoFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepFratrukketAnnenForeldersInntekt
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNettoFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNettoFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fradragFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloepFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelderFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortningFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstakFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.justeringsbeloepFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.antallSaerkullsbarnbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepBruttoSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNettoSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNettoSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fradragSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloepSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortningSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstakSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.justeringsbeloepSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.foedselsdatoPaaBarnetilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.grunnbeloep
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
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
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
                UfoeretrygdBarnetillegg.VirkningsDatoForOpphoer(
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
                barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn_safe,
            ) { beloepNettoSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektHarBetydningForSaerkullsbarnTillegg(
                        beloepNettoSaerkullsbarn = beloepNettoSaerkullsbarn,
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn.beloepNettoFellesbarn_safe
            ) { beloepNettoFellesbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektHarBetydningForFellesbarnTillegg(
                        beloepNettoFellesbarn = beloepNettoFellesbarn,
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand
                    )
                )
            }

            includePhrase(
                UfoeretrygdBarnetillegg.BetydningAvInntektFellesbarnSaerkullsbarn(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    sivilstand = sivilstand
                )
            )

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
                        barnetilleggFellesbarn.beloepFratrukketAnnenForeldersInntekt,
                        barnetilleggFellesbarn.beloepNettoFellesbarn,
                        barnetilleggFellesbarn.fribeloepFellesbarn,
                        barnetilleggFellesbarn.fradragFellesbarn,
                        barnetilleggFellesbarn.inntektAnnenForelderFellesbarn,
                        barnetilleggFellesbarn.inntektBruktIAvkortningFellesbarn,
                        barnetilleggFellesbarn.justeringsbeloepFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        grunnbeloep,
                        sivilstand = sivilstand,
                    )
                )
            }

            ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InntektTilAvkortningSaerkullsbarn(
                        barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn,
                        barnetilleggSaerkullsbarn.fribeloepSaerkullsbarn,
                        barnetilleggSaerkullsbarn.inntektBruktIAvkortningSaerkullsbarn,
                        barnetilleggSaerkullsbarn.justeringsbeloepSaerkullsbarn
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
                        barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn,
                        barnetilleggSaerkullsbarn.beloepBruttoSaerkullsbarn,
                        barnetilleggSaerkullsbarn.fradragSaerkullsbarn,
                        barnetilleggSaerkullsbarn.fribeloepSaerkullsbarn,
                        barnetilleggSaerkullsbarn.justeringsbeloepSaerkullsbarn,
                        barnetilleggSaerkullsbarn.antallSaerkullsbarnbarnInnvilget,
                        barnetilleggSaerkullsbarn.inntektBruktIAvkortningSaerkullsbarn,
                        barnetilleggFellesbarn.fradragFellesbarn,
                        barnetilleggFellesbarn.beloepBruttoFellesbarn,
                        barnetilleggFellesbarn.beloepNettoFellesbarn,
                        barnetilleggFellesbarn.fribeloepFellesbarn,
                        barnetilleggFellesbarn.justeringsbeloepFellesbarn,
                        barnetilleggFellesbarn.antallFellesbarnInnvilget,
                        barnetilleggFellesbarn.inntektBruktIAvkortningFellesbarn,
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.RedusertBarnetilleggFellesbarn(
                        barnetilleggFellesbarn.beloepNettoFellesbarn,
                        barnetilleggSaerkullsbarn.fradragSaerkullsbarn,
                        barnetilleggFellesbarn.fradragFellesbarn,
                        barnetilleggFellesbarn.fribeloepFellesbarn,
                        barnetilleggFellesbarn.justeringsbeloepFellesbarn,
                        barnetilleggFellesbarn.antallFellesbarnInnvilget,
                        sivilstand = sivilstand
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.RedusertBarnetilleggSearkullsbarnFellesbarn(
                        barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn,
                        barnetilleggFellesbarn.beloepNettoFellesbarn,
                        barnetilleggSaerkullsbarn.fradragSaerkullsbarn,
                        barnetilleggFellesbarn.fradragFellesbarn,
                        barnetilleggSaerkullsbarn.fribeloepSaerkullsbarn,
                        barnetilleggFellesbarn.fribeloepFellesbarn,
                        barnetilleggSaerkullsbarn.justeringsbeloepSaerkullsbarn,
                        barnetilleggFellesbarn.justeringsbeloepFellesbarn,
                        barnetilleggSaerkullsbarn.antallSaerkullsbarnbarnInnvilget,
                        barnetilleggFellesbarn.antallFellesbarnInnvilget,
                        sivilstand = sivilstand
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    UfoeretrygdBarnetillegg.InnvilgetOgIkkeUtbetalt(
                        beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNettoFellesbarn,
                        beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNettoSaerkullsbarn,
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        inntektstakFellesbarn = barnetilleggFellesbarn.inntektstakFellesbarn,
                        inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstakSaerkullsbarn,
                        justeringsbeloepFellesbarn = barnetilleggFellesbarn.justeringsbeloepFellesbarn,
                        justeringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.justeringsbeloepSaerkullsbarn,
                        antallSaerkullsbarnInnvilget = barnetilleggSaerkullsbarn.antallSaerkullsbarnbarnInnvilget,
                        antallFellesbarnInnvilget = barnetilleggFellesbarn.antallFellesbarnInnvilget
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
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}



