package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.map
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevKode: PE_UT_07_200

object OpphoerAvBarnetillegget : VedtaksbrevTemplate {

    override val kode = Brevkode.Vedtak.OPPHOER_BARNETILLEGGET

    override val template = createTemplate(
        name = kode.name,
        base = PensjonLatex,
        letterDataType = OpphoerAvBarnetilleggetDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – opphør av barnetillegget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        val harMinsteytelseVedVirk = argument().select(UfoerOmregningEnslig::harMinstytelseVedVirk)
        val inntektFoerUfoereErSannsynligEndret = argument().map { it.inntektFoerUfoerhetVedVirk.erSannsynligEndret }
// Hvis bruker ikke mottar noen barnetillegg:  <BTFBinnvilget> = false AND <BTSBinnvilget> = false
        title {
            text(
                Language.Bokmal to "NAV har vedtatt at barnetillegget ditt opphører",
                Language.Nynorsk to "NAV har stansa barnetillegget ditt",
                Language.English to "NAV has discontinued the child supplement in your disability benefit",
            )
        }
// Hvis barnetillegget fortsatt løper for andre barn/annet barn: <BTinnvilget> = true OR <BTSBinnvilget> = true
        title {
            text(
                Language.Bokmal to "NAV har endret barnetillegget ditt",
                Language.Nynorsk to "NAV har endra barnetillegget ditt",
                Language.English to "NAV has changed the child supplement in your disability benefit"
            )
        }