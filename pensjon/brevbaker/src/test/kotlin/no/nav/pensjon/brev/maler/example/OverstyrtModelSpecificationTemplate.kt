package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.maler.example.selectors.overstyrtModelSpecificationDto.saksbehandlerValg
import no.nav.pensjon.brev.maler.example.selectors.overstyrtModelSpecificationDto.saksbehandlervalg.pensjonInnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*

enum class OverstyrtModelSpecificationBrevkode : Brevkode.Redigerbart {
    OVERSTYRT_MODEL_SPECIFICATION;

    override fun kode() = name
}

@TemplateModelHelpers
object OverstyrtModelSpecificationTemplate : RedigerbarTemplate<OverstyrtModelSpecificationDto> {

    override val kode = OverstyrtModelSpecificationBrevkode.OVERSTYRT_MODEL_SPECIFICATION
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all
    override val modelSpecification = TemplateModelSpecification(emptyMap(), null)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er et redigerbart eksempel-brev", // Display title for external systems
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Redigerbart eksempelbrev" },
                nynorsk { +"Redigerbart eksempelbrev" }
            )
        }

        // Main letter content
        outline {
            showIf(saksbehandlerValg.pensjonInnvilget) {
                title1 {
                    text(bokmal { +"Du har fått innvilget pensjon" }, nynorsk { +"Du har fått innvilget pensjon" })
                }
            } orShow {
                title1 {
                    text(bokmal { +"Du har ikke fått innvilget pensjon" }, nynorsk { +"Du har ikkje fått innvilget pensjon" })
                }
            }
            paragraph {
                text(
                    bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." },
                    nynorsk { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." }
                )
            }

        }
    }
}

// This data class should normally be in the api-model. Placed here for test-purposes.
data class OverstyrtModelSpecificationDto(
    override val saksbehandlerValg: Saksbehandlervalg,
    override val pesysData: EmptyFagsystemdata,
) : RedigerbarBrevdata<OverstyrtModelSpecificationDto.Saksbehandlervalg, EmptyFagsystemdata> {
    data class Saksbehandlervalg(
        val pensjonInnvilget: Boolean,
        val navneliste: List<String>,
        val pensjonBeloep: Int?,
    ) : SaksbehandlerValgBrevdata
}
