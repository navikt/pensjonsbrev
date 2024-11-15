package no.nav.pensjon.brev

data class UnleashToggle(val name: String) {
    fun isEnabled() = FeatureToggleInitializer.isEnabled(this)
}

object FeatureToggles {
    // Sett inn featuretoggles her
}