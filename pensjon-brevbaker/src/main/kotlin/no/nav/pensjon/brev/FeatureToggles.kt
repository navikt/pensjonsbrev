package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.template.StableHash

data class ToggleImpl(val name: String) : FeatureToggle, StableHash by StableHash.of("Toggle: $name") {
    override fun key() = name
}

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = ToggleImpl("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = ToggleImpl("pl_7822.endringer_ut_endret_pga_inntekt")
}