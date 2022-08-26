package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.Fellesbarn
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.InnvilgetTilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.fellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.fellesbarnSelector
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.oensketVirkningsDato
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDtoSelectors.saerkullsbarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.ektefelle
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.fellesbarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.gjenlevende
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.saerkullsbarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.totaltUfoerePerMnd
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.totaltUfoerePerMndSelector
import no.nav.pensjon.brev.maler.fraser.Beloep
import no.nav.pensjon.brev.maler.fraser.OpphoerBarnetillegg
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.Expression

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
        title {
// Hvis bruker ikke mottar noen barnetillegg:  <BTFBinnvilget> = false AND <BTSBinnvilget> = false
// val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTFBinnvilget

            showIf(not(btFBinnvilget) and not(btSBinnvilget))
            {
                text(
                    Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                    Language.English to "NAV has discontinued the child supplement in your disability benefit"
                )
                // Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
                // val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BarnetilleggSerkull.BTSBinnvilget
            }.orShowIf(btFBinnvilget or btSBinnvilget) {
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
                    perMaaned = totaltUfoerePerMnd,
                    ektefelle = ektefelle.utbetalt_safe.ifNull(false),
                    gjenlevende = gjenlevende.utbetalt_safe.ifNull(false),
                    fellesbarn = fellesbarn.utbetalt_safe.ifNull(false),
                    saerkullsbarn = saerkullsbarn.utbetalt_safe.ifNull(false),
                )
            )
            includePhrase(OpphoerBarnetillegg.TBU2223)
            includePhrase(OpphoerBarnetillegg.TBU1128)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(OpphoerBarnetillegg.TBU3920)

            includePhrase(
                OpphoerBarnetillegg.BTOensketVirkningsDato(
                    oensketVirkningsDato = oensketVirkningsDato
                )
            )

            showIf (barnetilleggInnvilget) {

            }



            includePhrase(OpphoerBarnetillegg.TBU3800)
            includePhrase(OpphoerBarnetillegg.TBU2338)
            includePhrase(OpphoerBarnetillegg.TBU2339)
            includePhrase(OpphoerBarnetillegg.TBU3801)
            includePhrase(
                OpphoerBarnetillegg.BTFribeloep(
                    fellesbarnFribeloep = fellesFribeloep, saerkullsbarnFribeloep = saerkullsbarnFribeloep
                )
            )
            includePhrase(
                Ufoeretrygd.BarnetilleggIkkeUtbetalt(
                    fellesbarn = fellesbarn, saerkullsbarn = saerkullsbarn
                )
            )
            includePhrase(
                OpphoerBarnetillegg.BTInntektstak(
                    fellesbarnInntektstak = fellesbarnInntektstak, saerkullsbarnInntektstak = saerkullsbarnInntektstak
                )
            )
            includePhrase(OpphoerBarnetillegg.TBU1288)


        }
        // TBU2290 > hentes fr en liste, kan være flere barn!


    }
    override val kode: Brevkode.Vedtak
        get() = TODO("Not yet implemented")
}

