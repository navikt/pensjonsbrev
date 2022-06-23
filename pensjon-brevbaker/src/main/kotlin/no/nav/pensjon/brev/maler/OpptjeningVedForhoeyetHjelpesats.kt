package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.OmsorggsopptjeningVedForhoeyetHjelpesats
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.*
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

        outline {
            includePhrase(vedtakOverskriftPesys_001)

            includePhrase(omsorgsopptjenHjelpestoenadInnledn_001)

            // fodselsdato format:  1953-12-12
            // var date= GetValue("fag=bruker=fodselsdato");
            // var aar = date.substr(0,4); -substring function that exstracts the first 4 digits
            // if(aar > 1953) include
            /*showIf(
                includePhrase(omsorgsopptjenHjelpestKap20Hjemmel_001)
            )

            if(aar < 1954) include
            showIf(
                includePhrase(omsorgsopptjenHjelpestKap3Hjemmel_001)
            )
            */
            includePhrase(omsorgsopptjenInfoOverskrift_001)

            includePhrase(omsorgsopptjenInfo_001)

            includePhrase(omsorgsopptjenOverforingInfoOverskrift_001)

            includePhrase(omsorgsopptjenOverforingInfo_001)

            includePhrase(omsorgsopptjenHjelpestonadAutoGodkjennInfoTittel_001)

            includePhrase(omsorgsopptjenHjelpestonadAutoGodkjennInfo_001)
            

        }

    }
    //harSpørsmålPesys_001
    //mvhInfoAutoPesys_001
}
