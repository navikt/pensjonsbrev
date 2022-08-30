package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.FellesSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.OpptjeningVedForhoeyetHjelpesatsDtoSelectors.aarInnvilgetOmrsorgspoeng
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

// BrevTypeKode: MF_000094
@TemplateModelHelpers
object OpptjeningVedForhoeyetHjelpesats : VedtaksbrevTemplate<OpptjeningVedForhoeyetHjelpesatsDto> {

    override val kode = Brevkode.Vedtak.OMSORGP_GODSKRIVING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OpptjeningVedForhoeyetHjelpesatsDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        title {
            textExpr(
                Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for ".expr() + aarInnvilgetOmrsorgspoeng.format(),
                Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for ".expr() + aarInnvilgetOmrsorgspoeng.format(),
                English to "Earned pension savings for unpaid care work for ".expr() + aarInnvilgetOmrsorgspoeng.format(),
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            includePhrase(Omsorgsopptjening.HjelpestoenadInnledn(aarInnvilgetOmrsorgspoeng))

            val foedtEtter1953 = felles.bruker.foedselsdato.year.greaterThan(1953)
            showIf(foedtEtter1953) {
                includePhrase(Omsorgsopptjening.HjelpestKap20Hjemmel)
            } orShow {
                includePhrase(Omsorgsopptjening.HjelpestKap3Hjemmel)
            }

            includePhrase(Omsorgsopptjening.Info)

            includePhrase(Omsorgsopptjening.OverforingInfo)

            includePhrase(Omsorgsopptjening.HjelpestonadAutoGodkjennInfo)
        }

    }
}
