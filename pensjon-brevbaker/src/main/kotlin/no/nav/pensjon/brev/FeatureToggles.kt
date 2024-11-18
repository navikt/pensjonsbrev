package no.nav.pensjon.brev

data class UnleashToggle(val name: String) {
    fun isEnabled() = FeatureToggleHandler.isEnabled(this)
}

object FeatureToggles {
    val varselVersjon2 = UnleashToggle("varselVersjon2")
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
}