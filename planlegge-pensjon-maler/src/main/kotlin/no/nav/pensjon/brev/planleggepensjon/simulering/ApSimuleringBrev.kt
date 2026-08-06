package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.IBrevkategori
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.Brevkategori
import no.nav.pensjon.brev.planleggepensjon.FeatureToggles
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonBrevkoder
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.apSimuleringBrevDto.saksbehandlerValg
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.apSimuleringDto.simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpOffentligLivsvarig
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpOffentligTidsbegrenset
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpPrivat
import no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg.simuleringVedlegg
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

data class ApSimuleringBrevDto(override val saksbehandlerValg: ApSimuleringDto, override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata) : RedigerbarBrevdata<ApSimuleringDto, EmptyFagsystemdata>

@TemplateModelHelpers
object ApSimuleringBrev : RedigerbarTemplate<ApSimuleringBrevDto> {
    override val kategori: IBrevkategori = Brevkategori.AP_SIMULERINGSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<ISakstype> = emptySet()
    override val kode: Brevkode.Redigerbart = PlanleggePensjonBrevkoder.Redigerbar.PENSJONSKALKULATOR_AP_SIMULERING
    override val featureToggle = FeatureToggles.apSimulering.toggle
    override val modelSpecification: TemplateModelSpecification = TemplateModelSpecification(emptyMap(), null)

    override val template: LetterTemplate<*, ApSimuleringBrevDto> = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Beregning av pensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            showIf(saksbehandlerValg.simulering.afpPrivat.notNull()) {
                text(bokmal { +"Beregning av alderspensjon og AFP i privat sektor" })
            }.orShowIf(saksbehandlerValg.simulering.afpOffentligTidsbegrenset.notNull() or saksbehandlerValg.simulering.afpOffentligLivsvarig.notNull()) {
                text(bokmal { +"Beregning av AFP i offentlig sektor etterfulgt av alderspensjon" })
            }.orShow {
                text(bokmal { +"Beregning av alderspensjon" })
            }
        }

        outline {
        }

        includeAttachment(
            simuleringVedlegg,
            saksbehandlerValg,
        )
    }
}
