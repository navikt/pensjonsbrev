package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.OpptjeningVedForhoeyetHjelpesatsDtoSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OpptjeningVedForhoeyetHjelpesatsDtoSelectors.foedtEtter1953
import no.nav.pensjon.brev.maler.fraser.Omsorgsopptjening
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// BrevTypeKode: MF_000094
@TemplateModelHelpers
object OpptjeningVedForhoeyetHjelpesats : AutobrevTemplate<OpptjeningVedForhoeyetHjelpesatsDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_OMSORG_HJELPESTOENAD_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Du får pensjonsopptjening for omsorgsarbeid for " + aarInnvilgetOmsorgspoeng.format() },
                nynorsk { + "Du får pensjonsopptening for omsorgsarbeid for " + aarInnvilgetOmsorgspoeng.format() },
                english { + "Earned pension savings for unpaid care work for " + aarInnvilgetOmsorgspoeng.format() },
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            includePhrase(Omsorgsopptjening.HjelpestoenadInnledn(aarInnvilgetOmsorgspoeng))

            showIf(foedtEtter1953) {
                includePhrase(Omsorgsopptjening.HjelpestKap20Hjemmel)
            } orShow {
                includePhrase(Omsorgsopptjening.HjelpestKap3Hjemmel)
            }

            includePhrase(Omsorgsopptjening.Info)
            includePhrase(Omsorgsopptjening.OverforingInfo)
            includePhrase(Omsorgsopptjening.HjelpestonadAutoGodkjennInfo)
            includePhrase(Felles.HarDuSpoersmaal.omsorg)
        }
    }
}
