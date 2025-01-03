package no.nav.pensjon.brev

data class UnleashToggle(val name: String) {
    fun isEnabled() = FeatureToggleHandler.isEnabled(this)
}

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = UnleashToggle("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = UnleashToggle("pl_7822.endringer_ut_endret_pga_inntekt")
}