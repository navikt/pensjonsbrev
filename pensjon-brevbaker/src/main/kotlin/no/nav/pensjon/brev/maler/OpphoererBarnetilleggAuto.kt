package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.saerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.FellesbarnSelectors.fribeloep_safe
import no.nav.pensjon.brev.api.model.maler.FellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.barnetillegg
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.SaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.SaerkullsbarnSelectors.fribeloep_safe
import no.nav.pensjon.brev.api.model.maler.SaerkullsbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevKode: PE_UT_07_200
@TemplateModelHelpers
object OpphoererBarnetilleggAuto : VedtaksbrevTemplate<OpphoererBarnetilleggAutoDto> {

    // override val kode = Brevkode.Vedtak.OPPHOER_BARNETILLEGG

    override val template = createTemplate(
        name = kode.name,
        base = PensjonLatex,
        letterDataType = OpphoererBarnetilleggAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
        )
    ) {
        val harBarnetilleggFellesBarn = barnetillegg.fellesbarn_safe.notNull()
        val harBarnetilleggSaerkullsbarn = barnetillegg.saerkullsbarn.notNull()
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
                Ufoeretrygd.Beloep(
                    perMaaned = ufoeretrygd.utbetaltPerMaaned,
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
                OpphoerBarnetillegg.BTOensketVirkningsDato(
                    oensketVirkningsDato = barnetillegg.oensketVirkningsDato
                )
            )

            showIf(harBarnetilleggFellesBarn or harBarnetilleggSaerkullsbarn) {

            }

            includePhrase(OpphoerBarnetillegg.TBU3800)
            includePhrase(OpphoerBarnetillegg.TBU2338)
            includePhrase(OpphoerBarnetillegg.TBU2339)
            includePhrase(OpphoerBarnetillegg.TBU3801)
            includePhrase(
                OpphoerBarnetillegg.BTFribeloep(
                    fellesbarnFribeloep = barnetillegg.fellesbarn_safe.fribeloep_safe,
                    saerkullsbarnFribeloep = barnetillegg.saerkullsbarn_safe.fribeloep_safe
                )
            )
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
                barnetillegg.fellesbarn_safe.inntektstak_safe,
                barnetillegg.saerkullsbarn.inntektstak_safe
            ) { inntektstakFellesbarn, inntektstakSaerkullsbarn ->
                showIf(barnetillegg.fellesbarn_safe.beloepNetto_safe.equalTo(0)
                        and barnetillegg.saerkullsbarn_safe.beloepNetto_safe.equalTo(0)) {
                    includePhrase(
                        OpphoerBarnetillegg.BTInntektstak(
                            fellesbarnInntektstak = inntektstakFellesbarn,
                            saerkullsbarnInntektstak = inntektstakSaerkullsbarn
                        )
                    )
                }
            }

            includePhrase(OpphoerBarnetillegg.TBU1288)
        }
        // TBU2290 > hentes fr en liste, kan være flere barn!


    }
    override val kode: Brevkode.Vedtak
        get() = TODO("Not yet implemented")
}

