package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.Brevkategori
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonBrevkoder
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

data class SimuleringBrevDto(override val saksbehandlerValg: LagreSimuleringDto, override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata) : RedigerbarBrevdata<LagreSimuleringDto, EmptyFagsystemdata>

@TemplateModelHelpers
object SimuleringBrev : RedigerbarTemplate<SimuleringBrevDto> {
    override val kategori: TemplateDescription.IBrevkategori = Brevkategori.SIMULERINGSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<TemplateDescription.ISakstype> = emptySet()
    override val kode: Brevkode.Redigerbart = PlanleggePensjonBrevkoder.Redigerbar.PENSJONSKALKULATOR_SIMULERING

    override val template: LetterTemplate<*, SimuleringBrevDto> = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Simulering av alderspensjon", //TODO Riktig navn på brevet - journalpost tittel i dokumentoversikten
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, //TODO Distribusjonstype.VIKTIG?
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(bokmal { +"Her er tittelen som skal stå inne i brevet" })
        }

        outline {
            paragraph {
                text(
                    bokmal { +
                    "Du har bedt oss om å simulere din alderspensjon." },
                )
            }

        }
    }
}