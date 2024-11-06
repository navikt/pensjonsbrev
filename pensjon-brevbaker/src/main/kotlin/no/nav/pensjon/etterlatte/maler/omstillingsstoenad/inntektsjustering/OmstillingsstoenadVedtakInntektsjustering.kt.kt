package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal

data class OmstillingsstoenadVedtakInntektsjusteringDTO(
    override val innhold: List<Element>,
    val inntektsaar: Int
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadVedtakInntektsjustering : EtterlatteTemplate<OmstillingsstoenadVarselInntektsjusteringDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadVarselInntektsjusteringDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtaksbrev - inntektsjustering",
                isSensitiv = true,
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                text(
                    Bokmal to "Forhåndsvarsel om vurdering av omstillingsstønad for <år>",
                    Nynorsk to "Førehandsvarsel om vurdering av omstillingsstønad for <år>",
                    English to "Advance notice of assessment of adjustment allowance for <år>",
                )
            }

            // TODO: implementere brev
        }

    }