package no.nav.pensjon.brev

import no.nav.pensjon.brev.template.StableHash

data class UnleashToggle(val name: String) : StableHash by StableHash.of("Toggle: $name") {
    fun isEnabled() = FeatureToggleHandler.isEnabled(this)
}

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = UnleashToggle("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = UnleashToggle("pl_7822.endringer_ut_endret_pga_inntekt")
}