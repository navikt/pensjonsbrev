package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.FeatureToggleService

object FeatureToggleSingleton {
    private lateinit var featureToggleService: FeatureToggleService

    fun init(featureToggleService: FeatureToggleService) {
        this.featureToggleService = featureToggleService
    }

    fun isEnabled(toggle: FeatureToggle): Boolean {
        if (!::featureToggleService.isInitialized) {
            throw IllegalStateException("Du må sette opp en FeatureToggleService med FeatureToggleSingleton::init for å kunne bruke feature toggles.")
        }
        return featureToggleService.isEnabled(toggle)
    }
}