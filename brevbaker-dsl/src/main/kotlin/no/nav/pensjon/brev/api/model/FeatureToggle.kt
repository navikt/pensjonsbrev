package no.nav.pensjon.brev.api.model

interface FeatureToggle : ToggleName {
    fun isEnabled(): Boolean
}