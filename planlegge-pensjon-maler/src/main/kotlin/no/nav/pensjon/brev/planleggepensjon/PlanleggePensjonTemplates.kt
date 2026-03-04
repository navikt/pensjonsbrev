package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringBrev
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object PlanleggePensjonTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>> = setOf()

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(SimuleringBrev)

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
}

enum class Brevkategori : TemplateDescription.IBrevkategori {
    SIMULERINGSBREV;

    override val kode = name
}

object PlanleggePensjonBrevkoder {
    enum class AutoBrev : Brevkode.Automatisk {
        ;
        override fun kode() = name
    }
    enum class Redigerbar : Brevkode.Redigerbart {
        PENSJONSKALKULATOR_AP_SIMULERING;
        override fun kode() = name
    }
}

enum class FeatureToggles(key: String) {
    apSimulering("apSimulering");
    val toggle = FeatureToggle(key)
}