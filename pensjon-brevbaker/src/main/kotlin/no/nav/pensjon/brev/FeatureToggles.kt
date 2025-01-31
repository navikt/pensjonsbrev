package no.nav.pensjon.brev

import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.Toggle

data class UnleashToggle(val name: String) : FeatureToggle, Toggle, StableHash by StableHash.of("Toggle: $name") {
    override fun isEnabled() = FeatureToggleHandler.isEnabled(this)
    override fun key() = name
}

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = UnleashToggle("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = UnleashToggle("pl_7822.endringer_ut_endret_pga_inntekt")
    val brevMedFritekst = UnleashToggle("brevMedFritekst")
    val brevmalUTavslag = UnleashToggle("brevmal_ut_avslag")
}