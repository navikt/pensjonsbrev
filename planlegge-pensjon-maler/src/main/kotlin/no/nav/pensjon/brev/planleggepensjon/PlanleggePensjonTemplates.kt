package no.nav.pensjon.brev.planleggepensjon

import no.nav.pensjon.brev.api.model.IBrevkategori
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrev
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.ServiceberegningBrev
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object PlanleggePensjonTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>> = setOf()

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(ApSimuleringBrev, ServiceberegningBrev)

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
}

enum class Brevkategori : IBrevkategori {
    AP_SIMULERINGSBREV,
    SERVICEBEREGNING_SIMULERINGSBREV;

    override val kode = name
}

object PlanleggePensjonBrevkoder {
    enum class AutoBrev : Brevkode.Automatisk {
        ;
        override fun kode() = name
    }
    enum class Redigerbar : Brevkode.Redigerbart {
        PENSJONSKALKULATOR_AP_SIMULERING,
        SERVICEBEREGNING_SIMULERINGSBREV;
        override fun kode() = name
    }
}

enum class FeatureToggles(key: String) {
    apSimulering("apSimulering");
    val toggle = FeatureToggle(key)
}
