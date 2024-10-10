package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate

data class OmstillingsstoenadInntektsjusteringInfobrevDTO (
    val wip : Boolean
)

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringInfobrev :
    EtterlatteTemplate<OmstillingsstoenadInntektsjusteringInfobrevDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadInntektsjusteringInfobrevDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Du må oppdatere din informasjon om inntekt",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
        ) {
            title {
                text(
                    Bokmal to "Du må oppdatere din informasjon om inntekt",
                    Nynorsk to "Du må oppdatere din informasjon om inntekt",
                    English to "You need to update your income information",
                )
            }

        }
}

