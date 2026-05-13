package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.Brevkategori
import no.nav.pensjon.brev.planleggepensjon.FeatureToggles
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonBrevkoder
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrevDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg.forbeholdVedlegg
import no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg.simuleringVedlegg
import no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg.opptjeningVedlegg
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

data class ApSimuleringBrevDto(override val saksbehandlerValg: ApSimuleringDto, override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata) : RedigerbarBrevdata<ApSimuleringDto, EmptyFagsystemdata>

@TemplateModelHelpers
object ApSimuleringBrev : RedigerbarTemplate<ApSimuleringBrevDto> {
    override val kategori: TemplateDescription.IBrevkategori = Brevkategori.AP_SIMULERINGSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<TemplateDescription.ISakstype> = emptySet()
    override val kode: Brevkode.Redigerbart = PlanleggePensjonBrevkoder.Redigerbar.PENSJONSKALKULATOR_AP_SIMULERING
    override val featureToggle = FeatureToggles.apSimulering.toggle

    override val template: LetterTemplate<*, ApSimuleringBrevDto> = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Beregning av alderspensjon og AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(bokmal { +"Beregning av pensjon" })
        }

        outline {
            title1 {
                text(bokmal { +"Beregning av alderspensjon og AFP i privat sektor" })
            }

            paragraph {
                text(
                    bokmal {
                        +"Du har fått en foreløpig beregning av alderspensjon og AFP i privat sektor. "
                        +"Se vedlegg for beløp og detaljer om beregningen. "
                        +"Kontakt Nav hvis du har spørsmål."
                    },
                )
            }

        }

        includeAttachmentIfNotNull(
            simuleringVedlegg,
            saksbehandlerValg.simuleringsinformasjon,
        )

        includeAttachmentIfNotNull(
            opptjeningVedlegg,
            saksbehandlerValg.simuleringsinformasjon,
        )

        includeAttachment(forbeholdVedlegg)
    }
}