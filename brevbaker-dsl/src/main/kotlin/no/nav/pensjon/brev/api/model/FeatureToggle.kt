package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.FeatureToggleService

object FeatureToggleSingleton {
    private lateinit var featureToggleService: FeatureToggleService

    fun init(featureToggleService: FeatureToggleService) {
        this.featureToggleService = featureToggleService
    }

    fun isEnabled(toggle: FeatureToggle) = featureToggleService.isEnabled(toggle)
}