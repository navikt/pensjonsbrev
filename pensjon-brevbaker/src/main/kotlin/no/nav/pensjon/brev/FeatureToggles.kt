package no.nav.pensjon.brev

internal data class UnleashToggle(val name: String) {
    fun isEnabled() = FeatureToggleHandler.isEnabled(this)
}

internal object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
}