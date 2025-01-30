package no.nav.pensjon.brev

import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.api.model.FeatureToggle

data class UnleashToggle(val name: String) : FeatureToggle, StableHash by StableHash.of("Toggle: $name") {
    override fun isEnabled() = FeatureToggleHandler.isEnabled(this)
}

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = UnleashToggle("pl_7231.foreventet_svartid")
}