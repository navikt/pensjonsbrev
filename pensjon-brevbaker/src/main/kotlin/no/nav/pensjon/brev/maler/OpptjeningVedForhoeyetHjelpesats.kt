package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.OmsorggsopptjeningVedForhoeyetHjelpesats
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.omsorgsopptjeningVedForhoeyetHjelpesats
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

// BrevTypeKode: MF_000094
object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "MF_000094",
        base = PensjonLatex,
        letterDataType = OpptjeningVedForhoeyetHjelpesatsDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            "Vedtak – omregning til enslig uføretrygdet (automatisk)", isSensitiv = true
        )
    ) {
        val harMinsteytelseVedVirk = argument().map { it.minsteytelseVedvirk_sats != null }
        val inntektFoerUfoereErSannsynligEndret = argument().map { it.inntektFoerUfoerhetVedVirk.erSannsynligEndret }

        title {
            text(
                Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
                Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
                English to "Earned pension savings for unpaid care work for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>"
            )
        }

        outline {