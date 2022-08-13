package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevKode: PE_UT_07_200

object OpphoererBarnetilleggAuto : VedtaksbrevTemplate {

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
    )
    {
        val barnetilleggFellesbarnInnvilget = argument().select(OpphoererBarnetilleggAutoDto::barnetilleggFellesbarnInnvilget)
        val barnetilleggSaerkullsbarnInnvilget = argument().select(OpphoererBarnetilleggAutoDto::barnetilleggSaerkullsbarnInnvilget)

        title {

// Hvis bruker ikke mottar noen barnetillegg:  <BTFBinnvilget> = false AND <BTSBinnvilget> = false
// val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTFBinnvilget
            showIf(not(barnetilleggSaerkullsbarnInnvilget) and barnetilleggFellesbarnInnvilget) {
                text(
                    Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                    Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                    Language.English to "NAV has discontinued the child supplement in your disability benefit")
            }
// Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
// val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BarnetilleggSerkull.BTSBinnvilget
            showIf(barnetilleggFellesbarnInnvilget or barnetilleggSaerkullsbarnInnvilget) {
                text(
                    Language.Bokmal to "NAV har endret barnetillegget ditt",
                    Language.Nynorsk to "NAV har endra barnetillegget ditt",
                    Language.English to "NAV has changed the child supplement in your disability benefit")
            }
        }


    }
    override val kode: Brevkode.Vedtak
        get() = TODO("Not yet implemented")
}
