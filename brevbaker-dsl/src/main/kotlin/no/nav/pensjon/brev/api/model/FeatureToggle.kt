package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.template.StableHash

object FeatureToggleSingleton {
    private lateinit var featureToggleService: FeatureToggleService
    val isInitialized get() = ::featureToggleService.isInitialized

    fun init(featureToggleService: FeatureToggleService) {
        this.featureToggleService = featureToggleService
    }

    fun isEnabled(toggle: FeatureToggle): Boolean {
        if (!isInitialized) {
            throw IllegalStateException("Du må sette opp en FeatureToggleService med FeatureToggleSingleton::init for å kunne bruke feature toggles.")
        }
        return featureToggleService.isEnabled(toggle)
    }

}

data class ToggleImpl(val name: String) : FeatureToggle, StableHash by StableHash.of("Toggle: $name") {
    override fun key() = name
}