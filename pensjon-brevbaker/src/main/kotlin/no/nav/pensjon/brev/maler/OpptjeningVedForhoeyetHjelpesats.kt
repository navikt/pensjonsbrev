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
object OpptjeningVedForhoeyetHjelpesats : StaticTemplate {
    override val template = createTemplate(
        name = "MF_000094",
        base = PensjonLatex,
        letterDataType = OpptjeningVedForhoeyetHjelpesatsDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            "Vedtak – innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4", isSensitiv = false
        )
    ) {
        title {
            text(
                Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
                Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
                English to "Earned pension savings for unpaid care work for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>"
            )
        }

        Outline {

        }

    }

    omsorgsopptjenHjelpestInnledn_001
    omsorgsopptjenHjelpestKap20Hjemmel_001
    omsorgsopptjenHjelpestKap3Hjemmel_001
    title1 omsorgsopptjenInfoOverskrift_001
    omsorgsopptjenInfo_001
    title1 omsorgsopptjenOverforingInfoOverskrift_001
    omsorgsopptjenOverforingInfo_001
    title1 omsorgsopptjenHjelpestAutoInfoOverskrift_001
    omsorgsopptjenHjelpestAutoInfo_001
    harSpørsmålPesys_001
    mvhInfoAutoPesys_001
}
