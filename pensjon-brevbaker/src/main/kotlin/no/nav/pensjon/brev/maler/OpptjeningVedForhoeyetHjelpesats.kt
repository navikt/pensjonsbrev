package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate

// BrevTypeKode: MF_000094
object OpptjeningVedForhoeyetHjelpesats : VedtaksbrevTemplate {

    override val kode = Brevkode.Vedtak.OMSORGP_GODSKRIVING

    override val template = createTemplate(
        name = kode.name,
        base = PensjonLatex,
        letterDataType = OpptjeningVedForhoeyetHjelpesatsDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        val aarInnvilget = argument().select(OpptjeningVedForhoeyetHjelpesatsDto::aarInnvilgetOmrsorgspoeng)

        title {
            textExpr(
                Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for ".expr() + aarInnvilget.format(),
                Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for ".expr() + aarInnvilget.format(),
                English to "Earned pension savings for unpaid care work for ".expr() + aarInnvilget.format(),
            )
        }

        outline {
            includePhrase(Vedtak.overskrift)

            includePhrase(Omsorgsopptjening.hjelpestoenadInnledn, aarInnvilget)

            val foedtEtter1953 = felles().select(Felles::bruker).select(Bruker::foedselsdato).select(LocalDate::getYear).greaterThan(1953)
            showIf(foedtEtter1953) {
                includePhrase(Omsorgsopptjening.hjelpestKap20Hjemmel)
            } orShow {
                includePhrase(Omsorgsopptjening.hjelpestKap3Hjemmel)
            }

            includePhrase(Omsorgsopptjening.info)

            includePhrase(Omsorgsopptjening.overforingInfo)

            includePhrase(Omsorgsopptjening.hjelpestonadAutoGodkjennInfo)
        }

    }
}
