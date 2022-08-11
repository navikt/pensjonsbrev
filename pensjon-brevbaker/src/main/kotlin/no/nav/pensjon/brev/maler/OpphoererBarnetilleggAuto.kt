package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import kotlin.reflect.KProperty1

// BrevKode: PE_UT_07_200

object OpphoererBarnetilleggAuto : VedtaksbrevTemplate {

  // override val kode = Brevkode.Vedtak.OPPHOER_BARNETILLEGGET

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

// Hvis bruker ikke mottar noen barnetillegg:  <BTFBinnvilget> = false AND <BTSBinnvilget> = false

        title {
            // val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTFBinnvilget
            val barnetilleggFellesbarnInnvilget = argument().select(OpphoererBarnetilleggAutoDto::barnetilleggFellesbarnInnvilget)
            val barnetilleggSaerkullsbarnInnvilget = argument().select(OpphoererBarnetilleggAutoDto::barnetilleggSaerkullsbarnInnvilget)
            showIf(
                barnetilleggFellesbarnInnvilget
            )
            text(
                Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                Language.English to "NAV has discontinued the child supplement in your disability benefit",
            )
        }


// Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
// val = Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BarnetilleggSerkull.BTSBinnvilget
        title {
            text(
                Language.Bokmal to "NAV har endret barnetillegget ditt",
                Language.Nynorsk to "NAV har endra barnetillegget ditt",
                Language.English to "NAV has changed the child supplement in your disability benefit"
            )
        }
